package com.nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public class CitySavedData extends WorldSavedData {
	public static final String DATA_NAME = SevenDaysToMine.MODID + ":city_data";
	private List<Long> cities = new ArrayList<Long>();

	public CitySavedData() {
		super(DATA_NAME);
	}

	public CitySavedData(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		cities.clear();
		if (compound.hasKey("cities")) {
			NBTTagList list = compound.getTagList("cities", Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.tagCount(); i++) {
				System.out.println("READ CITY");
				NBTBase nbt = list.get(i);
				if (nbt instanceof NBTTagLong) {
					cities.add(((NBTTagLong) nbt).getLong());
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (Long l : cities) {
			System.out.println("WRITE CITY");
			list.appendTag(new NBTTagLong(l));
		}
		compound.setTag("cities", list);
		return compound;
	}

	public void addCity(BlockPos pos) {
		this.cities.add(pos.toLong());
		markDirty();
	}

	public boolean isCityNearby(BlockPos pos, long distanceSq) {
		for (Long l : cities) {
			if (pos.distanceSq(BlockPos.fromLong(l)) <= distanceSq) {
				return true;
			}
		}
		return false;
	}

	public static CitySavedData get(World world) {
		if (world != null) {
			CitySavedData data = (CitySavedData) world.getPerWorldStorage().getOrLoadData(CitySavedData.class,
					DATA_NAME);
			if (data == null) {
				data = new CitySavedData();
				world.getPerWorldStorage().setData(DATA_NAME, (WorldSavedData) data);
			}
			return data;
		}
		return null;
	}
	
	public List<Long> getCities(){
		return new ArrayList<Long>(cities);
	}

}
