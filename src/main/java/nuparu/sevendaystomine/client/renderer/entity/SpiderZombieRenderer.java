package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.SpiderZombieModel;
import nuparu.sevendaystomine.client.model.entity.TwistedZombieModel;
import nuparu.sevendaystomine.client.renderer.entity.layers.RedEyesLayer;
import nuparu.sevendaystomine.world.entity.zombie.SpiderZombieEntity;
import nuparu.sevendaystomine.world.entity.zombie.TwistedZombieEntity;

public class SpiderZombieRenderer<T extends SpiderZombieEntity, M extends SpiderZombieModel<T>>
		extends MobRenderer<T, M> {

	public RedEyesLayer redEyesLayer;

	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/spider_zombie.png");

	public SpiderZombieRenderer(EntityRendererProvider.Context context) {
		super(context, (M) new SpiderZombieModel(context.bakeLayer(ClientSetup.SPIDER_ZOMBIE_LAYER)), 0.5F);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this,new ResourceLocation(SevenDaysToMine.MODID,
				"textures/entity/zombie/eyes/spider_zombie_eyes.png")));
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

}

