package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelCamera;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;

@SideOnly(Side.CLIENT)
public class TileEntityCameraRenderer extends TileEntitySpecialRenderer<TileEntityCamera> {

	private static final ResourceLocation texture = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/camera.png");
	private ModelCamera camera = new ModelCamera();

	public TileEntityCameraRenderer() {

	}

	@Override
	public void render(TileEntityCamera te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (te != null) {
			te.getWorld();
		}
		int s = 0;
		if (te != null) {
			s = te.getBlockMetadata();
		}

		short short1 = 0;

		if (s == 2) {
			short1 = 0;
		}

		if (s == 3) {
			short1 = 180;
		}

		if (s == 5) {
			short1 = 270;
		}

		if (s == 4) {
			short1 = 90;
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);

		
		float rotation = 0f;
		if (te != null) {
			if (partialTicks == 1.0F) {
				rotation = te.getHeadRotation();
			} else {
				rotation = te.getHeadRotationPrev() + (te.getHeadRotation() - te.getHeadRotationPrev()) * partialTicks;
			}
		}
		
		bindTexture(texture);
		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		if(!te.isOn()) {
			rotation = 0;
		}
		camera.setAngle(rotation);
		camera.render(null, 0, 0, 0, 0, 0, 0.0625f);
		GL11.glPopMatrix();
	}

}
