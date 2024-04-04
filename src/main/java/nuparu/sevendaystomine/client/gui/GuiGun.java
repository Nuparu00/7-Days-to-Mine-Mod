package nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.IReloadable;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class GuiGun {

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

			IReloadable reloadableMain = null;
			IReloadable reloadableSec = null;

			if (item_main instanceof IReloadable) {
				reloadableMain = (IReloadable) item_main;
			}

			if (item_sec instanceof IReloadable) {
				reloadableSec = (IReloadable) item_sec;
			}

			if (reloadableMain == null && reloadableSec == null) {
				return;
			}

			if (reloadableMain != null) {
				int ammoMain = reloadableMain.getAmmo(main, player);
				int color_main = ammoMain <= 0 ? 0xff0000 : 0xffffff;
				boolean fuel = reloadableMain instanceof ItemFuelTool;
				String text = new StringBuilder(SevenDaysToMine.proxy.localize(fuel ? "stat.fuel.name" : "stat.ammo.name")).append(ammoMain)
						.append("/").append(reloadableMain.getCapacity(main, player)).toString();
				mc.fontRenderer.drawString(text,
						event.getResolution().getScaledWidth() - mc.fontRenderer.getStringWidth(text), 0, color_main);
			}

			if (reloadableSec != null) {
				int ammoSec = reloadableSec.getAmmo(sec, player);
				int color_sec = ammoSec <= 0 ? 0xff0000 : 0xffffff;
				boolean fuel = reloadableMain instanceof ItemFuelTool;
				String text = new StringBuilder(SevenDaysToMine.proxy.localize(fuel ? "stat.fuel.name" : "stat.ammo.name")).append(ammoSec)
						.append("/").append(reloadableSec.getCapacity(sec, player)).toString();
				mc.fontRenderer.drawString(text, 0, 0, color_sec);
			}

			ItemGun gunMain = null;
			if (reloadableMain instanceof ItemGun) {
				gunMain = (ItemGun) reloadableMain;
			}
			ItemGun gunSec = null;
			if (reloadableSec instanceof ItemGun) {
				gunSec = (ItemGun) reloadableSec;
			}

			if (gunMain != null || gunSec != null) {

				if(gunMain != null && (gunMain.getFOVFactor(main) != 1 && mc.gameSettings.keyBindAttack.isKeyDown())) {
					return;
				}
				
				if(gunSec != null && (gunSec.getFOVFactor(sec) != 1 && mc.gameSettings.keyBindAttack.isKeyDown())) {
					return;
				}

				double gunCross = Utils.getCrosshairSpread(player);
				float vel = (float) (Math.abs(player.motionX) + Math.abs(player.motionZ)) * 0.5f;

				pos = gunCross * (float) (0.75 + 3.14 * vel);
				if (player.isSneaking()) {
					pos *= 0.75f;
				}

				mc.renderEngine.bindTexture(GuiPlayerUI.UI_TEX);

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
				mc.ingameGUI.drawTexturedModalRect((int) (posX - 13 / 2 - (finalPos)), posY - 2, 0, 118, 13, 2);
				// RIGHT
				mc.ingameGUI.drawTexturedModalRect((int) (posX - 13 / 2 + (finalPos)), posY - 2, 19, 118, 13, 2);
				// TOP
				mc.ingameGUI.drawTexturedModalRect(posX - 2, (int) (posY - 13 / 2 - (finalPos)), 14, 104, 2, 13);
				// DOVVN
				mc.ingameGUI.drawTexturedModalRect(posX - 2, (int) (posY - 13 / 2 + (finalPos)), 14, 123, 2, 13);
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}
	}

}
