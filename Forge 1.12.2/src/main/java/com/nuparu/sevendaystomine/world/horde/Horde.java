package com.nuparu.sevendaystomine.world.horde;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Horde {
	// Living zombies
	public List<EntityZombieBase> zombies = new ArrayList<EntityZombieBase>();
	public List<HordeEntry> entries = new ArrayList<HordeEntry>();

	public BlockPos center;
	public World world;
	public UUID uuid;

	public Horde(World world) {
		this.world = world;
	}

	public Horde(BlockPos center, World world) {
		uuid = UUID.randomUUID();
		this.center = center;
		this.world = world;
		HordeSavedData.get(world).addHorde(this);
	}

	public void onZombieKill(EntityZombieBase zombie) {
		zombies.remove(zombie);
		// HordeSavedData.get(world).addHorde(this);
	}

	public void start() {

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
		
	}

	public abstract void readFromNBT(NBTTagCompound compound);

	public abstract NBTTagCompound writeToNBT(NBTTagCompound compound);

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
}
