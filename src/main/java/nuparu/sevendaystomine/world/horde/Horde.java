package nuparu.sevendaystomine.world.horde;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityZombieBase;
import nuparu.sevendaystomine.util.Utils;

public abstract class Horde {

	// Living zombies
	public List<EntityZombieBase> zombies = new ArrayList<EntityZombieBase>();
	public List<HordeEntry> entries = new ArrayList<HordeEntry>();

	public BlockPos center;
	public World world;
	public UUID uuid;
	public long startTime = 0;
	public int waves;
	public int zombiesInWave = 0;

	public int waveTimer;

	public HordeSavedData data;

	public UUID playerID;

	public Horde(World world) {
		this.world = world;
		startTime = Utils.getDay(world);
	}

	public Horde(BlockPos center, World world) {
		uuid = UUID.randomUUID();
		this.center = center;
		this.world = world;
		startTime = Utils.getDay(world);
		HordeSavedData.get(world).addHorde(this);
	}

	public void onZombieKill(EntityZombieBase zombie) {
		zombies.remove(zombie);
		// HordeSavedData.get(world).addHorde(this);
	}

	public void start() {
		--waves;
		waveTimer = ModConfig.world.hordeWaveDelay;
	}

	public void update() {
		boolean glow = world.getGameRules().getInt("hordeGlow") > zombies.size();
		for (EntityZombieBase zombie : new ArrayList<EntityZombieBase>(zombies)) {
			if (!zombie.isEntityAlive()) {
				zombies.remove(zombie);
				continue;
			}
			if (glow) {
				zombie.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 10000));
			}
		}

		if (startTime != Utils.getDay(world) && world != null) {
			HordeSavedData data = HordeSavedData.get(world);
			if (data != null) {
				data.removeHorde(this);
				return;
			}
		} else if (zombies.size() == 0) {
			if (waves > 0) {
				if (--waveTimer <= 0) {
					start();
				}
			} else if (playerID != null) {
				HordeSavedData data = HordeSavedData.get(world);
				EntityPlayer player = Utils.getPlayerFromUUID(playerID);
				if (player != null) {
					player.addExperience(30);
				}
				if (data != null) {
					data.removeHorde(this);
					return;
				}
			}
		}
	}

	public HordeEntry getHordeEntry(Random rand) {
		int total = 0;
		for (HordeEntry entry : entries) {
			total += entry.weight;
		}
		int i = rand.nextInt(total);
		for (HordeEntry entry : entries) {
			i -= entry.weight;
			if (i <= 0) {
				return entry;
			}
		}
		return null;
	}

	public void addZombie(EntityZombieBase zombie) {
		zombies.add(zombie);
	}

	public void onPlayerStartTacking(EntityPlayerMP player, EntityZombieBase zombie) {

	}

	public void onPlayerStopTacking(EntityPlayerMP player, EntityZombieBase zombie) {

	}

	public void onRemove() {
		for (EntityZombieBase zombie : zombies) {
			zombie.horde = null;
		}

	}

	public void readFromNBT(NBTTagCompound compound) {
		if (world.isRemote) {
			return;
		}
		waves = compound.getInteger("wave");
		zombiesInWave = compound.getInteger("zombiesInWave");
		uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		center = BlockPos.fromLong(compound.getLong("center"));
		startTime = compound.getLong("startTime");
		waveTimer = compound.getInteger("waveTimer");
		if(compound.hasKey("playerid",Constants.NBT.TAG_COMPOUND)) {
			playerID = NBTUtil.getUUIDFromTag(compound.getCompoundTag("playerid"));
		}
		zombies.clear();
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("wave", waves);
		compound.setInteger("zombiesInWave", zombiesInWave);
		compound.setTag("uuid", NBTUtil.createUUIDTag(uuid));
		compound.setLong("center", center.toLong());
		compound.setLong("startTime", startTime);
		compound.setInteger("waveTimer", waveTimer);
		if (playerID != null) {
			compound.setTag("playerid", NBTUtil.createUUIDTag(playerID));
		}
		return compound;
	}

	public static class HordeEntry {
		public ResourceLocation res;
		public int weight;

		public HordeEntry(ResourceLocation res, int weight) {
			this.res = res;
			this.weight = weight;
		}

		public EntityZombieBase spawn(World world, BlockPos pos) {
			Entity e = EntityList.createEntityByIDFromName(res, world);
			if (e == null || !(e instanceof EntityZombieBase))
				return null;
			e.setPosition(pos.getX(), pos.getY(), pos.getZ());
			if (!world.isRemote) {
				world.spawnEntity(e);
				return (EntityZombieBase) e;
			}
			return null;

		}
	}

	public BlockPos getCenter() {
		EntityPlayer player = getPlayer();
		return player != null ? new BlockPos(player) : this.center;
	}

	public EntityPlayer getPlayer() {
		return this.playerID != null ? Utils.getPlayerFromUUID(playerID) : null;
	}
}
