package nuparu.sevendaystomine.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelChlorineGrenade;
import nuparu.sevendaystomine.entity.EntityChlorineGrenade;

@SideOnly(Side.CLIENT)
public class RenderChlorineGreande extends Render<EntityChlorineGrenade>{
	private static final ResourceLocation tex = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/chlorine_grenade.png");
	ModelChlorineGrenade chlorineGrenade = new ModelChlorineGrenade();
	
	public RenderChlorineGreande(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0f;
	}

	@Override
	public void doRender(EntityChlorineGrenade entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		bindTexture(getEntityTexture(entity));
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y+0.4f, (float) z);
		GL11.glRotatef(entityYaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(90F - entity.prevRotationPitch - (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F,
				1.0F, 0.0F);

		GL11.glScalef(0.25f,0.25f,0.25f);
		if (chlorineGrenade != null)
			chlorineGrenade.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625f);
		GL11.glPopMatrix();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityChlorineGrenade entity) {
		return tex;
	}
}
