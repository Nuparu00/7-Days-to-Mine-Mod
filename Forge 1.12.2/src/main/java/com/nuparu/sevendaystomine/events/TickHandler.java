package com.nuparu.sevendaystomine.events;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.advancements.ModTriggers;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.ExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.inventory.InventoryPlayerExtended;
import com.nuparu.sevendaystomine.item.ItemNightVisionDevice;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.DamageSources;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.OpenSimplexNoise;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.client.RenderUtils;
import com.nuparu.sevendaystomine.world.MiscSavedData;
import com.nuparu.sevendaystomine.world.biome.BiomeWastelandBase;
import com.nuparu.sevendaystomine.world.horde.BloodmoonHorde;
import com.nuparu.sevendaystomine.world.horde.HordeSavedData;
import com.nuparu.sevendaystomine.world.horde.ZombieWolfHorde;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class TickHandler {
	public static float recoil = 0;
	public static float antiRecoil = 0;
	public static int time = 0;
	public static int useCount = 0;

	@SideOnly(Side.CLIENT)
	public static int windCounter;

	@SideOnly(Side.CLIENT)
	public static int beat;

	@SideOnly(Side.CLIENT)
	private static ResourceLocation bleedShaderRes;
	@SideOnly(Side.CLIENT)
	private static ResourceLocation nightShaderRes;
	@SideOnly(Side.CLIENT)
	public static ResourceLocation drunkShaderRes;

	@SideOnly(Side.CLIENT)
	public static Method f_loadShader;

	private static Method f_setSize;

	@SideOnly(Side.CLIENT)
	private static Minecraft mc;

	@SideOnly(Side.CLIENT)
	public static boolean bloodmoon;
	@SideOnly(Side.CLIENT)
	public static Field f_MOON_PHASES_TEXTURES;
	@SideOnly(Side.CLIENT)
	public static ResourceLocation bloodmoon_texture;
	@SideOnly(Side.CLIENT)
	public static ResourceLocation default_moon_texture;

	private long nextTorchCheck = 0l;

	@SideOnly(Side.CLIENT)
	public static Framebuffer fbo;

	@SuppressWarnings("deprecation")
	public static void init(Side side) {
		if (side == Side.CLIENT) {
			bloodmoon_texture = new ResourceLocation(SevenDaysToMine.MODID, "textures/environment/moon_phases.png");
			default_moon_texture = new ResourceLocation("textures/environment/moon_phases.png");

			f_loadShader = ReflectionHelper.findMethod(EntityRenderer.class, "loadShader", "func_175069_a",
					ResourceLocation.class);
			mc = Minecraft.getMinecraft();

			windCounter = 10;
			beat = 999999;

			bleedShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/blur_bleed.json");
			nightShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/night.json");
			drunkShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/drunk.json");

			fbo = new Framebuffer(500, 200, true);

		}
		f_setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	}

	public TickHandler() {

	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		EntityPlayer player = mc.player;
		if (player != null) {

			EntityRenderer entityRenderer = mc.entityRenderer;

			if (!player.isSpectator()) {
				if (player.isPotionActive(Potions.bleeding)) {
					if (entityRenderer.getShaderGroup() == null || !entityRenderer.getShaderGroup().getShaderGroupName()
							.equals(bleedShaderRes.toString())) {
						entityRenderer.loadShader(bleedShaderRes);
						++beat;
						if (entityRenderer.getShaderGroup() != null && entityRenderer.getShaderGroup()
								.getShaderGroupName().equals(bleedShaderRes.toString())) {
							List<Shader> listShaders = ReflectionHelper.getPrivateValue(ShaderGroup.class,
									entityRenderer.getShaderGroup(), "listShaders", "field_148031_d");
							((net.minecraft.client.shader.Shader) listShaders.get(0)).getShaderManager()
									.getShaderUniformOrDefault("Health").set(player.getHealth());
							((net.minecraft.client.shader.Shader) listShaders.get(0)).getShaderManager()
									.getShaderUniformOrDefault("MaxHealth").set(player.getMaxHealth());
						}
						if (player.getHealth() >= 1f && beat >= player.getHealth() * 10) {
							player.playSound(SoundHelper.HEARTBEAT, 1.0F, mc.world.rand.nextFloat() * 0.1F + 0.9F);
							beat = 0;
						}
					}
				} else {
					if (entityRenderer.getShaderGroup() != null
							&& entityRenderer.getShaderGroup().getShaderGroupName().equals(bleedShaderRes.toString())) {
						entityRenderer.stopUseShader();
					}
				}
				if (player.isPotionActive(Potions.drunk) || player.isPotionActive(Potions.alcoholPoison)) {
					if (entityRenderer.getShaderGroup() == null || !entityRenderer.getShaderGroup().getShaderGroupName()
							.equals(drunkShaderRes.toString())) {
						entityRenderer.loadShader(drunkShaderRes);

					}
				} else {
					if (entityRenderer.getShaderGroup() != null
							&& entityRenderer.getShaderGroup().getShaderGroupName().equals(drunkShaderRes.toString())) {
						entityRenderer.stopUseShader();
					}
				}
				if (!player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && player
						.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemNightVisionDevice) {
					if (entityRenderer.getShaderGroup() == null || !entityRenderer.getShaderGroup().getShaderGroupName()
							.equals(nightShaderRes.toString())) {
						entityRenderer.loadShader(nightShaderRes);

					}
				} else {
					if (entityRenderer.getShaderGroup() != null
							&& entityRenderer.getShaderGroup().getShaderGroupName().equals(nightShaderRes.toString())) {
						entityRenderer.stopUseShader();
					}
				}

			} else {
				if (entityRenderer.getShaderGroup() != null
						&& entityRenderer.getShaderGroup().getShaderGroupName().equals(bleedShaderRes.toString())) {
					entityRenderer.stopUseShader();
				}
				if (entityRenderer.getShaderGroup() != null
						&& entityRenderer.getShaderGroup().getShaderGroupName().equals(drunkShaderRes.toString())) {
					entityRenderer.stopUseShader();
				}
				if (entityRenderer.getShaderGroup() != null
						&& entityRenderer.getShaderGroup().getShaderGroupName().equals(nightShaderRes.toString())) {
					entityRenderer.stopUseShader();
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;
		if (world == null)
			return;
		HordeSavedData.get(world).update(world);
		BreakSavedData.get(world).update(world);

		if (world.provider.getDimension() != 0 || world.isRemote || ModConfig.world.airdropFrequency <= 0
				|| event.phase != TickEvent.Phase.START)
			return;
		MinecraftServer server = world.getMinecraftServer();
		if (server == null || server.getPlayerList().getCurrentPlayerCount() == 0)
			return;

		long time = world.getWorldTime() % 24000;
		MiscSavedData miscData = MiscSavedData.get(world);

		if (time >= 6000 && miscData.getLastAirdrop() != Utils.getDay(world)
				&& Utils.getDay(world) % ModConfig.world.airdropFrequency == 0) {
			miscData.setLastAirdrop(Utils.getDay(world));
			BlockPos pos = Utils.getAirdropPos(world);

			EntityAirdrop e = new EntityAirdrop(world, world.getSpawnPoint().up(255));
			world.spawnEntity(e);
			e.setPosition(pos.getX(), pos.getY(), pos.getZ());
			server.getPlayerList().sendMessage(new TextComponentTranslation("airdrop.message",
					pos.getX() + MathUtils.getIntInRange(world.rand, 32, 128) * (world.rand.nextBoolean() ? 1 : -1),
					pos.getZ() + MathUtils.getIntInRange(world.rand, 32, 128) * (world.rand.nextBoolean() ? 1 : -1)));
		}

	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) {
			EntityPlayer player = event.player;
			if (player == null || player.isDead)
				return;

			World world = player.world;

			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
			if (player instanceof EntityPlayerMP && !player.isCreative() && !player.isSpectator()) {
				if (world.getGameRules().getBoolean("handleThirst")) {
					handleExtendedPlayer(player, world, extendedPlayer);
				}
				IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				long time = world.getWorldTime() % 24000;
				if (Utils.isBloodmoon(world) && !world.isRemote && world.getDifficulty() != EnumDifficulty.PEACEFUL
						&& time > 13000 && time < 23000) {

					if (!iep.hasHorde()) {
						BlockPos pos = new BlockPos(playerMP);
						BloodmoonHorde horde = new BloodmoonHorde(pos, world, playerMP);
						horde.addTarget(playerMP);
						horde.start();
						iep.setHorde(true);
						world.playSound(null, pos, SoundHelper.HORDE, SoundCategory.HOSTILE,
								world.rand.nextFloat() * 0.1f + 0.95f, world.rand.nextFloat() * 0.1f + 0.95f);
					}

				} else if (Utils.isWolfHorde(world) && !world.isRemote
						&& world.getDifficulty() != EnumDifficulty.PEACEFUL && time > 1000 && time < 1060) {

					if (!iep.hasHorde()) {
						ZombieWolfHorde horde = new ZombieWolfHorde(new BlockPos(player), world, player);
						horde.addTarget(playerMP);
						horde.start();
						iep.setHorde(true);

					}
				} else if (iep.hasHorde()) {
					iep.setHorde(false);
				}

				if (Utils.isBloodmoon(Utils.getDay(world) - 1) && time < 1000) {
					ModTriggers.BLOODMOON_SURVIVAL.trigger(playerMP);
				}
			}
			if (extendedPlayer.isInfected()) {
				int time = extendedPlayer.getInfectionTime();
				extendedPlayer.setInfectionTime(time + 1);
				PotionEffect effect = player.getActivePotionEffect(Potions.infection);

				if (time < ExtendedPlayer.INFECTION_STAGE_TWO_START && (effect == null || effect.getAmplifier() != 0)) {
					player.addPotionEffect(new PotionEffect(Potions.infection, 24000));
				}
				if (time >= ExtendedPlayer.INFECTION_STAGE_TWO_START
						&& time < ExtendedPlayer.INFECTION_STAGE_THREE_START
						&& (effect == null || effect.getAmplifier() != 1)) {
					player.addPotionEffect(new PotionEffect(Potions.infection, 24000, 1));
				}
				if (time >= ExtendedPlayer.INFECTION_STAGE_THREE_START
						&& time < ExtendedPlayer.INFECTION_STAGE_FOUR_START
						&& (effect == null || effect.getAmplifier() != 2)) {
					player.addPotionEffect(new PotionEffect(Potions.infection, 24000, 2));
				}
				if (time >= ExtendedPlayer.INFECTION_STAGE_FOUR_START
						&& (player.getActivePotionEffect(Potions.infection) == null)) {
					player.attackEntityFrom(DamageSources.infection, 1);

				}
			}

			// Changes vanilla torches to enhanced torches every x seconds
			if (System.currentTimeMillis() > nextTorchCheck) {
				for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
					ItemStack s = player.inventory.mainInventory.get(i);
					if (s != null && s.getItem() == Item.getItemFromBlock(net.minecraft.init.Blocks.TORCH)) {
						player.inventory.mainInventory.set(i, new ItemStack(ModBlocks.TORCH_LIT, s.getCount()));
					}
				}
				for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
					ItemStack s = player.inventory.armorInventory.get(i);
					if (s != null && s.getItem() == Item.getItemFromBlock(net.minecraft.init.Blocks.TORCH)) {
						player.inventory.armorInventory.set(i, new ItemStack(ModBlocks.TORCH_LIT, s.getCount()));
					}
				}
				nextTorchCheck = System.currentTimeMillis() + 10000l;
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent event) {

		if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) {
			EntityPlayer player = mc.player;

			if (player == null)
				return;
			World world = player.world;

			if (recoil > 0) {
				recoil *= 0.8F;

				player.rotationPitch -= recoil / 2;
				if (useCount < 25) {
					antiRecoil += recoil / 2;
					player.rotationPitch += antiRecoil * 0.1F;
				}
				antiRecoil *= 0.8F;
			}

			IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
			if (iep.getStamina() <= 0) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
				player.setSprinting(false);
			}
			// System.out.println("C " + iep.getStamina());

			for (int l = 0; l < 1000; ++l) {
				int i1 = MathHelper.floor(player.posX) + world.rand.nextInt(16) - world.rand.nextInt(16);
				int j1 = MathHelper.floor(player.posY) + world.rand.nextInt(16) - world.rand.nextInt(16);
				int k1 = MathHelper.floor(player.posZ) + world.rand.nextInt(16) - world.rand.nextInt(16);
				BlockPos pos = new BlockPos(i1, j1, k1);
				Biome biome = world.getBiome(pos);

				if ((biome instanceof BiomeWastelandBase) && ((BiomeWastelandBase)biome).floatingParticles()) {
					if (world.rand.nextInt(8) > Math.abs(world.getHeight(pos).getY() - j1)) {
						IBlockState block = world.getBlockState(pos);

						if (block.getMaterial() == Material.AIR) {

							world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH,
									(double) ((float) i1 + world.rand.nextFloat()),
									(double) ((float) j1 + world.rand.nextFloat()),
									(double) ((float) k1 + world.rand.nextFloat()), 0.0D, 0.0D, 0.0D);
						}
					}
				}
			}

		}

	}

	public static void handleExtendedPlayer(EntityPlayer player, World world, IExtendedPlayer extendedPlayer) {
		if (world.isRemote) {
			return;
		}

		if (world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			extendedPlayer.setThirst(1000);
			extendedPlayer.setStamina(1000);
			return;
		}
		if (extendedPlayer.getThirst() > 0) {
			if (world.rand.nextInt(25) == 0) {
				extendedPlayer.consumeThirst((int) 1);
			}
		}

		if (player.isSprinting()) {
			if (extendedPlayer.getStamina() > 0) {
				if (world.rand.nextInt(3) == 0) {
					extendedPlayer.consumeStamina(2);
				}

				if (world.rand.nextInt(35) == 0) {
					extendedPlayer.consumeThirst((int) 1);
				}
			}

		} else if (extendedPlayer.getThirst() >= 100 && world.rand.nextInt(8) == 0
				&& player.distanceWalkedModified - player.prevDistanceWalkedModified <= 0.05) {
			extendedPlayer.addStamina(1);
		}

		if (extendedPlayer.getStamina() <= 0) {
			player.setSprinting(false);
		}

		if (extendedPlayer.getThirst() <= 0) {
			PotionEffect effect = new PotionEffect(Potions.thirst, 4, 4, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			player.addPotionEffect(effect);
		}

	}

}
