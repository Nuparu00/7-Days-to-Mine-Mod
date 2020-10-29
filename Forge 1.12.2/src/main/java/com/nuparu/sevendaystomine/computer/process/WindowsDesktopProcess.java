package com.nuparu.sevendaystomine.computer.process;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.monitor.DesktopShortcut;
import com.nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.gui.monitor.TaskbarButton;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.TexturedButton;
import com.nuparu.sevendaystomine.computer.HardDrive;
import com.nuparu.sevendaystomine.computer.application.Application;
import com.nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.StartProcessMessage;
import com.nuparu.sevendaystomine.network.packets.SyncIconMessage;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.client.Animations;
import com.nuparu.sevendaystomine.util.client.ColorRGBA;
import com.nuparu.sevendaystomine.util.client.RenderUtils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WindowsDesktopProcess extends DesktopProcess {

	@SideOnly(Side.CLIENT)
	public final ResourceLocation WIN_10_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_win10.png");
	@SideOnly(Side.CLIENT)
	public final ResourceLocation WIN_7_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_win7.png");
	@SideOnly(Side.CLIENT)
	public final ResourceLocation WIN_10_START = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/win10_start.png");
	
	@SideOnly(Side.CLIENT)
	public final ResourceLocation WIN_7_START = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/win7_start.png");
	@SideOnly(Side.CLIENT)
	public final ResourceLocation WIN_10_SHUTDOWN = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/win10_shutdown.png");

	@SideOnly(Side.CLIENT)
	public final ColorRGBA WIN_10_COL_DEF = new ColorRGBA(0.16, 0.17, 0.2, 0.8);
	@SideOnly(Side.CLIENT)
	public final ColorRGBA WIN_10_COL_START = new ColorRGBA(0.1, 0.11, 0.14, 0.76);

	@SideOnly(Side.CLIENT)
	public boolean start = false;
	@SideOnly(Side.CLIENT)
	public ArrayList<DesktopShortcut> shortcuts = new ArrayList<DesktopShortcut>();
	@SideOnly(Side.CLIENT)
	public ArrayList<TaskbarButton> taskbarIcons = new ArrayList<TaskbarButton>();

	@SideOnly(Side.CLIENT)
	private static final NumberFormat HOUR_FORMATTER = new DecimalFormat("00");

	private boolean shutdown = false;

	public WindowsDesktopProcess() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		ScaledResolution sr = new ScaledResolution(Screen.mc);
		ResourceLocation start;
		ColorRGBA hover;
		switch (computerTE.getSystem()) {
		default:
		case NONE:
		case WIN10:
			start = WIN_10_START;
			hover = new ColorRGBA(0.3, 0.7, 0.95);
			break;
		case WIN7:
			start = WIN_7_START;
			hover = new ColorRGBA(0.8,0.8,0.8);
			break;
		}
		TexturedButton button = new TexturedButton((sr.getScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getScaledHeight() / 2) + (screen.ySize / 2) - (screen.ySize * 0.1), screen.ySize * 0.1,
				screen.ySize * 0.1, screen, "", start, 0);
		button.textNormal = 0xffffff;
		button.normal = new ColorRGBA(1d, 1d, 1d);
		button.hovered = hover;
		button.setFontSize(0.7);
		button.setProcess(this);
		button.setZLevel(1);
		elements.add(button);

		double i = screen.ySize * 0.05;
		TexturedButton button1 = new TexturedButton((sr.getScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getScaledHeight() / 2) + (screen.ySize / 2) - (1.5 * screen.ySize * 0.1) - 2,i,i, screen, "", WIN_10_SHUTDOWN, 1) {

			@Override
			public boolean isDisabled() {
				return ((WindowsDesktopProcess) tickingProcess).start == false;
			}

		};
		button1.textNormal = 0xffffff;
		button1.normal = new ColorRGBA(1d, 1d, 1d);
		button1.hovered = new ColorRGBA(0.3, 0.7, 0.95);
		button1.setFontSize(0.7);
		button1.setProcess(this);
		button1.setZLevel(3);
		elements.add(button1);

		refreshDesktop();

	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().update();
		}
		Iterator<DesktopShortcut> it2 = shortcuts.iterator();
		while (it2.hasNext()) {
			it2.next().update();
		}
		
		Iterator<TaskbarButton> it3 = taskbarIcons.iterator();
		while (it3.hasNext()) {
			it3.next().update();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.shutdown) {
			if (computerTE != null) {
				computerTE.turnOff();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {

		renderBackground(partialTicks);
		renderTaskbar(partialTicks);
		renderStart(partialTicks);

		for (IScreenElement element : elements) {
			element.render(partialTicks);
		}

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().render(partialTicks);
		}
		
		Iterator<TaskbarButton> it2 = taskbarIcons.iterator();
		while (it2.hasNext()) {
			it2.next().render(partialTicks);
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderBackground(float partialTicks) {
		ScaledResolution sr = new ScaledResolution(Screen.mc);
		ResourceLocation bgr;
		switch (computerTE.getSystem()) {
		default:
		case NONE:
			bgr = WIN_10_BGR_DEF;break;
		case WIN10:
			bgr = WIN_10_BGR_DEF;break;
		case WIN7:
			bgr = WIN_7_BGR_DEF;break;
		}
		RenderUtils.drawTexturedRect(bgr, (sr.getScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getScaledHeight() / 2) - (screen.ySize / 2), 0, 0, screen.xSize, screen.ySize, screen.xSize,
				screen.ySize, 1, 0);
	}

	@SideOnly(Side.CLIENT)
	public void renderTaskbar(float partialTicks) {
		ScaledResolution sr = new ScaledResolution(Screen.mc);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		RenderUtils.drawColoredRect(WIN_10_COL_DEF, (sr.getScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getScaledHeight() / 2) + (screen.ySize / 2) - (screen.ySize * 0.1), screen.xSize,
				(screen.ySize * 0.1), 0);
		GlStateManager.disableBlend();
		GlStateManager.translate(0, 0, 2);

		int hours = 0;
		int minutes = 0;
		if (computerTE != null && computerTE.hasWorld()) {
			hours = Utils.getWorldHours(computerTE.getWorld());
			minutes = Utils.getWorldMinutes(computerTE.getWorld());
		}

		RenderUtils.drawCenteredString(
				new StringBuilder("").append(HOUR_FORMATTER.format(hours)).append(":")
						.append(HOUR_FORMATTER.format(minutes)).toString(),
				screen.getRelativeX(1) - (screen.ySize * 0.1), screen.getRelativeY(1) - (screen.ySize * 0.05) - 4,
				0xffffff);
		GlStateManager.translate(0, 0, -2);
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void renderStart(float partialTicks) {
		if (start) {
			RenderUtils.drawColoredRect(WIN_10_COL_START, screen.getRelativeX(0), screen.getRelativeY(1 / 3d),
					(screen.xSize / 4d), (screen.ySize * (2 / 3d)) - (screen.ySize * 0.1), 3);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 4);
			RenderUtils.drawString(computerTE.getUsername(), screen.getRelativeX(0) + 2,
					screen.getRelativeY(1 / 3d) + 2, 0xffffff);
			GlStateManager.translate(0, 0, -4);
			GlStateManager.popMatrix();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		if (elements.size() == 0) {
			return;
		}
		ScaledResolution sr = new ScaledResolution(Screen.mc);
		IScreenElement element0 = elements.get(0);

		element0.setX((sr.getScaledWidth() / 2) - (screen.xSize / 2));
		element0.setY((sr.getScaledHeight() / 2) + (screen.ySize / 2) - (screen.ySize * 0.1));
		element0.setWidth(screen.ySize * 0.1);
		element0.setHeight(screen.ySize * 0.1);

		IScreenElement element1 = elements.get(1);

		element1.setX((sr.getScaledWidth() / 2) - (screen.xSize / 2));
		element1.setY((sr.getScaledHeight() / 2) + (screen.ySize / 2) - (1.5 * screen.ySize * 0.1) - 2);
		element1.setWidth(screen.ySize * 0.05);
		element1.setHeight(screen.ySize * 0.05);

		refreshDesktop();

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
						.getByRes(new ResourceLocation(list.getStringTagAt(i)+"_process"));
				if (process != null) {
					if(process instanceof WindowedProcess) {
						WindowedProcess wp = (WindowedProcess)process;
						wp.application = ApplicationRegistry.INSTANCE.getByRes(new ResourceLocation(list.getStringTagAt(i)));
						wp.x = Screen.screen.getRelativeX(0.5)-50;
						wp.y = Screen.screen.localYToGlobal(0);
						wp.width=100;
						wp.height=100;
						wp.zLevel=4;
						wp.setScreen(getScreen());
					}
					computerTE.startProcess(process);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		int buttonId = button.ID;
		if (buttonId == 0) {
			start = !start;
			return;
		}
		if (buttonId == 1) {
			this.shutdown = true;
			sync("shutdown");
			return;
		}
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void refreshDesktop() {

		shortcuts.clear();

		double iconSize = screen.ySize * 0.1;

		HardDrive drive = computerTE.getHardDrive();

		Iterator<Entry<double[], Application>> it = new HashMap<double[], Application>(drive.desktopIcons).entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry<double[], Application> entry = it.next();
			double[] coords = entry.getKey();
			Application app = entry.getValue();

			if (coords.length < 2 || app == null) {
				continue;
			}

			double x = coords[0];
			double y = coords[1];

			shortcuts.add(new DesktopShortcut(screen.getRelativeX(0) + x, screen.getRelativeY(0) + y, iconSize,
					iconSize, screen, this, app));

		}
		
		this.taskbarIcons.clear();
		
		ScaledResolution sr = new ScaledResolution(Screen.mc);
		
		ArrayList<TickingProcess> processes= (ArrayList<TickingProcess>) computerTE.getProcessesList().clone();
		for(int i = 0; i < processes.size(); i++) {
			TickingProcess process = processes.get(i);
			if(process instanceof WindowedProcess) {
				TaskbarButton tb = new TaskbarButton((sr.getScaledWidth() / 2) - (screen.xSize / 2) + (screen.ySize*0.1*(i)),
						(sr.getScaledHeight() / 2) + (screen.ySize / 2) - (screen.ySize * 0.1), screen.ySize*0.1,
						screen.ySize * 0.1,this.getScreen(),(WindowedProcess)process);
				tb.setProcess(this);
				tb.setZLevel(5);
				this.taskbarIcons.add(tb);
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

	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().keyTyped(typedChar, keyCode);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().mouseReleased(mouseX, mouseY, state);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		ScaledResolution sr = new ScaledResolution(Screen.mc);
		if (start) {
			if (mouseX > (screen.getRelativeX(0) + (screen.xSize / 4d)) || mouseX < screen.getRelativeX(0)
					|| (mouseY < screen.getRelativeY(1 / 3d)
							|| mouseY > (screen.ySize * (2 / 3d)) - (screen.ySize * 0.1) + screen.getRelativeY(1 / 3d))
							&& !Utils.isInArea(mouseX, mouseY, (sr.getScaledWidth() / 2) - (screen.xSize / 2),
									(sr.getScaledHeight() / 2) + (screen.ySize / 2) - (screen.ySize * 0.1),
									screen.ySize * 0.1, screen.ySize * 0.1)) {
				start = false;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		Iterator<TaskbarButton> it2 = taskbarIcons.iterator();
		while (it2.hasNext()) {
			it2.next().mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	public void tryToRunProcess(ResourceLocation res) {
		this.processQueue.add(res);
		
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		PacketManager.startProcess.sendToServer(new StartProcessMessage(computerTE.getPos(), nbt));
	}

	public static class IconPosUpdate {
		public Application app;
		public double x;
		public double y;

		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt.setString("app", ApplicationRegistry.INSTANCE.getResByApp(app).toString());
			nbt.setDouble("x", x);
			nbt.setDouble("y", y);
			return nbt;
		}

		public void readFromNBT(NBTTagCompound nbt) {
			String s = nbt.getString("app");
			ResourceLocation res = new ResourceLocation(s);
			app = ApplicationRegistry.INSTANCE.getByRes(res);
			x = nbt.getDouble("x");
			y = nbt.getDouble("y");
		}
	}

}
