package com.nuparu.sevendaystomine.events;

import java.util.Calendar;
import java.util.Date;
import com.nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import com.nuparu.sevendaystomine.client.gui.GuiRedirect;
import com.nuparu.sevendaystomine.client.gui.GuiUpdate;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.proxy.CommonProxy;
import com.nuparu.sevendaystomine.util.ConfigHandler;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.VersionChecker;
import com.nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
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
				if(Utils.getCrosshairSpread(mc.player) != -1) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onChangeGUI(GuiOpenEvent e) {
		if (e.getGui() instanceof GuiMainMenu && !(e.getGui() instanceof GuiMainMenuEnhanced)) {

			//CommonProxy.getVersionChecker();
			if (CommonProxy.getVersionChecker().shouldOpen() && !VersionChecker.didOpen) {
				//CommonProxy.getVersionChecker();
				VersionChecker.didOpen = true;
				if (ConfigHandler.menu == true) {
					e.setGui(new GuiRedirect(new GuiUpdate(new GuiMainMenuEnhanced(), -1)));
					return;
				}
				e.setGui(new GuiRedirect(new GuiUpdate(new GuiMainMenu(), -1)));

			} else {
				if (ConfigHandler.menu == true) {
					e.setGui(new GuiMainMenuEnhanced());
				}
			}
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
}
