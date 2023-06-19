package nuparu.sevendaystomine.client.renderer.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.PhotoRequestMessage;
import nuparu.sevendaystomine.world.level.block.CalendarBlock;
import nuparu.sevendaystomine.world.level.block.PhotoBlock;
import nuparu.sevendaystomine.world.level.block.entity.PhotoBlockEntity;
import org.jetbrains.annotations.NotNull;

public class PhotoRenderer implements BlockEntityRenderer<PhotoBlockEntity> {


    //Colored textureless quad with lighting seems having trouble with rendering so we will just use a 1x1 white texture and pretend everything is fine
    private static final ResourceLocation WHITE_1X1 = new ResourceLocation(SevenDaysToMine.MODID,"textures/misc/1x1.png");
    public PhotoRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PhotoBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.PHOTO.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(CalendarBlock.FACING) ? blockstate.getValue(CalendarBlock.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof PhotoBlock) {

            String path = te.getPath();
            if (path == null || path.isEmpty())
                return;
            if (te.image == null) {
                if (System.currentTimeMillis() >= te.nextUpdate) {
                    te.image = ResourcesHelper.INSTANCE.getImage(path);
                    if (te.image == null) {
                        PacketManager.photoRequest.sendToServer(new PhotoRequestMessage(path));
                        te.image = ResourcesHelper.INSTANCE.tryToGetImage(path);
                    }
                    te.nextUpdate = System.currentTimeMillis() + 2000;
                }
                return;
            }

            int shape = Integer.compare(te.image.height(), te.image.width());

            float w = 16;
            float h = 16;

            //Controls the shape of the photo - portrait vs landscape
            if (shape < 0) {
                w = w * 0.75f;
                h = ((float) te.image.height() / (float) te.image.width()) * w;
            } else if (shape == 0) {
                h = h * 0.75f;
                w = h;
            } else {
                h = h * 0.75f;
                w = ((float) te.image.width() / (float) te.image.height()) * h;
            }

            float f = direction.toYRot();

            matrixStack.pushPose();

            matrixStack.translate(0.5D, 0.5+h/32d, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
            matrixStack.translate(-(w/32d), 0, 0.49);
            matrixStack.scale(0.0625f, 0.0625f, 0.0625f);
            RenderSystem.enableDepthTest();
            drawTexturedRect(matrixStack, WHITE_1X1,buffer, -1,-1,0,0,w+2,h+2,1,1,1,0.1f,combinedLight,combinedOverlay);
            drawTexturedRect(matrixStack, te.image.res(),buffer, 0,0,0,0,w,h,w,h,1,0,combinedLight,combinedOverlay);
            RenderSystem.disableDepthTest();
            matrixStack.popPose();
        }
    }

    public static void drawTexturedRect(PoseStack matrix, ResourceLocation texture, MultiBufferSource buffer, float x, float y, int u, int v, float width,
                                        float height, float imageWidth, float imageHeight, float scale, float zLevel, int packedLight, int overlay) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        float minU = (float) u / imageWidth;
        float maxU = (u + width) / imageWidth;
        float minV = (float) v / imageHeight;
        float maxV = (v + height) / imageHeight;
        matrix.pushPose();
        Matrix4f pose = matrix.last().pose();

        Matrix3f normal = matrix.last().normal();

        float nx = x;
        float ny = y;
        float nz = zLevel;
        float magnitude = Mth.sqrt(x * x + y * y + zLevel * zLevel);
        nx /= magnitude;
        ny /= magnitude;
        nz /= magnitude;

        VertexConsumer vertexBuilder = buffer.getBuffer((RenderType.entityCutout(texture)));

        vertexBuilder.vertex(pose, x + scale * width, y + scale * height, zLevel).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        vertexBuilder.vertex(pose, x + scale * width, y, zLevel).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        vertexBuilder.vertex(pose, x, y, zLevel).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        vertexBuilder.vertex(pose, x, y + scale * height, zLevel).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        matrix.popPose();
    }


    public static void blitColoredLight(PoseStack stack, MultiBufferSource buffer, float p_93114_, float p_93115_, float p_93116_, float p_93117_, float p_93118_, int color, int packedLight) {
        Matrix4f matrix4f = stack.last().pose();

        int r = 255;
        int g = 255;
        int b = 255;

        Matrix3f normal = stack.last().normal();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(""));
        VertexConsumer vertexBuilder = buffer.getBuffer((RenderType.entityCutout(new ResourceLocation(""))));
        vertexBuilder.vertex(matrix4f, p_93114_, p_93117_, p_93118_).color(r,g,b,255).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        vertexBuilder.vertex(matrix4f, p_93115_, p_93117_, p_93118_).color(r,g,b,255).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        vertexBuilder.vertex(matrix4f, p_93115_, p_93116_, p_93118_).color(r,g,b,255).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
        vertexBuilder.vertex(matrix4f, p_93114_, p_93116_, p_93118_).color(r,g,b,255).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal,0,0,1).endVertex();
    }

}
