package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.renderer.entity.layers.RedEyesLayer;
import nuparu.sevendaystomine.client.model.entity.CrawlerZombieModel;
import nuparu.sevendaystomine.world.entity.zombie.CrawlerZombieEntity;
import org.jetbrains.annotations.NotNull;

public class CrawlerZombieRenderer<T extends CrawlerZombieEntity, M extends CrawlerZombieModel<T>>
		extends MobRenderer<T, M> {

	public final RedEyesLayer redEyesLayer;

	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/zombie_crawler.png");

	public CrawlerZombieRenderer(EntityRendererProvider.Context context) {
		super(context, (M) new CrawlerZombieModel(context.bakeLayer(ClientSetup.CRAWLER_ZOMBIE_LAYER)), 0.5F);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this,new ResourceLocation(SevenDaysToMine.MODID,
				"textures/entity/zombie/eyes/zombie_crawler_eyes.png")));
	}

	protected boolean isShaking(@NotNull T p_230495_1_) {
		return false;
	}

	public @NotNull ResourceLocation getTextureLocation(@NotNull T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

}
