package com.nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.proxy.ClientProxy;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGun {
	public static final ResourceLocation CROSSHAIR_TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/guncrosshair.png");

	public static final Minecraft mc = Minecraft.getMinecraft();

	private double posPrev = 0;
	private double pos = 0;

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@SideOnly(Side.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {
			posPrev = pos;
			ScaledResolution res = event.getResolution();

			int posX = (res.getScaledWidth()) / 2;
			int posY = (res.getScaledHeight()) / 2;

			EntityPlayer player = mc.player;

			ItemStack main = player.getHeldItemMainhand();
			ItemStack sec = player.getHeldItemOffhand();
			if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
				pos = 0;
				return;
			}
			Item item_main = main.getItem();
			Item item_sec = sec.getItem();
			if (item_main == null && item_sec == null) {
				pos = 0;
				return;
			}

			ItemGun gun_main = null;
			ItemGun gun_sec = null;

			if (item_main instanceof ItemGun) {
				gun_main = (ItemGun) item_main;
			}

			if (item_sec instanceof ItemGun) {
				gun_sec = (ItemGun) item_sec;
			}

			if (gun_main == null && gun_sec == null) {
				return;
			}

			NBTTagCompound nbt_main = main.getTagCompound();
			NBTTagCompound nbt_sec = sec.getTagCompound();

			if (nbt_main == null && nbt_sec == null) {
				pos = 0;
				return;
			}
			if ((nbt_main == null || !nbt_main.hasKey("Ammo") || !nbt_main.hasKey("Capacity"))
					&& (nbt_sec == null || !nbt_sec.hasKey("Ammo") || !nbt_sec.hasKey("Capacity"))) {
				pos = 0;
				return;
			}
			int ammo_main = -1;
			int capacity_main = -1;

			int ammo_sec = -1;
			int capacity_sec = -1;

			if (gun_main != null && nbt_main != null) {
				ammo_main = nbt_main.getInteger("Ammo");
				capacity_main = nbt_main.getInteger("Capacity");
			}

			if (gun_sec != null && nbt_sec != null) {
				ammo_sec = nbt_sec.getInteger("Ammo");
				capacity_sec = nbt_sec.getInteger("Capacity");
			}
			if (ammo_main != -1) {
				int color_main = ammo_main <= 0 ? 0xff0000 : 0xffffff;
				String text = new StringBuilder(SevenDaysToMine.proxy.localize("stat.ammo.name")).append(ammo_main)
						.append("/").append(capacity_main).toString();
				mc.fontRenderer.drawString(text,
						event.getResolution().getScaledWidth() - mc.fontRenderer.getStringWidth(text), 0, color_main);
			}

			if (ammo_sec != -1) {
				int color_sec = ammo_sec <= 0 ? 0xff0000 : 0xffffff;
				String text = new StringBuilder(SevenDaysToMine.proxy.localize("stat.ammo.name")).append(ammo_sec)
						.append("/").append(capacity_sec).toString();
				mc.fontRenderer.drawString(text, 0, 0, color_sec);
			}

			if ((main.isEmpty() || gun_main.getFOVFactor(main) != 1)
					&& (main.isEmpty() || gun_main.getFOVFactor(main) != 1)
					&& mc.gameSettings.keyBindAttack.isKeyDown())
				return;

			double gunCross = Utils.getCrosshairSpread(player);
			float vel = (float) (Math.abs(player.motionX) + Math.abs(player.motionZ)) * 0.5f;

			pos = gunCross * (float) (0.75 + 3.14 * vel);
			if (player.isSneaking()) {
				pos *= 0.75f;
			}

			mc.renderEngine.bindTexture(CROSSHAIR_TEX);

			/*
			 * if (player.fallDistance > 0) { pos *= 1.1f; }
			 */

			/*
			 * if (player.rotationYaw != player.prevRotationYaw || player.rotationPitch !=
			 * player.prevRotationPitch) { pos += pos *
			 * ((Utils.angularDistance(player.rotationYaw, player.prevRotationYaw) +
			 * Utils.angularDistance(player.rotationPitch, player.prevRotationPitch) * 10));
			 * }
			 */

			double alpha = 1f - (vel) * 3.3f;

			double finalPos = MathUtils.lerp(posPrev, pos, event.getPartialTicks());

			GlStateManager.pushMatrix();
			GL11.glColor4d(1d, 1d, 1d, alpha);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
					GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.enableAlpha();
			// LEFT
			mc.ingameGUI.drawTexturedModalRect((int) (posX - 13 / 2 - (finalPos)), posY - 2, 0, 14, 13, 4);
			// RIGHT
			mc.ingameGUI.drawTexturedModalRect((int) (posX - 13 / 2 + (finalPos)), posY - 2, 19, 14, 13, 4);
			// TOP
			mc.ingameGUI.drawTexturedModalRect(posX - 2, (int) (posY - 13 / 2 - (finalPos)), 14, 0, 4, 13);
			// DOVVN
			mc.ingameGUI.drawTexturedModalRect(posX - 2, (int) (posY - 13 / 2 + (finalPos)), 14, 19, 4, 13);
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

}
