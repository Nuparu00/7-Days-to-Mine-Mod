package nuparu.sevendaystomine.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PlayerCapabilitySyncMessage;
import nuparu.sevendaystomine.util.Utils;

public class ExtendedPlayer implements IExtendedPlayer {
	public static final int MAX_THIRST = 780;
	public static final int MAX_STAMINA = 780;

	List<String> recipes = new ArrayList<String>();
	EntityPlayer player;
	int survivedBloodmoons = 0;
	int lastBloodmoonSurvivalCheck = 0;

	int thirst = 780;
	int stamina = 780;

	boolean isCrawling;

	int infectionTime = -1;

	// Last bloodmoon
	int bloodmoon;
	// Last wolf horde
	int wolfHorde;
	// Last generic horde
	int horde;
	//block drinking counter
	int drinkCounter;

	public ExtendedPlayer() {
		thirst = MAX_THIRST;
		stamina = MAX_STAMINA;
	}

	public void setThirst(int thirst) {
		this.thirst = MathHelper.clamp(thirst, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void consumeThirst(int thirst) {
		this.thirst = MathHelper.clamp(this.thirst - thirst, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void addThirst(int thirst) {
		this.thirst = MathHelper.clamp(this.thirst + thirst, 0, MAX_STAMINA);
		onDataChanged();
	}

	public int getThirst() {
		return this.thirst;
	}

	public int getMaximumThirst() {
		return this.MAX_THIRST;
	}

	public void setStamina(int stamina) {
		this.stamina = MathHelper.clamp(stamina, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void consumeStamina(int stamina) {
		this.stamina = MathHelper.clamp(this.stamina - stamina, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void addStamina(int stamina) {
		this.stamina = MathHelper.clamp(this.stamina + stamina, 0, MAX_STAMINA);
		onDataChanged();
	}

	public int getStamina() {
		return this.stamina;
	}

	public int getMaximumStamina() {
		return this.MAX_STAMINA;
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

		thirst = nbt.getInteger("thirst");
		stamina = nbt.getInteger("stamina");
		isCrawling = nbt.getBoolean("isCrawling");
		infectionTime = nbt.getInteger("infectionTime");
		bloodmoon = nbt.getInteger("bloodmoon");
		wolfHorde = nbt.getInteger("wolfHorde");
		horde = nbt.getInteger("horde");
		drinkCounter = nbt.getInteger("drinkCounter");
		survivedBloodmoons = nbt.getInteger("survivedBloodmoons");
		lastBloodmoonSurvivalCheck = nbt.getInteger("lastBloodmoonSurvivalCheck");

		recipes.clear();
		NBTTagList list = nbt.getTagList("recipes", Constants.NBT.TAG_STRING);
		for (int i = 0; i < list.tagCount(); i++) {
			recipes.add(list.getStringTagAt(i));
		}
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("thirst", thirst);
		nbt.setInteger("stamina", stamina);

		nbt.setBoolean("isCrawling", isCrawling);

		nbt.setInteger("infectionTime", infectionTime);

		nbt.setInteger("bloodmoon", bloodmoon);
		nbt.setInteger("wolfHorde", wolfHorde);
		nbt.setInteger("horde", horde);
		nbt.setInteger("drinkCounter", drinkCounter);
		nbt.setInteger("survivedBloodmoons", survivedBloodmoons);
		nbt.setInteger("lastBloodmoonSurvivalCheck", lastBloodmoonSurvivalCheck);

		NBTTagList list = new NBTTagList();
		for (String rec : recipes) {
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
		if (hasRecipe(rec))
			return;
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
	public boolean hasHorde(World world) {
		int day = Utils.getDay(world);

		return bloodmoon == day || wolfHorde == day || horde == day;
	}

	@Override
	public void setBloodmoon(int i) {
		bloodmoon = i;
		onDataChanged();
	}

	@Override
	public int getBloodmoon() {
		return bloodmoon;
	}

	@Override
	public void setWolfHorde(int i) {
		wolfHorde = i;
		onDataChanged();
	}

	@Override
	public int getWolfHorde() {
		return wolfHorde;
	}

	@Override
	public void setHorde(int i) {
		horde = i;
		onDataChanged();
	}

	@Override
	public int getHorde() {
		return horde;
	}

	@Override
	public boolean isInfected() {
		return infectionTime != -1;
	}

	@Override
	public void setDrinkCounter(int i) {
		drinkCounter = i;
		onDataChanged();
	}

	@Override
	public int getDrinkCounter() {
		return drinkCounter;
	}

	@Override
	public void setSurvivedBloodmoons(int i) {
		survivedBloodmoons = i;
		onDataChanged();
	}

	@Override
	public int getSurvivedBloodmons() {
		return survivedBloodmoons;
	}

	@Override
	public void setLastBloodmoonSurvivalCheck(int i) {
		lastBloodmoonSurvivalCheck = i;
		onDataChanged();
	}

	@Override
	public int getLastBloodmoonSurvivalCheck() {
		return lastBloodmoonSurvivalCheck;
	}

}