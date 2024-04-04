package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelTurretAdvanced;
import nuparu.sevendaystomine.tileentity.TileEntityTurret;

@SideOnly(Side.CLIENT)
public class TileEntityTurretAdvancedRenderer extends TileEntitySpecialRenderer<TileEntityTurret> {

	private static final ResourceLocation texture = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/turret_advanced.png");
	private ModelTurretAdvanced turret = new ModelTurretAdvanced();

	public TileEntityTurretAdvancedRenderer() {

	}

	@Override
	public void render(TileEntityTurret te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		if (te != null) {
			te.getWorld();
		}
		int s = 0;
		if (te != null) {
			s = te.getBlockMetadata();
		}

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
		float rotation = 0f;
		if (te != null) {
			if (partialTicks == 1.0F) {
				rotation = te.headRotation;
			} else {
				rotation = te.headRotationPrev + (te.headRotation - te.headRotationPrev) * partialTicks;
			}
		}
		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		bindTexture(texture);
		turret.render(rotation);
		GL11.glPopMatrix();
		/*
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player == null) return;
		
		Vec3d pos = te.getHeadPosition().subtract(player.posX, player.posY, player.posZ);
		Vec3d rot = te.getHeadRotation();
		Vec3d rayEnd = pos.add(new Vec3d(rot.x * TileEntityTurret.VIEW_DISTANCE, rot.y * TileEntityTurret.VIEW_DISTANCE, rot.z * TileEntityTurret.VIEW_DISTANCE));
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d( pos.x +x, pos.y +y , pos.z + z);

		GL11.glVertex3d(rayEnd.x, rayEnd.y, rayEnd.z);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();*/
	}

}
