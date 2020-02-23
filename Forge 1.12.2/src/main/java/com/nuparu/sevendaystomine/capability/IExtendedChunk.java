package com.nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTTagCompound;

import com.nuparu.sevendaystomine.world.gen.city.City;

import net.minecraft.entity.player.EntityPlayer;

public interface IExtendedChunk {

	public void readNBT(NBTTagCompound nbt);

	public NBTTagCompound writeNBT(NBTTagCompound nbt);

	public void copy(IExtendedChunk iep);

	public void onDataChanged();
	
	public boolean hasCity();
	
	public void setCity(City city);
	
	public City getCity();

}