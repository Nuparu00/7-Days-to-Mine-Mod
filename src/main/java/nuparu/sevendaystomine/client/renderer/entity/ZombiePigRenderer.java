package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.ZombiePigModel;
import nuparu.sevendaystomine.client.model.entity.ZombieWolfModel;
import nuparu.sevendaystomine.client.renderer.entity.layers.RedEyesLayer;
import nuparu.sevendaystomine.world.entity.zombie.ZombiePigEntity;
import nuparu.sevendaystomine.world.entity.zombie.ZombieWolfEntity;

public class ZombiePigRenderer<T extends ZombiePigEntity, M extends ZombiePigModel<T>>
		extends MobRenderer<T, M> {

	public RedEyesLayer redEyesLayer;

	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/zombie_pig.png");

	public ZombiePigRenderer(EntityRendererProvider.Context context) {
		super(context, (M) new ZombiePigModel(context.bakeLayer(ClientSetup.ZOMBIE_PIG_LAYER)), 0.5F);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this,new ResourceLocation(SevenDaysToMine.MODID,
				"textures/entity/zombie/eyes/zombie_pig_eyes.png")));
	}

	public static LayerDefinition createBodyLayer() {
		return ZombiePigModel.createBodyLayer(CubeDeformation.NONE);
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

}

