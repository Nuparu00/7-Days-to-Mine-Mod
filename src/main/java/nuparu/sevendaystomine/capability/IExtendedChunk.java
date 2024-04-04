package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTTagCompound;
import nuparu.sevendaystomine.world.gen.city.City;

public interface IExtendedChunk {

	public void readNBT(NBTTagCompound nbt);

	public NBTTagCompound writeNBT(NBTTagCompound nbt);

	public void copy(IExtendedChunk iep);

	public void onDataChanged();

	public boolean hasCity();

	public void setCity(City city);

	public City getCity();

}