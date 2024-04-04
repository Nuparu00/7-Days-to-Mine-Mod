package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelGlobe;
import nuparu.sevendaystomine.client.model.ModelGlobePole;
import nuparu.sevendaystomine.tileentity.TileEntityGlobe;

@SideOnly(Side.CLIENT)
public class TileEntityGlobeRenderer extends TileEntitySpecialRenderer<TileEntityGlobe> {

	private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/globe.png");

	private ModelGlobe modelGlobe = new ModelGlobe();
	private ModelGlobePole modelPole = new ModelGlobePole();

	public TileEntityGlobeRenderer() {

	}

	@Override
	public void render(TileEntityGlobe tileentity, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		int s = 0;

		if (tileentity != null) {
			s = tileentity.getBlockMetadata();
		}

		short short1 = 0;
		

		switch(s) {
		case 0 : short1 = 0; break;
		case 1 : short1 = 270; break;
		case 2 : short1 = 180; break;
		case 3 : short1 = 90; break;
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
		/*if (tileentity.getWorld() != null && mc.player != null) {
			if (tileentity.getWorld().getBlockState(new BlockPos((int)(x+MathUtils.lerp(mc.player.prevPosX, mc.player.posX, partialTicks)), (int)(y - 1 +MathUtils.lerp(mc.player.prevPosY, mc.player.posY, partialTicks)), (int)(z + MathUtils.lerp(mc.player.prevPosZ, mc.player.posZ, partialTicks)))).getBlock() instanceof BlockSlab) {
				GL11.glTranslatef(0F, -0.5F, 0F);
			
			}
		}*/
		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		modelPole.render();
		GL11.glTranslatef(0.0F, 0.0F, -0.625F);
		double rot = 0;
		if (tileentity != null) {
			double angle = tileentity.angle;
			double prev = tileentity.anglePrev;

			rot = prev + (angle - prev) * partialTicks;
		}
		GL11.glRotatef(23.5f, 1.0f, 0f, 0f);
		GL11.glRotated(rot, 0.0F, 1.0F, 0.0F);
		modelGlobe.render();

		GL11.glPopMatrix();
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

}
