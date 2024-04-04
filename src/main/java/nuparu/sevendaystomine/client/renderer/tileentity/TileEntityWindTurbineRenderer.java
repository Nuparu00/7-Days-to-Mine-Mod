package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelWindTurbine;
import nuparu.sevendaystomine.client.model.ModelWindTurbinePropeller;
import nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;

@SideOnly(Side.CLIENT)
public class TileEntityWindTurbineRenderer extends TileEntitySpecialRenderer<TileEntityWindTurbine> {

	private static final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/wind_turbine.png");
	private static final ResourceLocation TEX_PROPELLER = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/wind_turbine_propeller.png");
	private ModelWindTurbinePropeller propeller = new ModelWindTurbinePropeller();
	private ModelWindTurbine turbine = new ModelWindTurbine();

	public TileEntityWindTurbineRenderer() {

	}

	@Override
	public void render(TileEntityWindTurbine tileentity, double x, double y, double z, float partialTicks,
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
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		bindTexture(TEX);

		turbine.render();

		float rot = 0;
		if (tileentity != null) {
			float angle = tileentity.angle;
			float prev = tileentity.anglePrev;
			if (prev > angle) {
				prev -= 360;
			}
			rot = prev + (angle - prev) * partialTicks;
		}

		this.bindTexture(TEX_PROPELLER);
		GL11.glTranslatef(0.0F, -0.375F, 0F);
		GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
		propeller.render();

		GL11.glPopMatrix();
	}

}
