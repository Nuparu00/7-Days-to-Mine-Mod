package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.BoneSpearModel;
import nuparu.sevendaystomine.world.entity.item.BoneSpearEntity;
import org.jetbrains.annotations.NotNull;

public class BoneSpearRenderer extends EntityRenderer<BoneSpearEntity> {
    private final BoneSpearModel model;

    public BoneSpearRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.model = new BoneSpearModel(p_174008_.bakeLayer(ClientSetup.STONE_SPEAR_LAYER));
    }

    public void render(BoneSpearEntity p_116111_, float p_116112_, float p_116113_, PoseStack p_116114_, @NotNull MultiBufferSource p_116115_, int p_116116_) {
        p_116114_.pushPose();
        p_116114_.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.yRotO, p_116111_.getYRot()) - 90.0F));
        p_116114_.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.xRotO, p_116111_.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, p_116111_.isFoil());
        this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_116114_.popPose();
        super.render(p_116111_, p_116112_, p_116113_, p_116114_, p_116115_, p_116116_);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull BoneSpearEntity p_116109_) {
        return BoneSpearModel.TEXTURE;
    }
}