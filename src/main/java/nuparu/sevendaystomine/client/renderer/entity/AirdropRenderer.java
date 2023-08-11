package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.AirdropModel;
import nuparu.sevendaystomine.world.entity.item.AirdropEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AirdropRenderer<T extends AirdropEntity> extends EntityRenderer<T> {
	private static final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/airdrop.png");
	public final AirdropModel model;

	public AirdropRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		model = new AirdropModel(manager.bakeLayer(ClientSetup.AIRDROP_MODEL));
	}

	@Override
	public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack matrix,
                       @NotNull MultiBufferSource buffer, int p_225623_6_) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, p_225623_6_);
		Minecraft minecraft = Minecraft.getInstance();

		boolean flag = !entity.isInvisible();
		boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
		boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
		RenderType rendertype = this.getRenderType(entity, flag, flag1, flag2);
		matrix.pushPose();
		//matrix.scale(1,-1,1);
		matrix.translate(0,1.5,0);
		matrix.mulPose(Axis.XP.rotationDegrees(180));
		if (rendertype != null) {
			VertexConsumer ivertexbuilder = buffer.getBuffer(rendertype);
			int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
			model.setupAnim(entity,0,0,0,0,0);
			model.renderToBuffer(matrix, ivertexbuilder, p_225623_6_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
		matrix.popPose();
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T p_114482_) {
		return TEX;
	}

	@Nullable
	protected RenderType getRenderType(T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = this.getTextureLocation(p_230496_1_);
		if (p_230496_3_) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return model.renderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.outline(resourcelocation) : null;
		}
	}

	public static int getOverlayCoords(Entity p_229117_0_, float p_229117_1_) {
		return OverlayTexture.pack(OverlayTexture.u(p_229117_1_), OverlayTexture.v(false));
	}
	protected float getWhiteOverlayProgress(T p_225625_1_, float p_225625_2_) {
		return 0.0F;
	}

}
