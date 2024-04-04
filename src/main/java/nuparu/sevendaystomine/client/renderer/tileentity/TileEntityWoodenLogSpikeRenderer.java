package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;

@SideOnly(Side.CLIENT)
public class TileEntityWoodenLogSpikeRenderer extends TileEntitySpecialRenderer<TileEntityWoodenLogSpike> {

	public static ResourceLocation OAK = new ResourceLocation("textures/blocks/log_oak.png");
	public static ResourceLocation BIRCH = new ResourceLocation("textures/blocks/log_birch.png");
	public static ResourceLocation SPRUCE = new ResourceLocation("textures/blocks/log_spruce.png");
	public static ResourceLocation JUNGLE = new ResourceLocation("textures/blocks/log_jungle.png");
	public static ResourceLocation ACACIA = new ResourceLocation("textures/blocks/log_acacia.png");
	public static ResourceLocation DARK_OAK = new ResourceLocation("textures/blocks/log_big_oak.png");
	
	public static ResourceLocation OAK_TOP = new ResourceLocation("textures/blocks/log_oak_top.png");
	public static ResourceLocation BIRCH_TOP = new ResourceLocation("textures/blocks/log_birch_top.png");
	public static ResourceLocation SPRUCE_TOP = new ResourceLocation("textures/blocks/log_spruce_top.png");
	public static ResourceLocation JUNGLE_TOP = new ResourceLocation("textures/blocks/log_jungle_top.png");
	public static ResourceLocation ACACIA_TOP = new ResourceLocation("textures/blocks/log_acacia_top.png");
	public static ResourceLocation DARK_OAK_TOP = new ResourceLocation("textures/blocks/log_big_oak_top.png");
	
	public TileEntityWoodenLogSpikeRenderer() {

	}

	@Override
	public void render(TileEntityWoodenLogSpike te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		Block block = te.getBlockType();
		
		ResourceLocation side = OAK;
		ResourceLocation top = OAK_TOP;
		
		if(block == ModBlocks.BIRCH_LOG_SPIKE) {
			side = BIRCH;
			top = BIRCH_TOP;
		} else if(block == ModBlocks.SPRUCE_LOG_SPIKE) {
			side = SPRUCE;
			top = SPRUCE_TOP;
		} else if(block == ModBlocks.JUNGLE_LOG_SPIKE) {
			side = JUNGLE;
			top = JUNGLE_TOP;
		} else if(block == ModBlocks.ACACIA_LOG_SPIKE) {
			side = ACACIA;
			top = ACACIA_TOP;
		} else if(block == ModBlocks.DARK_OAK_LOG_SPIKE) {
			side = DARK_OAK;
			top = DARK_OAK_TOP;
		}
		
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);

		bindTexture(side);
		GL11.glBegin(GL11.GL_TRIANGLES);
		// Triangle 1
		GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.0f);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.0f);
		GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.0f);

		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.5f); // V0(red)
		GL11.glVertex3f(-0.5f, 0.0f, 0.5f);
		GL11.glTexCoord2d(1f, 0f); // V1(green)
		GL11.glVertex3f(0.5f, 0.0f, 0.5f);
		GL11.glTexCoord2d(1f, 1f); // V2(blue)
		// Triangle 2
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.5f); // V0(red)
		GL11.glVertex3f(0.5f, 0.0f, 0.5f);
		GL11.glTexCoord2d(1f, 0f); // V2(blue)
		GL11.glVertex3f(0.5f, 0.0f, -0.5f);
		GL11.glTexCoord2d(1f, 1f); // V3(green)
		// Triangle 3
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.5f); // V0(red)
		GL11.glVertex3f(0.5f, 0.0f, -0.5f);
		GL11.glTexCoord2d(1f, 0f); // V3(green)
		GL11.glVertex3f(-0.5f, 0.0f, -0.5f);
		GL11.glTexCoord2d(1f, 1f); // V4(blue)
		// Triangle 4
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glTexCoord2d(0f, 0.5f); // V0(red)
		GL11.glVertex3f(-0.5f, 0.0f, -0.5f);
		GL11.glTexCoord2d(1f, 0f); // V4(blue)
		GL11.glVertex3f(-0.5f, 0.0f, 0.5f);
		GL11.glTexCoord2d(1f, 1f); // V1(green)
		GL11.glEnd();
		bindTexture(top);
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(0f, 0.0f);
		GL11.glVertex3f(-0.5f, 0f, 0.5f);// V7
		GL11.glTexCoord2d(0f, 1.0f);
		GL11.glVertex3f(-0.5f, 0f, -0.5f); // V5
		GL11.glTexCoord2d(1f, 1.0f);
		GL11.glVertex3f(0.5f, 0f, -0.5f); // V3
		GL11.glTexCoord2d(1f, 0.0f);
		GL11.glVertex3f(0.5f, 0f, 0.5f); // V1
		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

}
