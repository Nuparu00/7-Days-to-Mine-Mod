package com.nuparu.sevendaystomine.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	private static ResourceLocation drunkShaderRes;

	@SideOnly(Side.CLIENT)
	private static Method f_loadShader;

	private static Method f_setSize;

	@SideOnly(Side.CLIENT)
	private static Minecraft mc;

	private long nextTorchCheck = 0l;

	@SuppressWarnings("deprecation")
	public static void init(Side side) {
		if (side == Side.CLIENT) {
			f_loadShader = ReflectionHelper.findMethod(EntityRenderer.class, "loadShader", "func_175069_a",
					ResourceLocation.class);
			mc = Minecraft.getMinecraft();

			windCounter = 10;
			beat = 999999;

			bleedShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/blur_bleed.json");
			nightShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/night.json");
			drunkShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/drunk.json");
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
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (player == null)
			return;

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
			if (player != null) {
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

	}
}
