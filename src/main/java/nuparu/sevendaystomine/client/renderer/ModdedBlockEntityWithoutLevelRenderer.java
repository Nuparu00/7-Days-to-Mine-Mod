package nuparu.sevendaystomine.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.BoneSpearModel;
import nuparu.sevendaystomine.client.model.entity.StoneSpearModel;
import nuparu.sevendaystomine.init.ModItems;
import org.jetbrains.annotations.NotNull;

public class ModdedBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

    public StoneSpearModel stoneSpearModel;
    public BoneSpearModel boneSpearModel;
    private final EntityModelSet entityModelSet;

    public ModdedBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet entityModelSet) {
        super(dispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.stoneSpearModel = new StoneSpearModel(this.entityModelSet.bakeLayer(ClientSetup.STONE_SPEAR_LAYER));
        this.boneSpearModel = new BoneSpearModel(this.entityModelSet.bakeLayer(ClientSetup.BONE_SPEAR_LAYER));
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager p_172555_) {
        super.onResourceManagerReload(p_172555_);
        this.stoneSpearModel = new StoneSpearModel(this.entityModelSet.bakeLayer(ClientSetup.STONE_SPEAR_LAYER));
        this.boneSpearModel = new BoneSpearModel(this.entityModelSet.bakeLayer(ClientSetup.BONE_SPEAR_LAYER));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int p_108834_, int p_108835_) {
        Item item = stack.getItem();
        if(item == ModItems.STONE_SPEAR.get()){
            poseStack.pushPose();
            poseStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(bufferSource, this.stoneSpearModel.renderType(StoneSpearModel.TEXTURE), false, stack.hasFoil());
            this.stoneSpearModel.renderToBuffer(poseStack, vertexconsumer1, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            return;
        }
        if(item == ModItems.BONE_SPEAR.get()){
            poseStack.pushPose();
            poseStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(bufferSource, this.boneSpearModel.renderType(BoneSpearModel.TEXTURE), false, stack.hasFoil());
            this.boneSpearModel.renderToBuffer(poseStack, vertexconsumer1, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            return;
        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, p_108834_, p_108835_);
    }
}
