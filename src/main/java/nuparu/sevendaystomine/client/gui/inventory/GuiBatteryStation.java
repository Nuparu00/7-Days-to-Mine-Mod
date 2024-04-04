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
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.inventory.container.ContainerBatteryStation;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityBatteryStation;

@SideOnly(Side.CLIENT)
public class GuiBatteryStation extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/battery_station.png");
	/**
	 * The player inventory.
	 */
	private final IItemHandlerNameable playerInventory;

	/**
	 * The chest inventory.
	 */
	private final TileEntityBatteryStation te;

	public GuiBatteryStation(ContainerBatteryStation containerBatteryStation) {
		super(containerBatteryStation);
		playerInventory = containerBatteryStation.getPlayerInventory();
		te = (TileEntityBatteryStation) containerBatteryStation.callbacks;
	}

	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	RenderUtils.drawCenteredString(
				new TextComponentTranslation("container.inventory", new Object[0]).getUnformattedText(), xSize/2,
				ySize - 96 + 2, 4210752);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		double percentage = ((double)te.getVoltageStored() / te.getCapacity());
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				drawTexturedModalRect(7 + (128 * j), 4 + (27 * i) + 23 - (int) Math.round((23 * percentage)), 176,
						14 + 23 - (int) Math.round((23 * percentage)), 34, (int) Math.round((23 * percentage)));
			}
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		double percentage = te.getVoltageStored() / te.getCapacity();

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (isPointInRegion(7, 4, 40, 80, mouseX, mouseY) || isPointInRegion(135, 4, 40, 80, mouseX, mouseY) ) {
			/*if (tileEntity.getTank().getFluidAmount() > 0) {
				List<String> tooltip = new ArrayList<String>();
				tooltip.add(tileEntity.getTank().getFluidAmount() + "mB");
				drawHoveringText(tooltip, mouseX, mouseY);
			}*/
			List<String> tooltip = new ArrayList<String>();
			tooltip.add(te.getVoltageStored() + "/" + te.getCapacity() + "J");
			tooltip.add(String.format("%.2f",((double)te.getVoltageStored()/te.getCapacity())*100) + "%");
			drawHoveringText(tooltip, mouseX, mouseY);
		}
		else {
			this.renderHoveredToolTip(mouseX, mouseY);
		}

	}
}
