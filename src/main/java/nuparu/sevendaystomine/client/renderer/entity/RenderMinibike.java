package nuparu.sevendaystomine.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelMinibike;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class RenderMinibike extends Render<EntityMinibike> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/minibike.png");
	protected ModelMinibike modelMinibike = new ModelMinibike();

	public RenderMinibike(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0;

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMinibike entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityMinibike entity, double x, double y, double z, float entityYaw, float partialTicks) {
		preRenderCallback(entity, partialTicks);

		GlStateManager.pushMatrix();
		this.setupTranslation(x, y+0.12, z);
		this.setupRotation(entity, entityYaw, partialTicks);
		this.applyRotations(entity, entity.ticksExisted + partialTicks, interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks), partialTicks);
		this.bindEntityTexture(entity);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.modelMinibike.render(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected void preRenderCallback(EntityMinibike entity, float partialTicks) {
		ModelMinibike model = modelMinibike;
		float rotation = entity.wheelAnglePrev + (entity.wheelAngle - entity.wheelAnglePrev) * partialTicks;
		if (rotation != 0) {
			model.setRotation(model.Frontwheel, rotation, 0, 0);
			model.setRotation(model.Backwheel, rotation, 0, 0);
		}
	}

	public void setupTranslation(double p_188309_1_, double p_188309_3_, double p_188309_5_) {
		GlStateManager.translate((float) p_188309_1_, (float) p_188309_3_ + 1.375F, (float) p_188309_5_);
	}

	public void setupRotation(EntityMinibike p_188311_1_, float p_188311_2_, float p_188311_3_) {
		GlStateManager.rotate(180.0F - p_188311_2_, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
	}

	protected void applyRotations(EntityMinibike minibike, float p_77043_2_, float rotationYaw,
			float partialTicks) {
		GL11.glRotated(Utils.lerp(minibike.getTurningPrev(),minibike.getTurning(),partialTicks), 0, 0, 1);

	}
	
	 protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
	    {
	        float f;

	        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F)
	        {
	            ;
	        }

	        while (f >= 180.0F)
	        {
	            f -= 360.0F;
	        }

	        return prevYawOffset + partialTicks * f;
	    }

}
