package com.nuparu.sevendaystomine.events;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import com.nuparu.sevendaystomine.client.gui.GuiRedirect;
import com.nuparu.sevendaystomine.client.gui.GuiUpdate;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.proxy.ClientProxy;
import com.nuparu.sevendaystomine.proxy.CommonProxy;
import com.nuparu.sevendaystomine.util.ConfigHandler;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.VanillaManager;
import com.nuparu.sevendaystomine.util.VanillaManager.VanillaScrapableItem;
import com.nuparu.sevendaystomine.util.VersionChecker;
import com.nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler {

	@SideOnly(Side.CLIENT)
	public static PositionedSoundRecord menuMusic_0;

	@SideOnly(Side.CLIENT)
	public static PositionedSoundRecord menuMusic_1;

	public static void init() {
		menuMusic_0 = new PositionedSoundRecord(SoundHelper.MENU_00.getSoundName(), SoundCategory.MUSIC, 1f, 1f, true,
				0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
		menuMusic_1 = new PositionedSoundRecord(SoundHelper.MENU_01.getSoundName(), SoundCategory.MUSIC, 1f, 1f, true,
				0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderScopeOverlayPre(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
			if (mc.gameSettings.thirdPersonView == 0) {
				if (Utils.getCrosshairSpread(mc.player) != -1) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiOpen(GuiOpenEvent e) {
		if (e.getGui() instanceof GuiMainMenu && !(e.getGui() instanceof GuiMainMenuEnhanced)) {

			/*
			 * if (CommonProxy.getVersionChecker().shouldOpen() && !VersionChecker.didOpen)
			 * { VersionChecker.didOpen = true; if (ConfigHandler.menu == true) {
			 * e.setGui(new GuiRedirect(new GuiUpdate(new GuiMainMenuEnhanced(), -1)));
			 * return; } e.setGui(new GuiRedirect(new GuiUpdate(new GuiMainMenu(), -1)));
			 * 
			 * } else { if (ConfigHandler.menu == true) {
			 */
			e.setGui(new GuiMainMenuEnhanced());
			// }
			// }
		}

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlaySoundEvent(PlaySoundEvent event) {
		if (event.getSound().getSoundLocation().getResourcePath().equals("music.menu")) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player == null) {
				if (ConfigHandler.menu == true) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					SoundManager sm = event.getManager();
					SoundHandler sh = sm.sndHandler;
					sh.stopSound(event.getSound());
					event.setResultSound(null);

					if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
						/*
						 * if (event.getSound() != menuMusic_1) { event.setResult(null); }
						 */
						if (sh.isSoundPlaying(menuMusic_0)) {
							sh.stopSound(menuMusic_0);
						}
						if (!sh.isSoundPlaying(menuMusic_1)) {
							event.setResultSound(menuMusic_1);
						}
					}
					/*
					 * if (event.getSound() != menuMusic_0) { event.setResult(null); }
					 */
					if (sh.isSoundPlaying(menuMusic_1)) {
						sh.stopSound(menuMusic_1);
					}
					if (!sh.isSoundPlaying(menuMusic_0)) {
						event.setResultSound(menuMusic_0);
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void onCLientConnect(ClientConnectedToServerEvent event) {
		SubtitleHelper.INSTANCE.clear();
		GuiIngameForge.renderArmor = true;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onFogColors(FogColors event) {
		Biome biome = event.getEntity().world.getBiome(new BlockPos(event.getEntity()));
		IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(event.getEntity().world,
				event.getEntity(), (float) event.getRenderPartialTicks());
		Material mat = iblockstate.getMaterial();

		if (Utils.isBloodmoon(event.getEntity().world)) {
			float mult = (float) MathUtils.clamp(
					Math.abs(event.getEntity().world.getCelestialAngle((float) event.getRenderPartialTicks()) - 0.54),
					0, 0.30);
			mult = (0.30f - mult) / 0.30f;

			event.setRed(MathUtils.clamp(event.getRed() + 0.3f * mult, 0, 1));
			event.setGreen(MathUtils.clamp(event.getGreen() + 0.05f * mult, 0, 1));
			event.setBlue(MathUtils.clamp(event.getBlue() + 0.02f * mult, 0, 1));
		}
		if ((biome == ModBiomes.BURNT_FOREST || biome == ModBiomes.WASTELAND) && mat != Material.WATER
				&& mat != Material.LAVA) {
			event.setRed(MathUtils.clamp(event.getRed() - 0.05f, 0, 1));
			event.setGreen(MathUtils.clamp(event.getGreen() - 0.13f, 0, 1));
			event.setBlue(MathUtils.clamp(event.getBlue() - 0.28f, 0, 1));
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(FogDensity event) {

		Biome biome = event.getEntity().world.getBiome(new BlockPos(event.getEntity()));
		IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(event.getEntity().world,
				event.getEntity(), (float) event.getRenderPartialTicks());
		Material mat = iblockstate.getMaterial();
		if ((biome == ModBiomes.BURNT_FOREST || biome == ModBiomes.WASTELAND) && mat != Material.WATER
				&& mat != Material.LAVA) {
			event.setDensity(event.getDensity() / 4f);
			GlStateManager.setFog(GlStateManager.FogMode.EXP);
			/*
			 * GlStateManager.setFogStart(1); GlStateManager.setFogEnd(10);
			 */
			event.setCanceled(true);
		} else {
			event.setCanceled(false);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void updateFOVEvent(FOVUpdateEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player == null)
			return;
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
			stack = player.getHeldItemOffhand();
			if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun))
				return;
		}
		ItemGun gun = (ItemGun) stack.getItem();
		float factor = gun.getFOVFactor(stack);
		if (factor == 1)
			return;
		if (mc.gameSettings.keyBindAttack.isKeyDown()) {
			event.setNewfov(event.getNewfov() / factor);
		}

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void itemToolTip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.isEmpty())
			return;
		Item item = stack.getItem();

		EnumMaterial mat = EnumMaterial.NONE;
		int weight = 0;
		if (item instanceof IScrapable) {
			IScrapable scrapable = (IScrapable) stack.getItem();
			mat = scrapable.getMaterial();
			weight = scrapable.getWeight();

		} else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof IScrapable) {
			IScrapable scrapable = (IScrapable) ((ItemBlock) item).getBlock();
			mat = scrapable.getMaterial();
			weight = scrapable.getWeight();
		} else if (VanillaManager.getVanillaScrapable(item) != null) {
			VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
			mat = scrapable.getMaterial();
			weight = scrapable.getWeight();
		}
		
		if(mat != null && mat != EnumMaterial.NONE) {
			event.getToolTip().add(weight+"x"+mat.getLocalizedName());
		}
	}

}
