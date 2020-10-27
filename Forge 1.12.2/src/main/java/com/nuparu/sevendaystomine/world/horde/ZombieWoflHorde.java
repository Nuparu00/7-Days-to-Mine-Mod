package com.nuparu.sevendaystomine.world.horde;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityInfectedSurvivor;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

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

public class ZombieWoflHorde extends Horde {

	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(
			new TextComponentTranslation("horde.wolf.name"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS))
					.setDarkenSky(true);

	public int waves = 10;
	public int zombiesInWave = 0;
	public EntityPlayer player;

	public static final int MIN_DISTANCE = 20;

	public ZombieWoflHorde(World world) {
		super(world);
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 10));
	}

	public ZombieWoflHorde(BlockPos center, World world, EntityPlayer player) {
		super(center, world);
		this.player = player;
		this.waves = MathUtils.getIntInRange(8, 10);
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 10));
	}

	public void addTarget(EntityPlayerMP player) {
		bossInfo.addPlayer(player);
	}

	public void removeTarget(EntityPlayerMP player) {
		// bossInfo.removePlayer(player);
	}

	@Override
	public void onZombieKill(EntityZombieBase zombie) {
		super.onZombieKill(zombie);
		bossInfo.setPercent((float) zombies.size() / (float) zombiesInWave);

		if (zombies.size() <= 0) {
			if (waves > 0) {
				zombiesInWave = 0;
				waves--;
				start();
			} else {
				HordeSavedData.get(world).removeHorde(this);
			}
		}
	}

	@Override
	public void addZombie(EntityZombieBase zombie) {
		super.addZombie(zombie);
		bossInfo.setPercent((float) zombies.size() / (float) zombiesInWave);
	}

	@Override
	public void onPlayerStartTacking(EntityPlayerMP player, EntityZombieBase zombie) {
		addTarget(player);
	}

	@Override
	public void onPlayerStopTacking(EntityPlayerMP player, EntityZombieBase zombie) {
		removeTarget(player);
	}

	public void onRemove() {
		for (EntityPlayerMP playerMP : bossInfo.getPlayers()) {
			bossInfo.removePlayer(playerMP);
		}
	}

	@Override
	public void start() {
		BlockPos pos = Utils.getTopGroundBlock(getSpawn(), world, true);
		zombies.clear();
		for (int i = 0; i < MathUtils.getIntInRange(7, 10); i++) {
			HordeEntry entry = getHordeEntry(world.rand);
			if (entry != null) {
				EntityZombieBase zombie = entry.spawn(world, pos);
				zombie.setAttackTarget(player);
				zombie.horde = this;
				zombies.add(zombie);
				zombiesInWave++;
				System.out.println(new BlockPos(zombie));
			}
		}
		bossInfo.setPercent((float) zombies.size() / (float) zombiesInWave);
	}

	public BlockPos getSpawn() {
		double x = world.rand.nextDouble() - 0.5;
		double y = world.rand.nextDouble() - 0.5;
		double z = world.rand.nextDouble() - 0.5;

		double mag = Math.sqrt(x * x + y * y + z * z);
		x /= mag;
		y /= mag;
		z /= mag;

		double d = world.rand.nextInt(10) + MIN_DISTANCE;
		return center.add(x * d, y * d, z * d);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (world.isRemote) {
			return;
		}
		waves = compound.getInteger("wave");
		zombiesInWave = compound.getInteger("zombiesInWave");
		uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		center = BlockPos.fromLong(compound.getLong("center"));
		zombies.clear();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setString("class", this.getClass().toString());
		compound.setInteger("wave", waves);
		compound.setInteger("zombiesInWave", zombiesInWave);
		compound.setTag("uuid", NBTUtil.createUUIDTag(uuid));
		compound.setLong("center", center.toLong());
		return compound;
	}

}
