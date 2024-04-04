package nuparu.sevendaystomine.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelAirdrop;
import nuparu.sevendaystomine.entity.EntityAirdrop;

@SideOnly(Side.CLIENT)
public class RenderAirdrop extends Render<EntityAirdrop> {
	private static final ResourceLocation tex = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/airdrop.png");
	public static final ModelAirdrop model = new ModelAirdrop();

	public RenderAirdrop(RenderManager renderManager) {
		super(renderManager);
		shadowSize = 1F;
	}

	@Override
	public void doRender(EntityAirdrop entity, double d, double d1, double d2, float f, float f1) {
		bindTexture(getEntityTexture(entity));
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1 + 1.5f, (float) d2);
		GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);

		GL11.glRotatef(90F - entity.prevRotationPitch - (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F,
				1.0F, 0.0F);

		if (model != null)
			model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625f);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAirdrop entity) {
		return tex;
	}
}