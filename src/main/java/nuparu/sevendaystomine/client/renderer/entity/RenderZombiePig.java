package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityZombiePig;

@SideOnly(Side.CLIENT)
public class RenderZombiePig extends RenderCorpse<EntityZombiePig> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/zombie_pig.png");

	public RenderZombiePig(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		layerRenderers.removeIf(r -> r instanceof LayerZombieEyes);
	}

	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityZombiePig entity) {
		return TEXTURE;
	}

	protected float handleRotationFloat(EntityZombiePig livingBase, float partialTicks) {
		return livingBase.getTailRotation();
	}

}
