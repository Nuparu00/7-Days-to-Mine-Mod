package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.inventory.container.ContainerCamera;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;

@SideOnly(Side.CLIENT)
public class GuiCamera extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/container_tiny.png");
	/**
	 * The player inventory.
	 */
	private final IItemHandlerNameable playerInventory;

	/**
	 * The chest inventory.
	 */
	private final IItemHandlerExtended camera;
	private String displayName;

	public GuiCamera(ContainerCamera container,String displayName) {
		super(container);
		playerInventory = container.playerInventory;
		camera = container.camera;
		this.displayName = displayName; 
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = displayName;
		RenderUtils.drawCenteredString(s, 88, 38, 4210752);
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
}
