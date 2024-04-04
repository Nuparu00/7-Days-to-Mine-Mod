package nuparu.sevendaystomine.client.gui.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.container.ContainerSeparator;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;

@SideOnly(Side.CLIENT)
public class GuiSeparator extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/separator.png");
	/**
	 * The player inventory.
	 */
	private final IItemHandlerNameable playerInventory;

	/**
	 * The chest inventory.
	 */
	private final IItemHandlerNameable chestInventory;
	
	public TileEntitySeparator te;

	public GuiSeparator(ContainerSeparator container) {
		super(container);
		playerInventory = container.getPlayerInventory();
		chestInventory = container.getBlockInventory();
		this.te = (TileEntitySeparator) container.callbacks;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.chestInventory.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(
				new TextComponentTranslation("container.inventory", new Object[0]).getUnformattedText(), 8,
				ySize - 96 + 2, 4210752);
		int progressLevel = getProgressLevel(24);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		drawTexturedModalRect(107, 43, 176, 14, progressLevel + 1, 16);
		mc.getTextureManager().bindTexture(resourceLocation);
		drawTexturedModalRect(107, 43, 176, 14, progressLevel + 1, 16);
		drawTexturedModalRect(45+24-progressLevel-1, 43, 176 + 24 - progressLevel-1, 31, progressLevel + 2, 16);
		int k = this.getBurnLeftScaled(14);
		this.drawTexturedModalRect(82, 73 - k, 176, 14 - k, 14, k);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(81, 59, 14, 14, mouseX, mouseY)) {
			List<String> tooltip = new ArrayList<String>();
			tooltip.add(te.getVoltageStored() + "J");
			drawHoveringText(tooltip, mouseX, mouseY);
		}
	}
	
	private int getProgressLevel(int progressIndicatorPixelWidth) {
		int ticksGrindingItemSoFar = te.getCookTime();
		int ticksPerItem = te.getTotalCookTime();
		return ticksPerItem != 0 && ticksGrindingItemSoFar != 0
				? (int)Math.round(ticksGrindingItemSoFar * progressIndicatorPixelWidth / ticksPerItem)
				: 0;
	}
	
	private int getBurnLeftScaled(int pixels) {
		return (int) (te.getVoltageStored() * pixels / te.getCapacity());
	}
}
