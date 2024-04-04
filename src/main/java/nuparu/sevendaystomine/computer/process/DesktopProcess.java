package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;
import java.util.ListIterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.DesktopShortcut;
import nuparu.sevendaystomine.client.gui.monitor.TaskbarButton;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.computer.process.WindowsDesktopProcess.IconPosUpdate;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.StartProcessMessage;
import nuparu.sevendaystomine.network.packets.SyncIconMessage;

public abstract class DesktopProcess extends TickingProcess {

	public ArrayList<ResourceLocation> processQueue = new ArrayList<ResourceLocation>();
	public boolean start = false;

	public ArrayList<DesktopShortcut> shortcuts = new ArrayList<DesktopShortcut>();
	public ArrayList<TaskbarButton> taskbarIcons = new ArrayList<TaskbarButton>();

	protected boolean shutdown = false;

	public DesktopProcess() {
		super();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.shutdown) {
			if (computerTE != null) {
				computerTE.stopComputer();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("shutdown", shutdown);

		NBTTagList list = new NBTTagList();

		ListIterator<ResourceLocation> it = ((ArrayList<ResourceLocation>) (processQueue.clone())).listIterator();
		while (it.hasNext()) {
			ResourceLocation process = it.next();
			list.appendTag(new NBTTagString(process.toString()));
		}

		nbt.setTag("processQueue", list);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.shutdown = nbt.getBoolean("shutdown");

		NBTTagList list = nbt.getTagList("processQueue", Constants.NBT.TAG_STRING);
		if (computerTE != null && !computerTE.getWorld().isRemote) {
			for (int i = 0; i < list.tagCount(); i++) {
				TickingProcess process = ProcessRegistry.INSTANCE
						.getByRes(new ResourceLocation(list.getStringTagAt(i) + "_process"));
				if (process != null) {
					if (process instanceof WindowedProcess) {
						WindowedProcess wp = (WindowedProcess) process;
						wp.application = ApplicationRegistry.INSTANCE
								.getByRes(new ResourceLocation(list.getStringTagAt(i)));
						wp.x = -1;
						wp.y = -1;
						wp.width = 100;
						wp.height = 100;
						wp.zLevel = 4;
					}
					computerTE.startProcess(process);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void onIconMove(Application app, double x, double y) {
		IconPosUpdate update = new IconPosUpdate();
		update.app = app;
		update.x = x;
		update.y = y;

		PacketManager.syncIcon
				.sendToServer(new SyncIconMessage(computerTE.getPos(), update.writeToNBT(new NBTTagCompound())));

	}

	public void tryToRunProcess(ResourceLocation res) {
		this.processQueue.add(res);

		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		PacketManager.startProcess.sendToServer(new StartProcessMessage(computerTE.getPos(), nbt));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {

	}
	
	public double getTaskbarHeight() {
		return screen.ySize * 0.1;	
	}
	
	public double iconSize() {
		return screen.ySize * 0.1;	
	}
}
