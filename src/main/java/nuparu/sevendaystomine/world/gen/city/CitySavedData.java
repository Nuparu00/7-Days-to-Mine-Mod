package nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.repair.BreakData;

public class CitySavedData extends WorldSavedData {
	public static final String DATA_NAME = SevenDaysToMine.MODID + ":city_data";
	private List<CityData> cities = new ArrayList<CityData>();
	private List<Long> scattered = new ArrayList<Long>();

	public CitySavedData() {
		super(DATA_NAME);
	}

	public CitySavedData(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(scattered == null) {
			scattered = new ArrayList<Long>();
		}
		if(cities == null) {
			cities = new ArrayList<CityData>();
		}
		
		cities.clear();
		if (compound.hasKey("cities")) {
			NBTTagList list = compound.getTagList("cities", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTBase nbt = list.get(i);
				if (nbt instanceof NBTTagCompound) {
					cities.add(new CityData((NBTTagCompound)nbt,this));
				}
			}
		}
		scattered.clear();
		if (compound.hasKey("scattered")) {
			NBTTagList list = compound.getTagList("scattered", Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTBase nbt = list.get(i);
				if (nbt instanceof NBTTagLong) {
					scattered.add(((NBTTagLong) nbt).getLong());
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(scattered == null) {
			scattered = new ArrayList<Long>();
		}
		if(cities == null) {
			cities = new ArrayList<CityData>();
		}
		NBTTagList list = new NBTTagList();
		for (CityData city : cities) {
			list.appendTag(city.writeNBT(new NBTTagCompound()));
		}
		compound.setTag("cities", list);
		list = new NBTTagList();
		for (Long l : scattered) {
			list.appendTag(new NBTTagLong(l));
		}
		compound.setTag("scattered", list);
		return compound;
	}

	public void addCity(City city) {
		if(cities == null) {
			cities = new ArrayList<CityData>();
		}
		this.cities.add(new CityData(city,this));
		markDirty();
	}
	
	public void addScattered(BlockPos pos) {
		if(scattered == null) {
			scattered = new ArrayList<Long>();
		}
		this.scattered.add(new BlockPos(pos.getX(),128,pos.getZ()).toLong());
		markDirty();
	}

	public boolean isCityNearby(BlockPos pos, long distanceSq) {
		if(cities == null) {
			cities = new ArrayList<CityData>();
		}
		int chunkX = pos.getX()*16;
		int chunkZ = pos.getZ()*16;
		
		for (CityData city : cities) {
			return Math.pow(city.chunkX-chunkX,2)+Math.pow(city.chunkZ-chunkZ,2) <= distanceSq;
		}
		return false;
	}
	
	public CityData getClosestCity(BlockPos pos, double maxDstSq) {
		int chunkX = pos.getX()/16;
		int chunkZ = pos.getZ()/16;

		CityData closest = null;
		if(cities == null) {
			cities = new ArrayList<CityData>();
		}
		for (CityData city : cities) {
			double dstSq = Math.pow(city.chunkX-chunkX,2)+Math.pow(city.chunkZ-chunkZ,2);
			if(dstSq < maxDstSq) {
				maxDstSq = dstSq;
				closest = city;
			}
		}
		
		return closest;
	}
	
	public boolean isScatteredNearby(BlockPos pos, long distanceSq) {
		if(scattered == null) {
			scattered = new ArrayList<Long>();
		}
		for (Long l : scattered) {
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
	
	public List<CityData> getCities(){
		return cities == null ? new ArrayList<CityData>() :  new ArrayList<CityData>(cities);
	}
	
	public List<Long> getScattered(){
		return scattered == null ? new ArrayList<Long>() : new ArrayList<Long>(scattered);
	}

}
