package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.ContainerForge;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

@SideOnly(Side.CLIENT)
public class GuiForge extends GuiContainer {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/forge.png");
	ContainerForge container;

	public GuiForge(ContainerForge container) {
		super(container);
		this.container = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = container.forge.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, 8, 6, 4210752);
		this.fontRenderer.drawString(
				new TextComponentTranslation("container.inventory", new Object[0]).getUnformattedText(), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		if (TileEntityForge.isBurning(this.container.forge)) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(marginHorizontal + 89, marginVertical + 59 - k, 176, 12 - k, 14, k + 1);
		}

		// Draw progress indicator
		int progressLevel = getProgressLevel(24);
		drawTexturedModalRect(marginHorizontal + 119, marginVertical + 43, 176, 14, progressLevel + 1, 16);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	private int getProgressLevel(int progressIndicatorPixelWidth) {
		int ticksGrindingItemSoFar = container.forge.getField(2);
		int ticksPerItem = container.forge.getField(3);
		return ticksPerItem != 0 && ticksGrindingItemSoFar != 0
				? ticksGrindingItemSoFar * progressIndicatorPixelWidth / ticksPerItem
				: 0;
	}

	private int getBurnLeftScaled(int pixels) {
		int i = this.container.forge.getField(1);
		if (i == 0) {
			i = 200;
		}

		return this.container.forge.getField(0) * pixels / i;
	}

}
