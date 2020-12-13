package com.nuparu.sevendaystomine.world.horde;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.entity.EntityInfectedSurvivor;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.horde.Horde.HordeEntry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ZombieWolfHorde extends Horde {

	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(
			new TextComponentTranslation("horde.wolf.name"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS))
					.setDarkenSky(true);

	public EntityPlayer player;

	public static final int MIN_DISTANCE = 30;

	public ZombieWolfHorde(World world) {
		super(world);
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 10));

		this.waves = ModConfig.world.wolfHordeWaves;
	}

	public ZombieWolfHorde(BlockPos center, World world, EntityPlayer player) {
		super(center, world);
		this.player = player;
		this.waves = ModConfig.world.wolfHordeWaves;
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 10));
	}

	public void addTarget(EntityPlayerMP player) {
		if (bossInfo == null)
			return;
		bossInfo.addPlayer(player);
	}

	public void removeTarget(EntityPlayerMP player) {
		if (bossInfo == null)
			return;
		bossInfo.removePlayer(player);
	}

	@Override
	public void onZombieKill(EntityZombieBase zombie) {
		super.onZombieKill(zombie);
		if (bossInfo == null)
			return;
		bossInfo.setPercent((float) zombies.size() / (float) zombiesInWave);
	}

	@Override
	public void addZombie(EntityZombieBase zombie) {
		super.addZombie(zombie);
		if (bossInfo == null)
			return;
		bossInfo.setPercent((float) zombies.size() / (float) zombiesInWave);
	}

	@Override
	public void onPlayerStartTacking(EntityPlayerMP player, EntityZombieBase zombie) {
		addTarget(player);
	}

	@Override
	public void onPlayerStopTacking(EntityPlayerMP player, EntityZombieBase zombie) {
		// removeTarget(player);
	}

	public void onRemove() {
		super.onRemove();
		if (bossInfo == null || bossInfo.getPlayers() == null)
			return;
		Collection<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>(bossInfo.getPlayers());
		for (EntityPlayerMP playerMP : players) {
			bossInfo.removePlayer(playerMP);
		}
	}

	@Override
	public void start() {
		super.start();
		if (player == null)
			return;
		zombies.clear();
		BlockPos origin = getSpawnOrigin();
		for (int i = 0; i < ModConfig.world.wolfHordeZombiesPerWave; i++) {
			BlockPos pos = Utils.getTopGroundBlock(getSpawn(origin).up(), world, true);
			HordeEntry entry = getHordeEntry(world.rand);
			if (entry != null) {
				EntityZombieBase zombie = entry.spawn(world, pos);
				if (zombie == null)
					continue;
				zombie.setAttackTarget(player);
				zombie.horde = this;
				zombies.add(zombie);
				zombiesInWave++;
			}
		}
		bossInfo.setPercent((float) zombies.size() / (float) zombiesInWave);
		data.markDirty();
	}

	public BlockPos getSpawnOrigin() {
		double angle = 2.0 * Math.PI * world.rand.nextDouble();
		double dist = MIN_DISTANCE + world.rand.nextDouble()*10;
		double x = center.getX() + dist * Math.cos(angle);
		double z = center.getZ() + dist * Math.sin(angle);
		
		return new BlockPos(x,0,z);
	}

	public BlockPos getSpawn(BlockPos origin) {
		double x = world.rand.nextDouble() - 0.5;
		double y = world.rand.nextDouble() - 0.5;
		double z = world.rand.nextDouble() - 0.5;

		double mag = Math.sqrt(x * x + y * y + z * z);
		x /= mag;
		y /= mag;
		z /= mag;

		double d = world.rand.nextDouble() * 1.5;
		return origin.add(x * d, y * d, z * d);
	}

}
