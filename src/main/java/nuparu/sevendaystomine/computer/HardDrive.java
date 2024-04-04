package nuparu.sevendaystomine.computer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;

public class HardDrive {

	public TileEntityComputer te;

	public ArrayList<Application> applications = new ArrayList<Application>();
	public ConcurrentHashMap<double[], Application> desktopIcons = new ConcurrentHashMap<double[], Application>();
	public ConcurrentHashMap<double[], File> files = new ConcurrentHashMap<double[], File>();

	private boolean bussy = false;

	public HardDrive(TileEntityComputer te) {
		this.te = te;
		if (te.getSystem() == EnumSystem.MAC) {
			desktopIcons.put(new double[] { 0.1, 32 }, ApplicationRegistry.INSTANCE.getByString("shell"));
			desktopIcons.put(new double[] { 0.1, 96 }, ApplicationRegistry.INSTANCE.getByString("notes"));
			desktopIcons.put(new double[] { 0.1, 64 }, ApplicationRegistry.INSTANCE.getByString("cctv"));
			desktopIcons.put(new double[] { 20, 32 }, ApplicationRegistry.INSTANCE.getByString("explorer"));
			desktopIcons.put(new double[] { 20, 64 }, ApplicationRegistry.INSTANCE.getByString("transit"));
			desktopIcons.put(new double[] { 20, 96 }, ApplicationRegistry.INSTANCE.getByString("epidemic"));
		} else {
			desktopIcons.put(new double[] { 0.1, 0.1 }, ApplicationRegistry.INSTANCE.getByString("shell"));
			desktopIcons.put(new double[] { 0.1, 64 }, ApplicationRegistry.INSTANCE.getByString("notes"));
			desktopIcons.put(new double[] { 0.1, 32 }, ApplicationRegistry.INSTANCE.getByString("cctv"));
			desktopIcons.put(new double[] { 20, 0.1 }, ApplicationRegistry.INSTANCE.getByString("explorer"));
			desktopIcons.put(new double[] { 20, 32 }, ApplicationRegistry.INSTANCE.getByString("transit"));
			desktopIcons.put(new double[] { 20, 64 }, ApplicationRegistry.INSTANCE.getByString("epidemic"));
		}
	}

	@SuppressWarnings("unchecked")
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (applications != null) {
			NBTTagCompound apps = new NBTTagCompound();
			apps.setInteger("count", applications.size());
			for (int i = 0; i < applications.size(); i++) {
				Application app = applications.get(i);
				apps.setString("app" + i, ApplicationRegistry.INSTANCE.getResByApp(app).toString());
			}
			nbt.setTag("applications", apps);
		}
		if (desktopIcons != null) {
			NBTTagList icons = new NBTTagList();
			int i = 0;
			bussy = true;
			Iterator<Entry<double[], Application>> it = desktopIcons.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<double[], Application> entry = it.next();
				double[] coords = entry.getKey();
				Application app = entry.getValue();

				if (coords.length < 2) {
					continue;
				}

				NBTTagCompound icon = new NBTTagCompound();
				icon.setDouble("x", coords[0]);
				icon.setDouble("y", coords[1]);
				icon.setString("app", ApplicationRegistry.INSTANCE.getResByApp(app).toString());
				icons.appendTag(icon);
				++i;
			}
			bussy = false;
			nbt.setTag("icons", icons);
		}

		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		applications.clear();

		if (nbt.hasKey("applications")) {
			NBTTagCompound apps = nbt.getCompoundTag("applications");
			if (apps.hasKey("count")) {
				int count = apps.getInteger("count");
				if (count > 0) {
					for (int i = 0; i < count; i++) {
						String s = apps.getString("app" + i);
						ResourceLocation res = new ResourceLocation(s);
						Application app = ApplicationRegistry.INSTANCE.getByRes(res);
						if (app != null) {
							applications.add(app);
						}
					}
				}
			}
		}
		desktopIcons.clear();
		if (nbt.hasKey("icons")) {
			NBTTagList icons = nbt.getTagList("icons", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < icons.tagCount(); i++) {
				NBTTagCompound icon = icons.getCompoundTagAt(i);
				double x = icon.getDouble("x");
				double y = icon.getDouble("y");
				String s = icon.getString("app");
				ResourceLocation res = new ResourceLocation(s);
				Application app = ApplicationRegistry.INSTANCE.getByRes(res);
				if (app != null) {
					desktopIcons.put(new double[] { x, y }, app);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setIconPostion(double x, double y, Application app) {
		for (Map.Entry<double[], Application> entry : desktopIcons.entrySet()) {
			if (entry.getValue() == app) {
				desktopIcons.remove(entry.getKey());
				desktopIcons.put(new double[] { x, y }, app);
				te.markDirty();
			}
		}
	}

}
