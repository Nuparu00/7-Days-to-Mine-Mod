package nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelAirplaneRotorBase;
import nuparu.sevendaystomine.client.model.ModelAirplaneRotorTurbine;
import nuparu.sevendaystomine.tileentity.TileEntityAirplaneRotor;

@SideOnly(Side.CLIENT)
public class TileEntityAirplaneRotorRenderer extends TileEntitySpecialRenderer<TileEntityAirplaneRotor> {
	private static final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/airplanerotor.png");
	private static final ResourceLocation TEX_TURBINE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/airplanerotor_turbine.png");
	private ModelAirplaneRotorBase rotor = new ModelAirplaneRotorBase();
	private ModelAirplaneRotorTurbine turbine = new ModelAirplaneRotorTurbine();

	public TileEntityAirplaneRotorRenderer() {

	}

	@Override
	public void render(TileEntityAirplaneRotor tileentity, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);

		World world = null;
		if (tileentity != null) {
			tileentity.getWorld();
		}
		Block bl = Blocks.AIR;
		if (tileentity != null) {
			bl = tileentity.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
		}
		renderBlock(tileentity, world, x,y,z, bl, partialTicks);
		GL11.glPopMatrix();
	}

	public void renderBlock(TileEntityAirplaneRotor te, World world, double i, double j, double k, Block block,
			float partial) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder wr = tessellator.getBuffer();
		BlockPos pos = new BlockPos(i, j, k);
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
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);

		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);

		bindTexture(TEX);

		rotor.render();
		float rot = 0f;
		if (te != null) {
			float angle = te.angle;
			float prev = te.anglePrev;

			if (prev > angle) {
				prev -= 360f;
			}
			rot = prev + (angle - prev) * partial;
		}

		this.bindTexture(TEX_TURBINE);
		GL11.glTranslatef(0.0F, 0.875F, -0.5F - (4 * 0.0625F));
		if (rot != 0f) {
			GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
		}
		turbine.render();

		GL11.glPopMatrix();

	}
	
	
}
