package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityZombieWolf;

@SideOnly(Side.CLIENT)
public class RenderZombieWolf extends RenderCorpse<EntityZombieWolf> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/zombie_wolf.png");

	public RenderZombieWolf(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		layerRenderers.removeIf(r -> r instanceof LayerZombieEyes);
	}

	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityZombieWolf entity) {
		return TEXTURE;
	}

	protected float handleRotationFloat(EntityZombieWolf livingBase, float partialTicks) {
		return livingBase.getTailRotation();
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityZombieWolf entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.isWolfWet()) {
			float f = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
			GlStateManager.color(f, f, f);
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
