package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.block.BlockScreenProjector;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoRequestMessage;
import nuparu.sevendaystomine.tileentity.TileEntityScreenProjector;

@SideOnly(Side.CLIENT)
public class TileEntityScreenProjectorRenderer extends TileEntitySpecialRenderer<TileEntityScreenProjector> {

	//TO-DO: clean this
	public void render(TileEntityScreenProjector te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (!te.hasValidImage())
			return;

		String path = te.getImagePath();

		if (path == null || path.isEmpty())
			return;

		if (te.image == null || !te.image.path.equals(path)) {
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

			short short1 = 90;

			if (j == 5) {
				short1 = 270;
			}

			if (j == 2) {
				short1 = 180;
			}

			if (j == 3) {
				short1 = 0;
			}

			World world = te.getWorld();

			BlockPos imagePos = te.getPos();
			IBlockState blockState = world.getBlockState(te.getPos());
			Block block = blockState.getBlock();
			if(!(block instanceof BlockScreenProjector)) return;
			EnumFacing facing = blockState.getValue(BlockHorizontalBase.FACING).getOpposite();

			int distance = 0;
			for (int i = 1; i < 16; i++) {
				BlockPos pos = imagePos.offset(facing, i);
				IBlockState state = world.getBlockState(pos);
				Block block2 = state.getBlock();

				if (!block2.isReplaceable(world, pos) && state.getMaterial().blocksMovement() && block2 != Blocks.AIR) {
					imagePos = pos;
					distance = i;
					break;
				}
			}

			if (imagePos != te.getPos()) {
				int shape = te.image.width > te.image.height ? -1 : (te.image.width == te.image.height ? 0 : 1);

				double w = distance;
				double h = distance;

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

				// imagePos.add(0, 1, 1);

				BlockPos delta = te.getPos().subtract(imagePos);

				GL11.glPushMatrix();
				/*
				 * GlStateManager.enableTexture2D(); GL11.glEnable(GL11.GL_BLEND);
				 * GL11.glEnable(GL11.GL_ALPHA_TEST); GL11.glAlphaFunc(GL11.GL_ALWAYS, 1.0f);
				 * 
				 * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				 * OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f,
				 * 240f);
				 */
				// 
				drawQuad(x - delta.getX(), y - delta.getY() + (distance == 2 ? h : (h / 2d)), z - delta.getZ(), w, h,
						0f, 0f, 1f, 1f, (float) short1, distance, te.image.res);
				/*
				 * GL11.glColor4d(1f, 1f, 1f, 1f); GL11.glDisable(GL11.GL_ALPHA_TEST);
				 * GL11.glDisable(GL11.GL_BLEND); GL11.glEnable(GL11.GL_LIGHTING);
				 * GlStateManager.disableTexture2D();
				 */
				GL11.glPopMatrix();

			}

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.enableAlpha();
			GlStateManager.disableTexture2D();
			GL11.glTranslated((float) x + 0.685f, (float) y + 0.25f, (float) z + 0.75f);

			if (facing == EnumFacing.EAST) {
				GL11.glTranslated(0.0625, 0,-0.425);
				GL11.glRotated(90, 0f, 1f, 0f);
			} else if (facing == EnumFacing.WEST) {
				GL11.glTranslated(-0.425, 0,-0.0625);
				GL11.glRotated(90, 0f, -1f, 0f);
			} else if (facing == EnumFacing.NORTH) {
				GL11.glTranslated(-0.365, 0,-0.5);
				GL11.glRotated(180, 0f, -1f, 0f);
			}

			renderBeam();
			GlStateManager.enableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();

		}
	}

	private void drawQuad(double x, double y, double z, double width, double height, float minU, float minV, float maxU,
			float maxV, float meta, int distance, ResourceLocation virtualLocation) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder wr = tessellator.getBuffer();
		Minecraft.getMinecraft().renderEngine.bindTexture(virtualLocation);
		if (meta == 90) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glTranslated(x - 0.01, y - 1, z);
			wr.pos(0, 0.5 + (height / 2f), 0.5 - (width / 2f)).tex(minU, minV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 - (width / 2f)).tex(minU, maxV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 + (width / 2f)).tex(maxU, maxV).endVertex();
			wr.pos(0, 0.5 + (height / 2f), 0.5 + (width / 2f)).tex(maxU, minV).endVertex();

