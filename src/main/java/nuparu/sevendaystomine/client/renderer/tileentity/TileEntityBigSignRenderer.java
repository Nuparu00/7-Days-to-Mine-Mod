package nuparu.sevendaystomine.client.renderer.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;

@SideOnly(Side.CLIENT)
public class TileEntityBigSignRenderer extends TileEntitySpecialRenderer<TileEntityBigSignMaster> {
	public void render(TileEntityBigSignMaster te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		Block block = te.getBlockType();
		GlStateManager.pushMatrix();
		float f = 0.6666667F;

		int k = te.getBlockMetadata();
		float f2 = 0.0F;

		if (k == 2) {
			f2 = 180.0F;
		}

		if (k == 4) {
			f2 = 90.0F;
		}

		if (k == 5) {
			f2 = -90.0F;
		}

		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0F, -0.3125F, -0.4375F);

		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 2.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
		GlStateManager.popMatrix();
		FontRenderer fontrenderer = this.getFontRenderer();
		float f3 = 0.010416667F;
		GlStateManager.translate(0.0F, 1.25F, 0.046666667F);
		GlStateManager.scale(0.060416667F, -0.060416667F, 0.060416667F);
		GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
		GlStateManager.depthMask(false);
		int i = 0;

		if (destroyStage < 0) {
			for (int j = 0; j < te.signText.length; ++j) {
				if (te.signText[j] != null) {
					ITextComponent itextcomponent = te.signText[j];
					List<ITextComponent> list = GuiUtilRenderComponents.splitText(itextcomponent, 90, fontrenderer,
							false, true);
					String s = list != null && !list.isEmpty() ? ((ITextComponent) list.get(0)).getFormattedText() : "";

					if (j == te.lineBeingEdited) {
						s = "> " + s + " <";
						fontrenderer.drawString(te.getTextColor()+s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5,
								0);
					} else {
						fontrenderer.drawString(te.getTextColor()+s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5,
								0);
					}
				}
			}
		}

		GlStateManager.depthMask(true);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();

		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}