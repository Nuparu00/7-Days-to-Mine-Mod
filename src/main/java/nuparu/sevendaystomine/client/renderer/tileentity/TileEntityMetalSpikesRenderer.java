package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.tileentity.TileEntityMetalSpikes;

@SideOnly(Side.CLIENT)
public class TileEntityMetalSpikesRenderer extends TileEntitySpecialRenderer<TileEntityMetalSpikes> {

	public static ResourceLocation RES = new ResourceLocation("textures/blocks/anvil_base.png");

	public TileEntityMetalSpikesRenderer() {

	}

	@Override
	public void render(TileEntityMetalSpikes te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (!te.isRetracted()) {

			int meta = te.getBlockMetadata();

			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.28f, (float) y+ 0.0625f, (float) z + 0.28f);
			if (meta == 0) {
				GL11.glTranslatef(0.44f,1- 0.127f,0);
				GL11.glRotatef(180, 0, 0, 1);
			}
			else if (meta == 2) {
				GL11.glTranslatef(0f,0.218f,0.66f);
				GL11.glRotatef(90, -1, 0, 0);
			}
			else if (meta == 3) {
				GL11.glTranslatef(0f,0.658f,-0.22f);
				GL11.glRotatef(90, 1, 0, 0);
			}
			else if (meta == 4) {
				GL11.glTranslatef(0.66f,0.218f,0);
				GL11.glRotatef(90, 0, 0, 1);
			}
			else if (meta == 5) {
				GL11.glTranslatef(-0.22f,0.658f,0);
				GL11.glRotatef(90, 0, 0, -1);
			}


			renderSpike(RES, 0.15625f, 0.5f);
			GL11.glTranslatef(0.44f, 0, 0f);
			renderSpike(RES, 0.15625f, 0.5f);
			GL11.glTranslatef(0f, 0, 0.44f);
			renderSpike(RES, 0.15625f, 0.5f);
			GL11.glTranslatef(-0.44f, 0, 0f);
			renderSpike(RES, 0.15625f, 0.5f);

			GL11.glPopMatrix();
		}
	}

	public void renderSpike(ResourceLocation side, float width, float height) {

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		bindTexture(side);
		GL11.glBegin(GL11.GL_TRIANGLES);
		// Triangle 1
		GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glTexCoord2d(0f, width); // V0(red)
		GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glTexCoord2d(1f, 1f); // V2(blue)
		GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glTexCoord2d(0f, 1f); // V3(green)

		GL11.glVertex3f(0.0f, height, 0.0f);
		GL11.glTexCoord2d(0f, -1); // V0(red)
		GL11.glVertex3f(-width, 0.0f, width);
		GL11.glTexCoord2d(1f, 0f); // V2(blue)
		GL11.glVertex3f(width, 0.0f, width);
		GL11.glTexCoord2d(1f, 1f); // V3(green)
		// Triangle 2
		GL11.glVertex3f(0.0f, height, 0.0f);
		GL11.glTexCoord2d(0f, width); // V0(red)
		GL11.glVertex3f(width, 0.0f, width);
		GL11.glTexCoord2d(1f, 0f); // V2(blue)
		GL11.glVertex3f(width, 0.0f, -width);
		GL11.glTexCoord2d(1f, 1f); // V3(green)
		// Triangle 3
		GL11.glVertex3f(0.0f, height, 0.0f);
		GL11.glTexCoord2d(0f, width); // V0(red)
		GL11.glVertex3f(width, 0.0f, -width);
		GL11.glTexCoord2d(1f, 0f); // V3(green)
		GL11.glVertex3f(-width, 0.0f, -width);
		GL11.glTexCoord2d(1f, 1f); // V4(blue)
		// Triangle 4
		GL11.glVertex3f(0.0f, height, 0.0f);
		GL11.glTexCoord2d(0f, width); // V0(red)
		GL11.glVertex3f(-width, 0.0f, -width);
		GL11.glTexCoord2d(1f, 0f); // V4(blue)
		GL11.glVertex3f(-width, 0.0f, width);
		GL11.glTexCoord2d(1f, 1f); // V1(green)
		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

}
