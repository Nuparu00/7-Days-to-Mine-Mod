package nuparu.sevendaystomine.world.horde;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import net.minecraftforge.fml.common.FMLCommonHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityZombieBase;
import nuparu.sevendaystomine.util.Utils;

public class ZombieWolfHorde extends Horde {

	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(
			new TextComponentTranslation("horde.wolf.name"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS))
					.setDarkenSky(true);

	public ZombieWolfHorde(World world) {
		super(world);
		entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 10));

		this.waves = ModConfig.world.wolfHordeWaves;
		EntityPlayer player = this.getPlayer();
		if (player != null) {
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
		}
	}

	public ZombieWolfHorde(BlockPos center, World world, EntityPlayer player) {
		super(center, world);
		this.playerID = player.getPersistentID();
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
		this.center = getCenter();
		BlockPos origin = getSpawnOrigin();
		for (int i = 0; i < ModConfig.world.wolfHordeZombiesPerWave; i++) {
			BlockPos pos = Utils.getTopGroundBlock(getSpawn(origin), world, true).up();
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
		bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
		data.markDirty();
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

}
