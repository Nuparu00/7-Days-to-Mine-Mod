package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

@SideOnly(Side.CLIENT)
public class GuiProjector extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/projector.png");
	InventoryPlayer playerInventory;
	TileEntity tileEntity;

	public GuiProjector(InventoryPlayer playerInventory, IInventory tileEntity, Container container) {
		super(container);
		this.playerInventory = playerInventory;
		this.tileEntity = (TileEntity) tileEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiArrowButton(0, guiLeft + 99, guiTop + 34, 0));
		this.buttonList.add(new GuiArrowButton(1, guiLeft + 57, guiTop + 34, 1));
		this.buttonList.add(new GuiArrowButton(2, guiLeft + 78, guiTop + 13, 2));
		this.buttonList.add(new GuiArrowButton(3, guiLeft + 78, guiTop + 55, 3));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileEntity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, 8, 4, 4210752);
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
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public class GuiArrowButton extends GuiButton {

		int direction;

		public GuiArrowButton(int buttonId, int x, int y, int direction) {
			super(buttonId, x, y, 20, 20, "");
			this.direction = direction;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {

				mc.getTextureManager().bindTexture(resourceLocation);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;

				int i = 0;

				if (flag) {
					i += this.height;
				}
				
				this.drawTexturedModalRect(this.x, this.y, 176 + (direction * this.width), i, this.width, this.height);

			}
		}

	}
}
