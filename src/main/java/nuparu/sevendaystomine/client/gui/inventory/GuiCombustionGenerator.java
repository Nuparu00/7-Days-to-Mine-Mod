package nuparu.sevendaystomine.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.container.ContainerGenerator;
import nuparu.sevendaystomine.tileentity.TileEntityGeneratorBase;

@SideOnly(Side.CLIENT)
public class GuiCombustionGenerator extends GuiContainer {
	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/generator_combustion.png");

	TileEntityGeneratorBase tileEntity;
	public GuiCombustionGenerator(ContainerGenerator container) {
		super(container);
		this.tileEntity = (TileEntityGeneratorBase) container.callbacks;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileEntity.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.format("gui.electricity.voltage") + tileEntity.getPowerPerUpdate() + "J", 55, 44,
				4210752);
		fontRenderer.drawString(I18n.format("gui.electricity.stored") + tileEntity.getVoltageStored() + "/"
				+ tileEntity.getCapacity() + "J", 55, 54, 4210752);
		mc.getTextureManager().bindTexture(resourceLocation);
		drawTexturedModalRect(30 + (int) (tileEntity.getTemperature() * 138), 32, 190, 31, 6, 8);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		if (tileEntity.isBurning()) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(marginHorizontal + 30, marginVertical + 45 + 12 - k, 176, 12 - k, 14, k + 1);
		}
	}

	private int getBurnLeftScaled(int pixels) {
		int i = tileEntity.getField(1);
		if (i == 0) {
			i = 200;
		}

		return tileEntity.getField(0) * pixels / i;
	}

}
