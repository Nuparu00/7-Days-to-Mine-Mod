package nuparu.sevendaystomine.client.gui.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.container.ContainerGenerator;
import nuparu.sevendaystomine.tileentity.TileEntityGasGenerator;

@SideOnly(Side.CLIENT)
public class GuiGasGenerator extends GuiContainer {
	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/generator_gas.png");
	InventoryPlayer playerInventory;
	TileEntityGasGenerator tileEntity;

	int fluidHeight = 0;

	public GuiGasGenerator(ContainerGenerator container) {
		super(container);
		this.tileEntity = (TileEntityGasGenerator) container.callbacks;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (isPointInRegion(9, 8, 16, 78, mouseX, mouseY)) {
			if (tileEntity.getTank().getFluidAmount() > 0) {
				List<String> tooltip = new ArrayList<String>();
				tooltip.add(tileEntity.getTank().getFluidAmount() + "mB");
				drawHoveringText(tooltip, mouseX, mouseY);
			}
		} else {
			this.renderHoveredToolTip(mouseX, mouseY);
		}

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
		drawTexturedModalRect(9, 8, 176, 31, 14, 71);
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

		Fluid fluid = tileEntity.getTank().getFluid().getFluid();
		TextureAtlasSprite fluidTexture = mc.getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		fluidHeight = tileEntity.getFluidGuiHeight(71);
		renderTiledTextureAtlas(9 + guiLeft, 8 + guiTop + (71 - fluidHeight), 14, fluidHeight, 0, fluidTexture);

	}

	public void renderTiledTextureAtlas(int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		putTiledTextureQuads(buffer, x, y, width, height, depth, sprite);

		tessellator.draw();
	}

	public void putTiledTextureQuads(BufferBuilder buffer, int x, int y, int width, int height, float depth,
			TextureAtlasSprite sprite) {
		float u1 = sprite.getMinU();
		float v1 = sprite.getMinV();

		// tile vertically
		do {
			int renderHeight = Math.min(sprite.getIconHeight(), height);
			height -= renderHeight;

			float v2 = sprite.getInterpolatedV((16f * renderHeight) / (float) sprite.getIconHeight());

			// we need to draw the quads per width too
			int x2 = x;
			int width2 = width;
			// tile horizontally
			do {
				int renderWidth = Math.min(sprite.getIconWidth(), width2);
				width2 -= renderWidth;

				float u2 = sprite.getInterpolatedU((16f * renderWidth) / (float) sprite.getIconWidth());

				buffer.pos(x2, y, depth).tex(u1, v1).endVertex();
				buffer.pos(x2, y + renderHeight, depth).tex(u1, v2).endVertex();
				buffer.pos(x2 + renderWidth, y + renderHeight, depth).tex(u2, v2).endVertex();
				buffer.pos(x2 + renderWidth, y, depth).tex(u2, v1).endVertex();

				x2 += renderWidth;
			} while (width2 > 0);

			y += renderHeight;
		} while (height > 0);
	}

	private int getBurnLeftScaled(int pixels) {
		int i = tileEntity.getField(1);
		if (i == 0) {
			i = 200;
		}

		return tileEntity.getField(0) * pixels / i;
	}

}
