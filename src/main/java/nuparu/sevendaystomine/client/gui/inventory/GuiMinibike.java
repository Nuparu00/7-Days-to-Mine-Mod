package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.item.EnumQuality;

public class GuiMinibike extends GuiContainer {
	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/minibike.png");
	protected InventoryPlayer playerInventory;
	protected EntityMinibike minibike;

	boolean chestPrev = false;

	public GuiMinibike(InventoryPlayer playerInventory, EntityMinibike minibike, Container container) {
		super(container);
		this.playerInventory = playerInventory;
		this.minibike = minibike;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = minibike.getDisplayName().getUnformattedText();
		if (ModConfig.players.qualitySystem) {
			int calculated = minibike.getCalculatedQuality();
			if (calculated > 0) {
				EnumQuality quality = EnumQuality.getFromQuality(calculated);
				s = quality.getColor()
						+ SevenDaysToMine.proxy.localize("stat.quality." + quality.name().toLowerCase() + ".name") + " "
						+ s;
			} else {
				s = SevenDaysToMine.proxy.localize("stat.unfinished.name") + " " + s;
			}
		}
		this.fontRenderer.drawString(s, 87 - (this.fontRenderer.getStringWidth(s) / 2), 6, 4210752);
		this.fontRenderer.drawString(
				new TextComponentTranslation("container.inventory", new Object[0]).getUnformattedText(), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, 176, ySize);

		boolean chest = this.minibike.getChest(); 
		if (chest) {
			this.drawTexturedModalRect(marginHorizontal + 175, marginVertical + 6, 176, 0, 66, 67);
		}

		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		drawEntityOnScreen(i + ((this.xSize+(chest ? -66 : 0)) / 2), j + 66, 32, (float) (i + 51) - mouseX, (float) (j + 75 - 50) - mouseY,
				this.minibike);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public void updateScreen() {
		super.updateScreen();
		if (minibike.isDead) {
			Minecraft.getMinecraft().currentScreen = null;
		}
		boolean chest = this.minibike.getChest();
		if (chestPrev != chest) {
			if (chest) {
				xSize = 246;
			} else {
				xSize = 176;
			}
			this.initGui();
		}
		chestPrev = chest;
	}

	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, Entity ent) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX, (float) posY, 50.0F);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (mouseX / 40.0F))) * 20.0F, 0.0F, 1.0F, 0.0F);
		// ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
		// ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

}
