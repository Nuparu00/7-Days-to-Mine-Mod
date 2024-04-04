package nuparu.sevendaystomine.world.horde;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityZombieBase;
import nuparu.sevendaystomine.util.Utils;

public class BloodmoonHorde extends Horde {

	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(
			new TextComponentTranslation("horde.bloodmoon.name"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS))
					.setDarkenSky(true);



	public BloodmoonHorde(World world) {
		super(world);
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "reanimated_corpse"), 20));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "bloated_zombie"), 12));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "infected_survivor"), 16));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "spider_zombie"), 8));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "plagued_nurse"), 12));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_crawler"), 10));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_soldier"), 6));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "burnt_zombie"), 3));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 8));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_miner"), 10));

		if (center != null) {
			Biome biome = world.getBiome(center);
			if (biome.isSnowyBiome()) {
				entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frigid_hunter"), 15));
				entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frostbitten_worker"), 15));
				entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frozen_lumberjack"), 7));
			}
		}
		if (ModConfig.world.bloodmoonFrequency > 0 && Utils.getDay(world) > 2 * ModConfig.world.bloodmoonFrequency) {
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "feral_zombie"),
					Math.min(5, (int) Math.floor(Utils.getDay(world) / ModConfig.world.bloodmoonFrequency) - 1)));
		}

		this.waves = ModConfig.world.bloodmoonHordeWaves;
		if (Utils.isCityInArea(world, (int) (center.getX() * 16), (int) (center.getZ() * 16), 5)) {
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_policeman"), 6));
		}
	}

	public BloodmoonHorde(BlockPos center, World world, EntityPlayer player) {
		super(center, world);
		this.playerID = player.getPersistentID();
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "reanimated_corpse"), 20));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "bloated_zombie"), 12));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "infected_survivor"), 16));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "spider_zombie"), 8));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "plagued_nurse"), 12));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_crawler"), 10));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_soldier"), 6));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "burnt_zombie"), 3));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 8));
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_miner"), 10));

		Biome biome = world.getBiome(center);
		if (biome.isSnowyBiome()) {
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frigid_hunter"), 15));
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frostbitten_worker"), 15));
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frozen_lumberjack"), 7));
		}
		if (ModConfig.world.bloodmoonFrequency > 0 && Utils.getDay(world) > 2 * ModConfig.world.bloodmoonFrequency) {
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "feral_zombie"),
					Math.min(5, (int) Math.floor(Utils.getDay(world) / ModConfig.world.bloodmoonFrequency) - 1)));
		}
		this.waves = ModConfig.world.bloodmoonHordeWaves;

		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>(server.getPlayerList().getPlayers());
		for (EntityPlayerMP playerMP : players) {
			if (playerMP.getGameProfile().equals(player.getGameProfile()))
				continue;
			if (playerMP.world.provider.getDimension() == world.provider.getDimension()) {
				if (playerMP.getDistanceSq(player) <= 1024) {
					this.waves = (int) Math.ceil(waves * 0.8);
					break;
				}
			}
		}

		if (Utils.isCityInArea(world, (int) (player.posX * 16), (int) (player.posZ * 16), 5)) {
			this.waves = (int) Math.ceil(waves * 1.2);
			entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_policeman"), 6));
		}
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
		bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
	}

	@Override
	public void addZombie(EntityZombieBase zombie) {
		super.addZombie(zombie);
		if (bossInfo == null)
			return;
		bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
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
		EntityPlayer player = getPlayer();
		if (player == null)
			return;
		zombies.clear();
		zombiesInWave = 0;
		this.center = getCenter();
		BlockPos origin = getSpawnOrigin();
		for (int i = 0; i < getZombiesInWave(); i++) {
			BlockPos pos = Utils.getTopGroundBlock(getSpawn(origin), world, true).up();
			HordeEntry entry = getHordeEntry(world.rand);
			if (entry != null) {
				EntityZombieBase zombie = entry.spawn(world, pos);
				if (zombie == null)
					continue;
				if (player != null) {
					zombie.setAttackTarget(player);
				}
				zombie.horde = this;
				zombies.add(zombie);
				zombiesInWave++;
			}
		}
		bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
		data.markDirty();
	}

	public int getZombiesInWave() {
		if (ModConfig.world.bloodmoonFrequency <= 0)
			return ModConfig.world.bloodmoonHordeZombiesPerWaveMax;
		return (int) MathHelper.clampedLerp(ModConfig.world.bloodmoonHordeZombiesPerWaveMin,
				ModConfig.world.bloodmoonHordeZombiesPerWaveMax,
				((int) (Math.floor(Utils.getDay(world) / ModConfig.world.bloodmoonFrequency)) - 1) / 5);
	}

	public BlockPos getSpawnOrigin() {
		double angle = 2.0 * Math.PI * world.rand.nextDouble();
		double dist = ModConfig.world.hordeMinDistance
				+ world.rand.nextDouble() * (ModConfig.world.hordeMaxDistance - ModConfig.world.hordeMinDistance);
		double x = center.getX() + dist * Math.cos(angle);
		double z = center.getZ() + dist * Math.sin(angle);

		return new BlockPos(x, 0, z);
	}

	public BlockPos getSpawn(BlockPos origin) {
		double x = world.rand.nextDouble() - 0.5;
		double y = world.rand.nextDouble() - 0.5;
		double z = world.rand.nextDouble() - 0.5;
		double d = world.rand.nextDouble() * 1.5;
		return origin.add(x * d, y * d, z * d);
	}

	public BlockPos getCenter() {
		EntityPlayer player = getPlayer();
		return player != null ? new BlockPos(player) : this.center;
	}

	public EntityPlayer getPlayer() {
		return this.playerID != null ? Utils.getPlayerFromUUID(playerID) : null;
	}

}
