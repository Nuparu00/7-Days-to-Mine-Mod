package nuparu.sevendaystomine.world.gen.city;

import net.minecraft.nbt.NBTTagCompound;

public class CityData {

	public int chunkX;
	public int chunkZ;
	public int population;
	public int zombieLevel;

	public CitySavedData data;

	public CityData(City city, CitySavedData data) {
		this.population = city.population;
		this.chunkX = city.start.getX() / 16;
		this.chunkZ = city.start.getZ() / 16;
		//Max population is 229376
		this.zombieLevel = (population / 1792)*8;
		this.data = data;
	}

	public CityData(NBTTagCompound nbt, CitySavedData data) {
		readNBT(nbt);
		this.data = data;
	}

	public void readNBT(NBTTagCompound nbt) {
		this.population = nbt.getInteger("population");
		this.chunkX = nbt.getInteger("chunkX");
		this.chunkZ = nbt.getInteger("chunkZ");
		this.setZombieLevel(nbt.getInteger("zombieLevel"));
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("population", population);
		nbt.setInteger("chunkX", chunkX);
		nbt.setInteger("chunkZ", chunkZ);
		nbt.setInteger("zombieLevel", getZombieLevel());
		return nbt;
	}

	public int getZombieLevel() {
		return zombieLevel;
	}

	public void setZombieLevel(int zombieLevel) {
		int zombieLevelPrev = this.zombieLevel;
		if (this.data != null) {
			this.zombieLevel = Math.max(0, zombieLevel);
			if (this.zombieLevel != zombieLevelPrev) {
				this.data.markDirty();
			}
		}
	}

}
