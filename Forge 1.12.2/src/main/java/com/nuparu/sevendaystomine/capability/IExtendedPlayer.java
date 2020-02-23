package com.nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;

public interface IExtendedPlayer {

	public void setThirst(int thirst);

	public void consumeThirst(int thirst);

	public void addThirst(int thirst);

	public int getThirst();

	public int getMaximumThirst();

	public void setStamina(int stamina);

	public void consumeStamina(int stamina);

	public void addStamina(int stamina);

	public int getStamina();

	public int getMaximumStamina();

	public boolean getCrawling();

	public void setCrawling(boolean state);

	public boolean isInfected();

	public void setInfected(boolean state);

	public int getInfectionStage();

	public void setInfectionStage(int stage);

	public int getInfectionDay();

	public void setInfectionDay(int day);

	public int getTimeToNextStage();

	public void setTimeToNextStage(int time);

	public IExtendedPlayer setOwner(EntityPlayer player);

	public EntityPlayer getOwner();

	public void readNBT(NBTTagCompound nbt);

	public NBTTagCompound writeNBT(NBTTagCompound nbt);

	public void copy(IExtendedPlayer iep);

	public void onDataChanged();

	public void onStartedTracking(EntityPlayer tracker);

}