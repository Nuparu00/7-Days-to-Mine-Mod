package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Giant;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.world.entity.zombie.FrostbittenWorkerEntity;
import nuparu.sevendaystomine.world.entity.zombie.FrozenLumberjackEntity;
import nuparu.sevendaystomine.world.entity.zombie.FrozenZombieEntity;

public class FrozenLumberjackRenderer<T extends FrozenLumberjackEntity, M extends HumanoidModel<T>>
		extends ZombieBipedRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/frozen_lumberjack.png");
	private static final float SCALE = 1.025f;

	public FrozenLumberjackRenderer(EntityRendererProvider.Context context) {
		super(context, ClientSetup.FROZEN_LUMBERJACK_LAYER,0.5f * SCALE);
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

	protected void scale(T p_114775_, PoseStack p_114776_, float p_114777_) {
		p_114776_.scale(SCALE, SCALE, SCALE);
	}
}
