package nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.potions.Potions;

@SideOnly(Side.CLIENT)
public class GuiPlayerUI {

	public static final ResourceLocation UI_TEX = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/hud.png");
	public static final ResourceLocation SCOPE_TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/misc/scope.png");

	float minibikeFuelPrev = 0;

	public GuiPlayerUI() {

	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@SideOnly(Side.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {

			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.player;

			if (player == null)
				return;

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.renderEngine.bindTexture(UI_TEX);

			EnumHudPosition pos = ModConfig.client.hudPosition;

			ScaledResolution resolution = event
					.getResolution();/*
										 * int i = resolution.getScaledWidth() / 2; int i1 = i - 110 + 2; int j1 =
										 * resolution.getScaledHeight() - 8 - 3;
										 * 
										 * int posX = i1; int posY = j1;
										 * 
										 * if (!player.isCreative() && !player.isSpectator() &&
										 * mc.playerController.shouldDrawHUD() && ModConfig.players.thirstSystem) {
										 * 
										 * IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
										 * 
										 * mc.ingameGUI.drawTexturedModalRect(posX - 100, posY - 9, 0,
										 * player.isPotionActive(Potions.dysentery) ? 29 : 8, (int) Math
										 * .floor(extendedPlayer.getThirst() / (10f * (extendedPlayer.getMaximumThirst()
										 * / 780f))), 6); mc.ingameGUI.drawTexturedModalRect(posX - 101, posY - 10, 0,
										 * 0, 81, 8); mc.ingameGUI.drawTexturedModalRect(posX - 100, posY - 0, 0, 15,
										 * (int) Math .floor(extendedPlayer.getStamina() / (10f *
										 * (extendedPlayer.getMaximumStamina() / 780f))), 6);
										 * mc.ingameGUI.drawTexturedModalRect(posX - 101, posY - 1, 0, 0, 81, 8);
										 * 
										 * } if (player.getRidingEntity() instanceof EntityMinibike) { EntityMinibike
										 * minibike = (EntityMinibike) player.getRidingEntity(); float fuel =
										 * minibike.getFuel(); mc.ingameGUI.drawTexturedModalRect(posX - 100, posY - 18,
										 * 0, 22, (int) Math.floor((fuel == 0 ? minibikeFuelPrev : fuel) / (10f *
										 * (EntityMinibike.MAX_FUEL / 780f))), 6);
										 * mc.ingameGUI.drawTexturedModalRect(posX - 101, posY - 19, 0, 0, 81, 8);
										 * minibikeFuelPrev = minibike.getFuel(); }
										 */

			double width = resolution.getScaledWidth();
			double height = resolution.getScaledHeight();

			boolean showFuel = player.getRidingEntity() instanceof EntityMinibike;

			int x = (int) (width * pos.getX()) + pos.getXOffset();
			int y = (int) (height * pos.getY()) + pos.getYOffset() + (pos.isTop() && showFuel ? 10 : 0);

			if (!player.isCreative() && !player.isSpectator() && mc.playerController.shouldDrawHUD()) {
				IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
				if (ModConfig.players.thirstSystem) {
					mc.ingameGUI.drawTexturedModalRect(x + 1, y - 9, 0,
							player.isPotionActive(Potions.dysentery) ? 29 : 8,
							(int) Math.floor(
									extendedPlayer.getThirst() / (10f * (extendedPlayer.getMaximumThirst() / 780f))),
							6);
					mc.ingameGUI.drawTexturedModalRect(x, y - 10, 0, 0, 81, 8);
				}
				if (ModConfig.players.staminaSystem) {
					mc.ingameGUI.drawTexturedModalRect(x + 1, y - 0, 0, 15,
							(int) Math.floor(
									extendedPlayer.getStamina() / (10f * (extendedPlayer.getMaximumStamina() / 780f))),
							6);
					mc.ingameGUI.drawTexturedModalRect(x, y - 1, 0, 0, 81, 8);
				}
			}

			if (showFuel) {
				EntityMinibike minibike = (EntityMinibike) player.getRidingEntity();
				float fuel = minibike.getFuel();
				mc.ingameGUI.drawTexturedModalRect(x + 1, y - 18, 0, 22, (int) Math
						.floor((fuel == 0 ? minibikeFuelPrev : fuel) / (10f * (EntityMinibike.MAX_FUEL / 780f))), 6);
				mc.ingameGUI.drawTexturedModalRect(x, y - 19, 0, 0, 81, 8);
				minibikeFuelPrev = minibike.getFuel();
			}

			ItemStack stack = player.getHeldItemMainhand();

			if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
				stack = player.getHeldItemOffhand();
				if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
					GL11.glPopMatrix();
					return;
				}
			}
			ItemGun gun = (ItemGun) stack.getItem();
			float factor = gun.getFOVFactor(stack);
			if (factor == 1) {
				GL11.glPopMatrix();
				return;
			}
			if (mc.gameSettings.keyBindAttack.isKeyDown() && gun.getScoped()) {
				int w = resolution.getScaledWidth();
				int h = resolution.getScaledHeight();

				GlStateManager.disableDepth();
				GlStateManager.depthMask(false);
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableAlpha();
				mc.renderEngine.bindTexture(SCOPE_TEX);
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

				bufferbuilder.pos(w / 2 - 2 * h, h, -90D).tex(0.0D, 1.0D).endVertex();
				bufferbuilder.pos(w / 2 + 2 * h, h, -90D).tex(1.0D, 1.0D).endVertex();
				bufferbuilder.pos(w / 2 + 2 * h, 0.0D, -90D).tex(1.0D, 0.0D).endVertex();
				bufferbuilder.pos(w / 2 - 2 * h, 0.0D, -90D).tex(0.0D, 0.0D).endVertex();
				tessellator.draw();
				GlStateManager.depthMask(true);
				GlStateManager.enableDepth();
				GlStateManager.enableAlpha();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}
			GL11.glPopMatrix();
		}
	}
}
