package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoRequestMessage;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;

@SideOnly(Side.CLIENT)
public class TileEntityPhotoRenderer extends TileEntitySpecialRenderer<TileEntityPhoto> {

	public void render(TileEntityPhoto te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		String path = te.path;

		if (path == null || path.isEmpty())
			return;

		if (te.image == null) {
			if (System.currentTimeMillis() >= te.nextUpdate) {
				te.image = ResourcesHelper.INSTANCE.getImage(path);
				if (te.image == null) {
					PacketManager.photoRequest.sendToServer(new PhotoRequestMessage(path));
					te.image = ResourcesHelper.INSTANCE.tryToGetImage(path);
				}
				te.nextUpdate = System.currentTimeMillis() + 2000;
			}
		} else if (te.image.res != null) {
			int j;

			if (!te.hasWorld()) {
				return;
			} else {
				Block block = te.getBlockType();
				j = te.getBlockMetadata();
			}

			short short1 = 0;

			if (j == 1) {
				short1 = 90;
			}

			if (j == 2) {
				short1 = 180;
			}

			if (j == 3) {
				short1 = -90;
			}

			int shape = te.image.width > te.image.height ? -1 : (te.image.width == te.image.height ? 0 : 1);

			double w = 1;
			double h = 1;
			


			if (shape == -1) {
				w = w * 0.75f;
				h = ((float) te.image.height / (float) te.image.width) * w;
			} else if (shape == 0) {
				h = h * 0.75f;
				w = h;
			} else if (shape == 1) {
				h = h * 0.75f;
				w = ((float) te.image.width / (float) te.image.height) * h;
			}
			GlStateManager.pushMatrix();
			GlStateManager.enableLighting();
			setLightmap(te.getPos().getX(),te.getPos().getY(),te.getPos().getZ(),te.getWorld());
			drawQuad((float) x, (float) y + 1.0F, (float) z + 1.0F, w, h, 0f, 0f, 1f, 1f, (float) short1,
					te.image.res);
			GlStateManager.disableLighting();
			GlStateManager.popMatrix();
		}
	}
	
	private void setLightmap(double x, double y, double z, World world)
    {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        int l = world.getCombinedLight(new BlockPos(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        GlStateManager.color(1.6F, 1.6F, 1.6F);
    }

	private void drawQuad(float x, float y, float z, double width, double height, float minU, float minV, float maxU,
			float maxV, float meta, ResourceLocation virtualLocation) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder wr = tessellator.getBuffer();

		this.bindTexture(virtualLocation);
		if (meta == 90) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glPushMatrix();
			GL11.glTranslated(x+0.9999, y-1, z - 1);
			wr.pos(-0.001, 0.5+(height/2f), 0.5-(width/2f)).tex(minU, minV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5-(width/2f)).tex(minU, maxV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5+(width/2f)).tex(maxU, maxV).endVertex();
			wr.pos(-0.001, 0.5+(height/2f), 0.5+(width/2f)).tex(maxU, minV).endVertex();
			

			tessellator.draw();
			GL11.glPopMatrix();
		}

		if (meta == -90) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glPushMatrix();
			GL11.glTranslated(x+0.0001, y-1, z);
			GL11.glRotatef(meta+270, 0.0F, -1.0F, 0.0F);
			wr.pos(-0.001, 0.5+(height/2f), 0.5-(width/2f)).tex(minU, minV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5-(width/2f)).tex(minU, maxV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5+(width/2f)).tex(maxU, maxV).endVertex();
			wr.pos(-0.001, 0.5+(height/2f), 0.5+(width/2f)).tex(maxU, minV).endVertex();

			tessellator.draw();
			GL11.glPopMatrix();
		}

		if (meta == 0) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glPushMatrix();
			GL11.glTranslated(x, y-1, z - 0.9999);
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			// GL11.glScalef(-1.0F, -1.0F, 1.0F);
			wr.pos(-0.001, 0.5+(height/2f), 0.5-(width/2f)).tex(minU, minV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5-(width/2f)).tex(minU, maxV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5+(width/2f)).tex(maxU, maxV).endVertex();
			wr.pos(-0.001, 0.5+(height/2f), 0.5+(width/2f)).tex(maxU, minV).endVertex();

			tessellator.draw();
			GL11.glPopMatrix();
		}

		if (meta == 180) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glPushMatrix();
			GL11.glTranslated(x + 1, y-1, z-0.0001);
			GL11.glRotatef(90, 0.0F, -1.0F, 0.0F);
			// GL11.glScalef(-1.0F, -1.0F, 1.0F);
			wr.pos(-0.001, 0.5+(height/2f), 0.5-(width/2f)).tex(minU, minV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5-(width/2f)).tex(minU, maxV).endVertex();
			wr.pos(-0.001, 0.5-(height/2f), 0.5+(width/2f)).tex(maxU, maxV).endVertex();
			wr.pos(-0.001, 0.5+(height/2f), 0.5+(width/2f)).tex(maxU, minV).endVertex();

			tessellator.draw();
			GL11.glPopMatrix();
		}

	}
}