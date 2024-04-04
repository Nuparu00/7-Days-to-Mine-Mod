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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.container.ContainerFlamethrower;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityFlamethrower;

@SideOnly(Side.CLIENT)
public class GuiFlamethrower extends GuiContainer {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/flamethrower.png");
	/**
	 * The player inventory.
	 */
	private final IItemHandlerNameable playerInventory;

	/**
	 * The chest inventory.
	 */
	private final TileEntityFlamethrower tileEntity;
	
	public GuiFlamethrower(ContainerFlamethrower container) {
		super(container);
		playerInventory = container.getPlayerInventory();
		tileEntity = container.getTileEntity();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = ((IItemHandlerNameable)(this.tileEntity.getInventory())).getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, xSize - fontRenderer.getStringWidth(s) - 10, 6, 4210752);
		s = new TextComponentTranslation("container.inventory", new Object[0]).getUnformattedText();
		this.fontRenderer.drawString(s, xSize - fontRenderer.getStringWidth(s) - 10, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		FluidStack stack = tileEntity.getTank().getFluid();
		if(stack == null) return;
		Fluid fluid = stack.getFluid();
		TextureAtlasSprite fluidTexture = mc.getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int fluidHeight = tileEntity.getFluidGuiHeight(71);
		renderTiledTextureAtlas(9 + guiLeft, 8 + guiTop + (71 - fluidHeight), 14, fluidHeight, 0, fluidTexture);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		if (isPointInRegion(9, 8, 16, 78, mouseX, mouseY)) {
			List<String> tooltip = new ArrayList<String>();
			tooltip.add(tileEntity.getTank().getFluidAmount() + "mB");
			drawHoveringText(tooltip, mouseX, mouseY);
		}
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
}