			tessellator.draw();

		}

		if (meta == 270) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			// 
			GL11.glTranslated(x + 1.01, y - 1, z + 1);
			GL11.glRotated(meta + 270, 0.0F, -1.0F, 0.0F);

			wr.pos(0, 0.5 + (height / 2f), 0.5 - (width / 2f)).tex(minU, minV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 - (width / 2f)).tex(minU, maxV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 + (width / 2f)).tex(maxU, maxV).endVertex();
			wr.pos(0, 0.5 + (height / 2f), 0.5 + (width / 2f)).tex(maxU, minV).endVertex();

			tessellator.draw();

		}

		if (meta == 0) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glTranslated(x, y - 1, z + 1.01);
			GL11.glRotated(90F, 0.0F, 1.0F, 0.0F);
			// GL11.glScalef(-1.0F, -1.0F, 1.0F);
			wr.pos(0, 0.5 + (height / 2f), 0.5 - (width / 2f)).tex(minU, minV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 - (width / 2f)).tex(minU, maxV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 + (width / 2f)).tex(maxU, maxV).endVertex();
			wr.pos(0, 0.5 + (height / 2f), 0.5 + (width / 2f)).tex(maxU, minV).endVertex();

			tessellator.draw();

		}

		if (meta == 180) {
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);

			GL11.glTranslated(x + 1, y - 1, z - 0.01);
			GL11.glRotated(90, 0.0F, -1.0F, 0.0F);
			// GL11.glScalef(-1.0F, -1.0F, 1.0F);
			wr.pos(0, 0.5 + (height / 2f), 0.5 - (width / 2f)).tex(minU, minV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 - (width / 2f)).tex(minU, maxV).endVertex();
			wr.pos(0, 0.5 - (height / 2f), 0.5 + (width / 2f)).tex(maxU, maxV).endVertex();
			wr.pos(0, 0.5 + (height / 2f), 0.5 + (width / 2f)).tex(maxU, minV).endVertex();

			tessellator.draw();

		}
	}

	/*
	 * public void renderBeam() {
	 * 
	 * GL11.glBegin(GL11.GL_TRIANGLES); // Triangle 1 GL11.glColor4d(0.75, 0.75, 0.8, 1);
	 * GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red) GL11.glColor4d(0.75, 0.75, 0.8, 0);
	 * GL11.glVertex3f(-0.5f, -1.5f, 0.5f); // V1(green) GL11.glColor4d(0.75, 0.75, 0.8, 0);
	 * GL11.glVertex3f(0.5f, -1.5f, 0.5f); // V2(blue) // Triangle 2
	 * GL11.glColor4d(0.75, 0.75, 0.8, 1); GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
	 * GL11.glColor4d(0.75, 0.75, 0.8, 0); GL11.glVertex3f(0.5f, -1.5f, 0.5f); // V2(blue)
	 * GL11.glColor4d(0.75, 0.75, 0.8, 0); GL11.glVertex3f(0.5f, -1.5f, -0.5f); // V3(green)
	 * // Triangle 3 GL11.glColor4d(0.75, 0.75, 0.8, 1); GL11.glVertex3f(0.0f, 0.0f, 0.0f);
	 * // V0(red) GL11.glColor4d(0.75, 0.75, 0.8, 0); GL11.glVertex3f(0.5f, -1.5f, -0.5f);
	 * // V3(green) GL11.glColor4d(0.75, 0.75, 0.8, 0); GL11.glVertex3f(-0.5f, -1.5f,
	 * -0.5f); // V4(blue) // Triangle 4 GL11.glColor4d(0.75, 0.75, 0.8, 1);
	 * GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red) GL11.glColor4d(0.75, 0.75, 0.8, 0);
	 * GL11.glVertex3f(-0.5f, -1.5f, -0.5f); // V4(blue) GL11.glColor4d(0.75, 0.75, 0.8, 0);
	 * GL11.glVertex3f(-0.5f, -1.5f, 0.5f); // V1(green) GL11.glEnd(); }
	 */

	public void renderBeam() {

		GL11.glBegin(GL11.GL_TRIANGLES);
		// Triangle 1
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, 0.5f, 1.5f); // V1(green)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, 0.5f, 1.5f); // V2(blue)
		// Triangle 2
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, 0.5f, 1.5f); // V2(blue)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, -0.5f, 1.5f); // V3(green)
		// Triangle 3
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, -0.5f, 1.5f); // V2(blue)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, 0.5f, 1.5f); // V3(green)
		// Triangle 4
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, 0.5f, 1.5f); // V1(green)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, 0.5f, 1.5f); // V2(blue)

		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, -0.5f, 1.5f); // V1(green)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, -0.5f, 1.5f); // V2(blue)
		// Triangle 2
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, 0.5f, 1.5f); // V2(blue)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, -0.5f, 1.5f); // V3(green)
		// Triangle 3
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, -0.5f, 1.5f); // V2(blue)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, 0.5f, 1.5f); // V3(green)
		// Triangle 4
		GL11.glColor4d(0.75, 0.75, 0.8, 1);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f); // V0(red)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(0.5f, -0.5f, 1.5f); // V1(green)
		GL11.glColor4d(0.75, 0.75, 0.8, 0);
		GL11.glVertex3f(-0.5f, -0.5f, 1.5f); // V2(blue)
		GL11.glEnd();
	}

}