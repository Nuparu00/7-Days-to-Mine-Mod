package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityLootableCorpse;

@SideOnly(Side.CLIENT)
public class GuiContainerLootableEntity extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation("textures/gui/container/dispenser.png");
	protected InventoryPlayer playerInventory;
	protected EntityLootableCorpse entity;

	public GuiContainerLootableEntity(InventoryPlayer playerInventory, EntityLootableCorpse entity,
			Container container) {
		super(container);
		this.playerInventory = playerInventory;
		this.entity = entity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = entity.getOriginal().getDisplayName().getUnformattedText();
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
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public void updateScreen() {
		super.updateScreen();
		if (entity.isDead) {
			Minecraft.getMinecraft().currentScreen = null;
		}
	}

}
