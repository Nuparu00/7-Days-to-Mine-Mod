package nuparu.sevendaystomine.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderStates;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.entity.zombie.ZombieBaseEntity;

public class RedEyesLayer<T extends ZombieBaseEntity, M extends EntityModel<T>, A extends EntityModel<T>>
		extends RenderLayer<T, M> {

	private final RenderType SPIDER_EYES;

	public RedEyesLayer(RenderLayerParent<T, M> p_i50926_1_) {
		this(p_i50926_1_, new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/zombie/eyes/zombie_eyes.png"));
	}

	public RedEyesLayer(RenderLayerParent<T, M> p_i50926_1_, ResourceLocation res) {
		super(p_i50926_1_);
		SPIDER_EYES = RenderStates.eyes(res);
	}

	@Override
	public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
		if (Utils.isBloodmoon(Minecraft.getInstance().level) && p_116986_.isAlive()) {
			VertexConsumer vertexconsumer = p_116984_.getBuffer(this.renderType());
			this.getParentModel().renderToBuffer(p_116983_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public RenderType renderType() {
		return SPIDER_EYES;
	}
}
