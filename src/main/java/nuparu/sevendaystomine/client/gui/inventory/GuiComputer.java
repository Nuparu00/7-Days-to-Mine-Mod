package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.ContainerComputer;

@SideOnly(Side.CLIENT)
public class GuiComputer extends GuiContainer {
	private static final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/container/computer.png");
	private final InventoryPlayer inventoryPlayer;
	private final IInventory tileForge;

	public GuiComputer(InventoryPlayer parInventoryPlayer, IInventory parInventoryGrinder) {
		super(new ContainerComputer(parInventoryPlayer, parInventoryGrinder));
		inventoryPlayer = parInventoryPlayer;
		tileForge = parInventoryGrinder;
		this.xSize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileForge.getDisplayName().getUnformattedText();
		fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2,
				4210752);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEX);
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