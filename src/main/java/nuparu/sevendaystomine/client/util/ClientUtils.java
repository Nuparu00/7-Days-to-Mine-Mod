package nuparu.sevendaystomine.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
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

}
