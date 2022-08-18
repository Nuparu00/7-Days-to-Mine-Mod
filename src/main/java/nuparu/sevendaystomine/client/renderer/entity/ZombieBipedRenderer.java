package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.GiantZombieModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import nuparu.sevendaystomine.client.renderer.entity.layers.RedEyesLayer;
import nuparu.sevendaystomine.world.entity.zombie.ZombieBipedEntity;

public abstract class ZombieBipedRenderer<T extends ZombieBipedEntity, M extends HumanoidModel<T>>
		extends HumanoidMobRenderer<T, M> {

	public RedEyesLayer redEyesLayer;
	
	public ZombieBipedRenderer(EntityRendererProvider.Context manager, ModelLayerLocation modelLayerLocation) {
		this(manager,modelLayerLocation,0.5f);
	}

	public ZombieBipedRenderer(EntityRendererProvider.Context manager, ModelLayerLocation modelLayerLocation, float shadow) {
		super(manager, (M) new HumanoidModel(manager.bakeLayer(modelLayerLocation)), shadow);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this));
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel(manager.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(manager.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));

	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

}
