package com.nuparu.sevendaystomine.capability;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
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

	int infectionTime = -1;
	
	//Has been a bloodmoon horde spawnde for this player on the current day?
	boolean bloodmoon;
	
	
	List<String> recipes = new ArrayList<String>();

	EntityPlayer player;

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


	public int getInfectionTime() {
		return this.infectionTime;
	}

	public void setInfectionTime(int time) {
		this.infectionTime = time;
		onDataChanged();
	}

	public IExtendedPlayer setOwner(EntityPlayer player) {
		this.player = player;
		return this;
	}

	public EntityPlayer getOwner() {
		return this.player;
	}

	public void readNBT(NBTTagCompound nbt) {
		maxThirst = nbt.getInteger("maxThirst");
		maxStamina = nbt.getInteger("maxStamina");
		thirst = nbt.getInteger("thirst");
		stamina = nbt.getInteger("stamina");
		isCrawling = nbt.getBoolean("isCrawling");
		infectionTime = nbt.getInteger("infectionTime");
		bloodmoon = nbt.getBoolean("bloodmoon");
		
		
		recipes.clear();
		NBTTagList list = nbt.getTagList("recipes", Constants.NBT.TAG_STRING);
		for (int i = 0; i < list.tagCount(); i++) {
			recipes.add(list.getStringTagAt(i));
		}
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("maxThirst", maxThirst);
		nbt.setInteger("maxStamina", maxStamina);

		nbt.setInteger("thirst", thirst);
		nbt.setInteger("stamina", stamina);

		nbt.setBoolean("isCrawling", isCrawling);

		nbt.setInteger("infectionTime", infectionTime);
		
		nbt.setBoolean("bloodmoon", bloodmoon);
		
		NBTTagList list = new NBTTagList();
		for(String rec : recipes) {
			list.appendTag(new NBTTagString(rec));
		}
		nbt.setTag("recipes", list);

		return nbt;
	}

	public void copy(IExtendedPlayer iep) {
		readNBT(iep.writeNBT(new NBTTagCompound()));
	}

	public void onDataChanged() {
		if (!player.world.isRemote) {
			EntityTracker tracker = ((WorldServer) player.world).getEntityTracker();
			PlayerCapabilitySyncMessage message = new PlayerCapabilitySyncMessage(this, player);

			for (EntityPlayer entityPlayer : tracker.getTrackingPlayers(player)) {
				PacketManager.playerCapabilitySync.sendTo(message, (EntityPlayerMP) entityPlayer);
			}
			PacketManager.playerCapabilitySync.sendTo(message, (EntityPlayerMP) player);
		}
	}

	public void onStartedTracking(EntityPlayer tracker) {

		if (!player.world.isRemote) {
			PlayerCapabilitySyncMessage message = new PlayerCapabilitySyncMessage(this, player);
			PacketManager.playerCapabilitySync.sendTo(message, (EntityPlayerMP) tracker);
		}
	}

	public static class Factory implements Callable<IExtendedPlayer> {

		@Override
		public IExtendedPlayer call() throws Exception {
			return new ExtendedPlayer();
		}
	}

	@Override
	public void unlockRecipe(String rec) {
		if(hasRecipe(rec)) return;
		recipes.add(rec);
		onDataChanged();
	}

	@Override
	public boolean hasRecipe(String rec) {
		return recipes.contains(rec);
	}

	@Override
	public List<String> getRecipes() {
		return recipes;
	}

	@Override
	public boolean getBloodmoon() {
		return bloodmoon;
	}

	@Override
	public void setBloodmoon(boolean state) {
		bloodmoon = state;
		onDataChanged();
	}

	@Override
	public boolean isInfected() {
		return infectionTime != -1;
	}
	
}