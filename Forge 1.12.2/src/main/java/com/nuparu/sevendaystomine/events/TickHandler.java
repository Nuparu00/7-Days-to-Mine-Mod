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
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.DamageSources;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.OpenSimplexNoise;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.client.RenderUtils;
import com.nuparu.sevendaystomine.world.MiscSavedData;
import com.nuparu.sevendaystomine.world.horde.BloodmoonHorde;
import com.nuparu.sevendaystomine.world.horde.ZombieWoflHorde;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
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

	/*
	 * OpenSimplexNoise noise = new OpenSimplexNoise();
	 * 
	 * int update = 1; int updatePrev = 0;
	 * 
	 * int WIDTH = 512; int HEIGHT = 512; double FEATURE_SIZE = 12;
	 * 
	 * BufferedImage image; DynamicTexture tex; ResourceLocation rl;
	 */

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		/*
		 * update = 7; FEATURE_SIZE = 32; if (update != updatePrev) { image = new
		 * BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		 * 
		 * for (int y = 0; y < HEIGHT; y++) { for (int x = 0; x < WIDTH; x++) { double
		 * q1 = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, 0.0); double q2 =
		 * noise.eval(x / FEATURE_SIZE + 1.3, y / FEATURE_SIZE + 0.7, 0.0);
		 * 
		 * double r1 = noise.eval(x / FEATURE_SIZE + 1 * q1 + 1.7, y / FEATURE_SIZE + 1
		 * * q2 + 9.2, 0.0); double r2 = noise.eval(x / FEATURE_SIZE + 1 * q1 + 8.3, y /
		 * FEATURE_SIZE + 1 * q2 + 2.8, 0.0);
		 * 
		 * double value = Math.abs(noise.eval(x / FEATURE_SIZE + 2 * q1, y /
		 * FEATURE_SIZE + 2 * q2, MathUtils.getDoubleInRange(0, 0))); if (value < 0.05)
		 * { value = -1; }
		 * 
		 * int rgb = 0x010101 * (int) ((value + 1) * 127.5); image.setRGB(x, y, rgb); }
		 * }
		 * 
		 * tex = new DynamicTexture(image); rl =
		 * mc.getTextureManager().getDynamicTextureLocation(SevenDaysToMine.MODID +
		 * ":noise", tex); updatePrev = update; } GL11.glPushMatrix();
		 * RenderUtils.drawTexturedRect(rl, 0, 0, 0, 0, 256, 256, 256, 256, 1, 1);
		 * GL11.glPopMatrix();
		 */

		EntityPlayer player = mc.player;
		if (player != null) {

			EntityRenderer entityRenderer = mc.entityRenderer;

			if (!player.isCreative() && !player.isSpectator()) {
				if (player.isPotionActive(Potions.bleeding)) {
					if (entityRenderer.getShaderGroup() == null || !entityRenderer.getShaderGroup().getShaderGroupName()
							.equals(bleedShaderRes.toString())) {
						try {
							f_loadShader.invoke(Minecraft.getMinecraft().entityRenderer, bleedShaderRes);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
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
					if (player.isPotionActive(Potions.drunk)) {
						if (entityRenderer.getShaderGroup() == null || !entityRenderer.getShaderGroup()
								.getShaderGroupName().equals(drunkShaderRes.toString())) {
							try {
								f_loadShader.invoke(entityRenderer, drunkShaderRes);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}

						}
					} else {
						if (entityRenderer.getShaderGroup() != null && entityRenderer.getShaderGroup()
								.getShaderGroupName().equals(drunkShaderRes.toString())) {
							entityRenderer.stopUseShader();
						}
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
			}

			/*
			 * if (f_MOON_PHASES_TEXTURES != null) { if (Utils.getDay(player.world) % 7 ==
			 * 0) {
			 * 
			 * try { if (!bloodmoon || ((ResourceLocation)
			 * (f_MOON_PHASES_TEXTURES.get(null))) .compareTo(bloodmoon_texture) != 0) {
			 * 
			 * System.out.println(((ResourceLocation)
			 * (f_MOON_PHASES_TEXTURES.get(null))).toString()); System.out.println("AAAA " +
			 * bloodmoon); TickHandler.f_MOON_PHASES_TEXTURES.setAccessible(true);
			 * FieldUtils.writeStaticField(f_MOON_PHASES_TEXTURES, bloodmoon_texture, true);
			 * FieldUtils.removeFinalModifier(TickHandler.f_MOON_PHASES_TEXTURES);
			 * TickHandler.f_MOON_PHASES_TEXTURES.set(null, bloodmoon_texture); bloodmoon =
			 * true; } } catch (IllegalArgumentException | IllegalAccessException |
			 * SecurityException e) { e.printStackTrace(); } } else {
			 * 
			 * try {
			 * 
			 * if (bloodmoon || ((ResourceLocation) (f_MOON_PHASES_TEXTURES.get(null)))
			 * .compareTo(default_moon_texture) != 0) {
			 * System.out.println(((ResourceLocation)
			 * (f_MOON_PHASES_TEXTURES.get(null))).toString()); System.out.println("BBBB " +
			 * bloodmoon); TickHandler.f_MOON_PHASES_TEXTURES.setAccessible(true);
			 * FieldUtils.removeFinalModifier(TickHandler.f_MOON_PHASES_TEXTURES);
			 * TickHandler.f_MOON_PHASES_TEXTURES.set(null, default_moon_texture); bloodmoon
			 * = false;
			 * 
			 * } } catch (IllegalArgumentException | IllegalAccessException |
			 * SecurityException e) { e.printStackTrace(); } } }
			 */

		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;
		if (world == null || world.isRemote || ModConfig.world.airdropFrequency <= 0)
			return;

		long time = world.getWorldTime() % 24000;
		MiscSavedData miscData = MiscSavedData.get(world);

		if (time >= 6000 && miscData.getLastAirdrop() != Utils.getDay(world) && Utils.getDay(world) % ModConfig.world.airdropFrequency == 0) {
			MinecraftServer server = Utils.getServer();
			if (server == null)
				return;
			BlockPos pos = Utils.getAirdropPos(world);
			server.getPlayerList().sendMessage(new TextComponentTranslation("airdrop.message",
					pos.getX() + MathUtils.getIntInRange(world.rand, 32, 128) * (world.rand.nextBoolean() ? 1 : -1),
					pos.getZ() + MathUtils.getIntInRange(world.rand, 32, 128) * (world.rand.nextBoolean() ? 1 : -1)));
			EntityAirdrop e = new EntityAirdrop(world, pos);
			world.spawnEntity(e);
			miscData.setLastAirdrop(Utils.getDay(world));
		}

	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (player == null)
			return;

		World world = player.world;

		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		if (!player.isCreative() && !player.isSpectator()) {
			if (world.getGameRules().getBoolean("handleThirst")) {
				handleExtendedPlayer(player, world, extendedPlayer);
			}
			IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
			long time = world.getWorldTime() % 24000;
			if (Utils.isBloodmoon(world)) {

				if (player instanceof EntityPlayerMP && !world.isRemote
						&& world.getDifficulty() != EnumDifficulty.PEACEFUL && time > 13000 && time < 23000) {

					if (!iep.hasHorde()) {
						BloodmoonHorde horde = new BloodmoonHorde(new BlockPos(player), world, player);
						horde.addTarget((EntityPlayerMP) player);
						horde.start();
						iep.setHorde(true);
					}
				}
			} else if (Utils.isWolfHorde(world)) {
				if (player instanceof EntityPlayerMP && !world.isRemote
						&& world.getDifficulty() != EnumDifficulty.PEACEFUL && time > 1000 && time < 1060) {

					if (!iep.hasHorde()) {
						ZombieWoflHorde horde = new ZombieWoflHorde(new BlockPos(player), world, player);
						horde.addTarget((EntityPlayerMP) player);
						horde.start();
						iep.setHorde(true);
					}
				}
			} else if (iep.hasHorde()) {
				iep.setHorde(false);
			}
		}
		if (extendedPlayer.isInfected()) {
			int time = extendedPlayer.getInfectionTime();
			extendedPlayer.setInfectionTime(time + 1);
			if (time >= 48000 && time < 96000 && (player.getActivePotionEffect(Potions.infection) == null)) {
				player.addPotionEffect(new PotionEffect(Potions.infection, 24000, 1));
			}
			if (time >= 96000 && time < 144000 && (player.getActivePotionEffect(Potions.infection) == null)) {
				player.addPotionEffect(new PotionEffect(Potions.infection, 24000, 2));
			}
			if (time >= 144000 && (player.getActivePotionEffect(Potions.infection) == null)) {
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

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent event) {

		if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) {
			EntityPlayer player = mc.player;
			if (player == null)
				return;

			if (recoil > 0) {
				recoil *= 0.8F;
			}
			player.rotationPitch -= recoil / 2;
			if (useCount < 25) {
				antiRecoil += recoil / 2;
				player.rotationPitch += antiRecoil * 0.1F;
			}
			antiRecoil *= 0.8F;

		}

	}

	public static void handleExtendedPlayer(EntityPlayer player, World world, IExtendedPlayer extendedPlayer) {
		if (world.isRemote)
			return;

		if (world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			extendedPlayer.setThirst(1000);
			extendedPlayer.setStamina(1000);
			return;
		}

		if (world.rand.nextInt(25) == 0) {
			extendedPlayer.consumeThirst((int) 1);
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

		}

		if (extendedPlayer.getStamina() > extendedPlayer.getMaximumStamina()) {
			extendedPlayer.setStamina(extendedPlayer.getMaximumStamina());
		}
		if (extendedPlayer.getStamina() < 0) {
			extendedPlayer.setStamina(0);
		}

		if (extendedPlayer.getThirst() <= 0) {

			extendedPlayer.setThirst(0);

			PotionEffect effect = new PotionEffect(Potions.thirst, 4, 4, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			player.addPotionEffect(effect);
		}
		if (extendedPlayer.getThirst() > extendedPlayer.getMaximumThirst()) {
			extendedPlayer.setThirst(extendedPlayer.getMaximumThirst());
		}
	}
}
