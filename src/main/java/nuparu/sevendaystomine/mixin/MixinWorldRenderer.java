package nuparu.sevendaystomine.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.capability.BreakData;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(LevelRenderer.class)
public class MixinWorldRenderer {

    private static Field f_storage;
    private static Field f_chunks;
    private static Field f_renderBuffers;
    @Shadow
    private ClientLevel level;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0), method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V")
    private void renderLevel(PoseStack matrixStack, float partialTicks, long nano, boolean outline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Level world = minecraft.level;


        ChunkSource provider = world.getChunkSource();

        if (f_storage == null) {
            f_storage = ObfuscationReflectionHelper.findField(ClientChunkCache.class, "f_104410_");
        }
        try {
            ClientChunkCache.Storage storage = (ClientChunkCache.Storage) f_storage.get(provider);

            if (f_chunks == null) {
                f_chunks = ObfuscationReflectionHelper.findField(ClientChunkCache.Storage.class, "f_104466_");
            }
            AtomicReferenceArray<LevelChunk> chunks = (AtomicReferenceArray<LevelChunk>) f_chunks.get(storage);
            if (f_renderBuffers == null) {
                f_renderBuffers = ObfuscationReflectionHelper.findField(Minecraft.class, "f_90993_");
            }
            RenderBuffers renderBuffers = (RenderBuffers) f_renderBuffers.get(minecraft);


            Vec3 vec = minecraft.gameRenderer.getMainCamera().getPosition();

            double d0 = vec.x();
            double d1 = vec.y();
            double d2 = vec.z();

            for (int i = 0; i < chunks.length(); i++) {
                LevelChunk chunk = chunks.get(i);
                if (chunk == null)
                    continue;
                IChunkData data = CapabilityHelper.getChunkData(chunk);
                if (data == null)
                    continue;
                Iterator<Map.Entry<BlockPos, BreakData>> it = data.getData().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<BlockPos, BreakData> pair = it.next();
                    BlockPos blockpos1 = pair.getKey();
                    double d3 = (double) blockpos1.getX() - d0;
                    double d4 = (double) blockpos1.getY() - d1;
                    double d5 = (double) blockpos1.getZ() - d2;
                    if (!(d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)) {
                        BreakData breakData = pair.getValue();
                        if (breakData != null) {
                            int k3 = Math.min(Math.round(breakData.getState() * 9), 9);

                            matrixStack.pushPose();
                            matrixStack.translate((double) blockpos1.getX() - d0, (double) blockpos1.getY() - d1,
                                    (double) blockpos1.getZ() - d2);
                            RenderSystem.disableDepthTest();
                            if (k3 >= 0) {
                                minecraft.getMainRenderTarget().bindWrite(true);
                                PoseStack.Pose matrixstack$entry1 = matrixStack.last();

                                VertexConsumer ivertexbuilder1 = new SheetedDecalTextureGenerator(
                                        renderBuffers.crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(k3)),
                                        matrixstack$entry1.pose(), matrixstack$entry1.normal());
                                minecraft.getBlockRenderer().renderBreakingTexture(world.getBlockState(blockpos1),
                                        blockpos1, world, matrixStack, ivertexbuilder1);
                            }
                            matrixStack.popPose();
                            renderBuffers.crumblingBufferSource().endBatch();
                            RenderSystem.enableDepthTest();

                        }
                    }
                }

            }

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
