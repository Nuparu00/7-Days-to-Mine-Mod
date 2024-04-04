package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;

@SideOnly(Side.CLIENT)
public class GuiWorkbench extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/workbench.png");
	InventoryPlayer playerInventory;

	public GuiWorkbench(InventoryPlayer playerInventory, Container container) {
		super(container);
		this.playerInventory = playerInventory;
		this.xSize = 176;
		this.ySize = 188;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = I18n.format("container.workbench");
		RenderUtils.drawCenteredString(s, 134, 6, 4210752);
		this.fontRenderer.drawString(
				new TextComponentTranslation("container.inventory", new Object[0]).getUnformattedText(), 8,
				ySize - 96 + 5, 4210752);
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
	}
}
