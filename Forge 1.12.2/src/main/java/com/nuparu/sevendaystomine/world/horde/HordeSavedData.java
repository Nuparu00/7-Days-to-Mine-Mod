package com.nuparu.sevendaystomine.world.horde;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public class HordeSavedData extends WorldSavedData {
	public static final String DATA_NAME = SevenDaysToMine.MODID + ":horde_data";

	protected List<Horde> hordes = new ArrayList<Horde>();
	protected int dim = Integer.MIN_VALUE;

	public HordeSavedData() {
		super(DATA_NAME);
	}

	public HordeSavedData(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		dim = compound.getInteger("dim");
		World world = DimensionManager.getWorld(dim);
		if (world == null) {
			return;
		}

		if (compound.hasKey("hordes")) {
			NBTTagList list = compound.getTagList("hordes", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound nbt = list.getCompoundTagAt(i);
				if (!nbt.hasKey("class"))
					continue;
				String className = nbt.getString("class");

				Class<?> clazz;
				try {
					clazz = Class.forName(className);

					if (clazz == null)
						continue;
					Constructor<?> constructor = clazz.getConstructor(World.class);
					Horde horde = (Horde) constructor.newInstance(world);
					horde.readFromNBT(nbt);
					hordes.add(horde);
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
						| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (Horde horde : hordes) {
			list.appendTag(horde.writeToNBT(new NBTTagCompound()));
		}
		compound.setInteger("dim", dim);
		compound.setTag("hordes", list);
		return compound;
	}

	public void addHorde(Horde horde) {
		if (!hordes.contains(horde)) {
			hordes.add(horde);
			if (dim == Integer.MIN_VALUE) {
				dim = horde.world.provider.getDimension();
			}
		}
		markDirty();
	}

	public Horde getHordeByUUID(UUID uuid) {
		for (Horde horde : hordes) {
			if (horde.uuid.equals(uuid)) {
				return horde;
			}
		}
		return null;
	}

	public void removeHorde(Horde horde) {
		if (hordes.contains(horde)) {
			hordes.remove(horde);
			horde.onRemove();
			markDirty();
		}
	}

	public void clear() {
		for (Horde horde : hordes) {
			horde.onRemove();
		}
		hordes.clear();
		markDirty();
	}

	public static HordeSavedData get(World world) {
		if (world != null) {
			HordeSavedData data = (HordeSavedData) world.getPerWorldStorage().getOrLoadData(HordeSavedData.class,
					DATA_NAME);
			if (data == null) {
				data = new HordeSavedData();
				world.getPerWorldStorage().setData(DATA_NAME, (WorldSavedData) data);
			}
			return data;
		}
		return null;
	}

}
