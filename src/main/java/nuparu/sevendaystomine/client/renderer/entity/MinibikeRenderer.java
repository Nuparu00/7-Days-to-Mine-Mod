package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.MinibikeModel;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.entity.item.MinibikeEntity;
import org.jetbrains.annotations.NotNull;

public class MinibikeRenderer<T extends MinibikeEntity, M extends MinibikeModel<T>> extends LivingEntityRenderer<T, M> {
	private static final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/minibike.png");

	public MinibikeRenderer(EntityRendererProvider.Context manager) {
		super(manager, (M) new MinibikeModel<T>(manager.bakeLayer(ClientSetup.MINIBIKE_MODEL)), 0.6f);
	}

	@Override
	protected void setupRotations(@NotNull T minibike, @NotNull PoseStack matrixStack, float p_225621_3_, float p_225621_4_, float partialTicks) {
		super.setupRotations(minibike, matrixStack, p_225621_3_, p_225621_4_, partialTicks);
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathUtils.lerp(partialTicks,minibike.getTurningPrev(), minibike.getTurning())));
	}
	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T p_114482_) {
		return TEX;
	}

	@Override
	protected boolean shouldShowName(@NotNull T minibike) {
		return false;
	}

}
