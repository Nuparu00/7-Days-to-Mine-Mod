package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.world.entity.zombie.FrigidHunterEntity;
import nuparu.sevendaystomine.world.entity.zombie.PlaguedNurseEntity;

public class PlaguedNurseRenderer<T extends PlaguedNurseEntity, M extends HumanoidModel<T>>
		extends ZombieBipedRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/plagued_nurse.png");

	public PlaguedNurseRenderer(EntityRendererProvider.Context context) {
		super(context, ClientSetup.FRIGID_HUNTER_LAYER);
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}
}
