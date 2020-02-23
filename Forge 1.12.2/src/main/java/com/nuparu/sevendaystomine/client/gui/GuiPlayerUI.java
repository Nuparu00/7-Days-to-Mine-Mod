package com.nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerUI {
	
	public static final ResourceLocation UI_TEX = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/hud.png");
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	@SideOnly(Side.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {

			Minecraft mc = Minecraft.getMinecraft();
               EntityPlayer player = mc.player;
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
			if (!player.isCreative() && !player.isSpectator() && mc.playerController.shouldDrawHUD()) {
				ScaledResolution resolution = event.getResolution();
				//int posX = (resolution.getScaledWidth()) / 2 + 10;
				//int posY = (resolution.getScaledHeight()) - 48;
				
				 int i = resolution.getScaledWidth() / 2;
				 int i1 = i - 110 + 2;
				 int j1 = resolution.getScaledHeight() - 8 - 3;
				
				int posX = i1;
				int posY = j1;
				
				GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				mc.renderEngine.bindTexture(UI_TEX);
				mc.ingameGUI.drawTexturedModalRect(posX - 100, posY - 9, 0, 8, (int) Math.round(extendedPlayer.getThirst() / (10f*(extendedPlayer.getMaximumThirst()/780f))), 6);
				mc.ingameGUI.drawTexturedModalRect(posX - 101, posY - 10, 0, 0, 81, 8);
				mc.ingameGUI.drawTexturedModalRect(posX - 100, posY - 0, 0, 15, (int) Math.round(extendedPlayer.getStamina() / (10f*(extendedPlayer.getMaximumStamina()/780f))), 6);
				mc.ingameGUI.drawTexturedModalRect(posX - 101, posY - 1, 0, 0, 81, 8);
				GL11.glPopMatrix();
			}
		}
	}
}
