package nuparu.sevendaystomine.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelWallClock;
import nuparu.sevendaystomine.tileentity.TileEntityWallClock;

@SideOnly(Side.CLIENT)
public class TileEntityWallClockRenderer extends TileEntitySpecialRenderer<TileEntityWallClock> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/wall_clock.png");
	private ModelWallClock model = new ModelWallClock();

	public TileEntityWallClockRenderer() {
	}

	public void render(TileEntityWallClock te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		boolean flag = te.getWorld() != null;
		int i = flag ? te.getBlockMetadata() & 3 : 0;
		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		} else {
			if (TEXTURE != null) {
				this.bindTexture(TEXTURE);
			}
		}

		this.renderBlock(x,y,z, i, alpha);
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

	private void renderBlock(double x, double y, double z, int facing, float alpha) {
		GlStateManager.pushMatrix();
		float f = 0.0F;

		if (facing == EnumFacing.NORTH.getHorizontalIndex()) {
			f = 0.0F;
		} else if (facing == EnumFacing.SOUTH.getHorizontalIndex()) {
			f = 180.0F;
		} else if (facing == EnumFacing.WEST.getHorizontalIndex()) {
			f = -90.0F;
		} else if (facing == EnumFacing.EAST.getHorizontalIndex()) {
			f = 90.0F;
		}

		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		this.model.render(0.0625f);
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.popMatrix();
	}
}
