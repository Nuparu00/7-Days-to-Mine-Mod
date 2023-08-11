package nuparu.sevendaystomine.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ClientUtils {
    /*
    Never, ever use on server side!
     */
    public static String localize(String unlocalized, Object... args) {
        return I18n.get(unlocalized, args);
    }

    public static void drawTexturedRect(PoseStack matrix, ResourceLocation texture, float x, float y, int u, int v, float width,
                                        float height, float imageWidth, float imageHeight, float scale, float zLevel) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);

        float minU = (float) u / imageWidth;
        float maxU = (u + width) / imageWidth;
        float minV = (float) v / imageHeight;
        float maxV = (v + height) / imageHeight;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f pose = matrix.last().pose();
        bufferbuilder.vertex(pose, x + scale * width, y + scale * height, zLevel).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(pose, x + scale * width, y, zLevel).uv(maxU, minV).endVertex();
        bufferbuilder.vertex(pose, x, y, zLevel).uv(minU, minV).endVertex();
        bufferbuilder.vertex(pose, x, y + scale * height, zLevel).uv(minU, maxV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
        return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
    }

    public static void renderNameTag(BlockPos pos, Component p_225629_2_, PoseStack p_225629_3_, MultiBufferSource p_225629_4_, int p_225629_5_, double maxDst) {
        Minecraft minecraft = Minecraft.getInstance();
        double d0 = minecraft.getEntityRenderDispatcher().distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
        if (d0 <= maxDst) {
            float f = 1 + 0.5F;
            p_225629_3_.pushPose();
            p_225629_3_.translate(0.5D, f, 0.5D);
            p_225629_3_.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());
            p_225629_3_.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = p_225629_3_.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(1);
            int j = (int) (f1 * 255.0F) << 24;
            Font fontrenderer = minecraft.font;
            float f2 = (float) (-fontrenderer.width(p_225629_2_) / 2);
            fontrenderer.drawInBatch(p_225629_2_, f2, 0, 0xffffff, false, matrix4f, p_225629_4_, Font.DisplayMode.NORMAL, j, p_225629_5_);
            //fontrenderer.drawInBatch(p_225629_2_, f2, (float) 0, -1, false, matrix4f, p_225629_4_, false, 0, p_225629_5_);

            p_225629_3_.popPose();
        }
    }
}
