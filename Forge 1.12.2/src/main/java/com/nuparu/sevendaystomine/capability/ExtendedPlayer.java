package com.nuparu.sevendaystomine.capability;

import net.minecraft.world.WorldServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.Callable;

import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.PlayerCapabilitySyncMessage;

import net.minecraft.entity.EntityTracker;

public class ExtendedPlayer implements IExtendedPlayer {

	int maxThirst = 780;
	int maxStamina = 780;

	int thirst = 780;
	int stamina = 780;

	boolean isCrawling;

	boolean isInfected;
	int infectionStage;
	int infectionDay;
	int timeToNextStage;

	EntityPlayer owner;

	public void setThirst(int thirst) {
		this.thirst = thirst;
		onDataChanged();
	}

	public void consumeThirst(int thirst) {
		this.thirst = this.thirst >= thirst ? this.thirst -= thirst : 0;
		onDataChanged();
	}

	public void addThirst(int thirst) {
		this.thirst = this.thirst <= maxThirst - thirst ? this.thirst += thirst : maxThirst;
		onDataChanged();
	}

	public int getThirst() {
		return this.thirst;
	}

	public int getMaximumThirst() {
		return this.maxThirst;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
		onDataChanged();
	}

	public void consumeStamina(int stamina) {
		this.stamina = this.stamina >= stamina ? this.stamina -= stamina : 0;
		onDataChanged();
	}

	public void addStamina(int stamina) {
		this.stamina = this.stamina <= maxStamina - stamina ? this.stamina += stamina : maxStamina;
		onDataChanged();
	}

	public int getStamina() {
		return this.stamina;
	}

	public int getMaximumStamina() {
		return this.maxStamina;
	}

	public boolean getCrawling() {
		return this.isCrawling;
	}

	public void setCrawling(boolean state) {
		this.isCrawling = state;
		onDataChanged();
	}

	public boolean isInfected() {
		return this.isInfected;
	}

	public void setInfected(boolean state) {
		this.isInfected = state;
		onDataChanged();
	}

	public int getInfectionStage() {
		return this.infectionStage;
	}

	public void setInfectionStage(int stage) {
		this.infectionStage = stage;
		onDataChanged();
	}

	public int getInfectionDay() {
		return this.infectionDay;
	}

	public void setInfectionDay(int day) {
		this.infectionDay = day;
		onDataChanged();
	}

	public int getTimeToNextStage() {
		return this.timeToNextStage;
	}

	public void setTimeToNextStage(int time) {
		this.timeToNextStage = time;
		onDataChanged();
	}

	public IExtendedPlayer setOwner(EntityPlayer player) {
		this.owner = player;
		return this;
	}

	public EntityPlayer getOwner() {
		return this.owner;
	}

	public void readNBT(NBTTagCompound nbt) {
		maxThirst = nbt.getInteger("maxThirst");
		maxStamina = nbt.getInteger("maxStamina");
		thirst = nbt.getInteger("thirst");
		stamina = nbt.getInteger("stamina");
		isCrawling = nbt.getBoolean("isCrawling");
		isInfected = nbt.getBoolean("isInfected");
		infectionStage = nbt.getInteger("infectionStage");
		infectionDay = nbt.getInteger("infectionDay");
		timeToNextStage = nbt.getInteger("timeToNextStage");
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("maxThirst", maxThirst);
		nbt.setInteger("maxStamina", maxStamina);

		nbt.setInteger("thirst", thirst);
		nbt.setInteger("stamina", stamina);

		nbt.setBoolean("isCrawling", isCrawling);
		nbt.setBoolean("isInfected", isInfected);

		nbt.setInteger("infectionStage", infectionStage);
		nbt.setInteger("infectionDay", infectionDay);
		nbt.setInteger("timeToNextStage", timeToNextStage);

		return nbt;
	}

	public void copy(IExtendedPlayer iep) {
		readNBT(iep.writeNBT(new NBTTagCompound()));
	}

	public void onDataChanged() {
		if (!owner.world.isRemote) {
			EntityTracker tracker = ((WorldServer) owner.world).getEntityTracker();
			PlayerCapabilitySyncMessage message = new PlayerCapabilitySyncMessage(this, owner);

			for (EntityPlayer entityPlayer : tracker.getTrackingPlayers(owner)) {
				PacketManager.playerCapabilitySync.sendTo(message, (EntityPlayerMP) entityPlayer);
			}
			PacketManager.playerCapabilitySync.sendTo(message, (EntityPlayerMP) owner);
		}
	}

	public void onStartedTracking(EntityPlayer tracker) {

		if (!owner.world.isRemote) {
			PlayerCapabilitySyncMessage message = new PlayerCapabilitySyncMessage(this, owner);
			PacketManager.playerCapabilitySync.sendTo(message, (EntityPlayerMP) tracker);
		}
	}

	public static class Factory implements Callable<IExtendedPlayer> {

		@Override
		public IExtendedPlayer call() throws Exception {
			return new ExtendedPlayer();
		}
	}
	
}