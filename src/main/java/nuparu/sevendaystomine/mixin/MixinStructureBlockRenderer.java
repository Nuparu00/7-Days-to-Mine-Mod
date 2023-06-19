package nuparu.sevendaystomine.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.StructureBlockRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.properties.StructureMode;
import nuparu.sevendaystomine.client.util.ClientUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlockRenderer.class)
public abstract class MixinStructureBlockRenderer {

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/StructureBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"))
    public void render(StructureBlockEntity te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, CallbackInfo ci) {
        if (te.getMode() == StructureMode.DATA) {
            MutableComponent component = Component.literal(te.getMetaData());
            component.setStyle(component.getStyle().withColor(ChatFormatting.GREEN));
            Player player = Minecraft.getInstance().player;
            double maxDst = player != null && player.isCreative() && player.getMainHandItem().is(Blocks.STRUCTURE_BLOCK.asItem()) ? 256 : 0;
            if(maxDst >= 1) {
                ClientUtils.renderNameTag(te.getBlockPos(), component, poseStack, buffer, 0xffffff, maxDst);
            }
        }
    }
}
