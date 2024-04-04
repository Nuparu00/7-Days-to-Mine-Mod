package nuparu.sevendaystomine.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelSleepingBag;
import nuparu.sevendaystomine.tileentity.TileEntitySleepingBag;

@SideOnly(Side.CLIENT)
public class TileEntitySleepingBagRenderer extends TileEntitySpecialRenderer<TileEntitySleepingBag> {
	private static final ResourceLocation[] TEXTURES;
	private ModelSleepingBag model = new ModelSleepingBag();
	private int version;

	public TileEntitySleepingBagRenderer() {
		this.version = this.model.getModelVersion();
	}

	public void render(TileEntitySleepingBag te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (this.version != this.model.getModelVersion()) {
			this.model = new ModelSleepingBag();
			this.version = this.model.getModelVersion();
		}
		boolean flag = te.getWorld() != null;
		boolean flag1 = flag ? te.isHeadPiece() : true;
		EnumDyeColor enumdyecolor = te != null ? te.getColor() : EnumDyeColor.RED;
		int i = flag ? te.getBlockMetadata() & 3 : 0;

		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		} else {
			ResourceLocation resourcelocation = TEXTURES[enumdyecolor.getMetadata()];

			if (resourcelocation != null) {
				this.bindTexture(resourcelocation);
			}
		}

		if (flag) {
			this.renderPiece(flag1, x, y, z, i, alpha);
		} else {
			GlStateManager.pushMatrix();
			this.renderPiece(true, x, y, z, i, alpha);
			this.renderPiece(false, x, y, z - 1.0D, i, alpha);
			GlStateManager.popMatrix();
		}

		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

	private void renderPiece(boolean p_193847_1_, double x, double y, double z, int facing, float alpha) {
		this.model.preparePiece(p_193847_1_);
		GlStateManager.pushMatrix();
		float f = 0.0F;
		float f1 = 0.0F;
		float f2 = 0.0F;

		if (facing == EnumFacing.NORTH.getHorizontalIndex()) {
			f = 0.0F;
			f1 += 0.5f;
			f2 += 0.5f;
		} else if (facing == EnumFacing.SOUTH.getHorizontalIndex()) {
			f = 180.0F;
			f1 = 1.0F;
			f2 = 1.0F;
			f1 -= 0.5f;
			f2 -= 0.5f;
		} else if (facing == EnumFacing.WEST.getHorizontalIndex()) {
			f = -90.0F;
			f2 = 1.0F;
			f1 += 0.5f;
			f2 -= 0.5f;
		} else if (facing == EnumFacing.EAST.getHorizontalIndex()) {
			f = 90.0F;
			f1 = 1.0F;
			f1 -= 0.5f;
			f2 += 0.5f;
		}

		GlStateManager.translate((float) x + f1, (float) y + 1.5F, (float) z + f2);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		this.model.render();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.popMatrix();
	}

	static {
		EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
		TEXTURES = new ResourceLocation[aenumdyecolor.length];

		for (EnumDyeColor enumdyecolor : aenumdyecolor) {
			TEXTURES[enumdyecolor.getMetadata()] = new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/sleepingbag/" + enumdyecolor.getDyeColorName() + ".png");
		}
	}
}
