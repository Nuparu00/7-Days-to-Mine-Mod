package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelSolarPanel;
import nuparu.sevendaystomine.client.model.ModelSolarPanelTop;
import nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;

@SideOnly(Side.CLIENT)
public class TileEntitySolarPanelRenderer extends TileEntitySpecialRenderer<TileEntitySolarPanel> {

	private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/solar_panel.png");

	private ModelSolarPanel model = new ModelSolarPanel();
	private ModelSolarPanelTop panel = new ModelSolarPanelTop();

	public TileEntitySolarPanelRenderer() {

	}

	@Override
	public void render(TileEntitySolarPanel tileentity, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		int s = 0;

		if (tileentity != null) {
			s = tileentity.getBlockMetadata();
		}
		EnumFacing facing = tileentity.facing;

		short short1 = 0;

		if (s == 2) {
			short1 = 180;
		}

		if (s == 3) {
			short1 = 0;
		}

		if (s == 5) {
			short1 = 90;
		}

		if (s == 4) {
			short1 = -90;
		}
		if (destroyStage >= 0) {
			 this.bindTexture(DESTROY_STAGES[destroyStage]);
	            GlStateManager.matrixMode(5890);
	            GlStateManager.pushMatrix();
	            GlStateManager.scale(4.0F, 4.0F, 1.0F);
	            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
	            GlStateManager.matrixMode(5888);
		} else {
			bindTexture(TEX);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);

		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		model.render();
		GL11.glTranslatef(0F, 0.6875F, 0F);
		float rot = 0f;
		if (tileentity != null && tileentity.getWorld() != null) {
			rot = tileentity.getWorld().getCelestialAngle(partialTicks) * 360.0F;
		}

		if (facing == EnumFacing.EAST) {
			rot *= -1;
		} else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			rot = 0;
		}
		GlStateManager.rotate(rot, 1.0F, 0.0F, 0.0F);

		panel.render();

		GL11.glPopMatrix();
		if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
	}

}
