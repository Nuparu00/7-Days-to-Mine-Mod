package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class TextModelPart extends ModelPart {

    String text;

    double xPos;
    double yPos;
    double zPos;

    float xRot;
    float yRot;
    float zRot;

    public float scale = 0.005f;

    public TextModelPart(double xPos, double yPos, double zPos,float xRot, float yRot, float zRot) {
        super(new ArrayList<>(), new HashMap<String, ModelPart>());
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
    }

    @Override
    public void render(@NotNull PoseStack matrixStack, @NotNull VertexConsumer p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (this.visible) {
            matrixStack.pushPose();
            this.translateAndRotate(matrixStack);
                /*this.compile(p_228309_1_.last(), p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);

                for(ModelRenderer modelrenderer : this.children) {
                    modelrenderer.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
                }*/
            matrixStack.translate(0.35,0,0);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(yRot));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(zRot));
            matrixStack.translate(xPos,yPos,zPos);
            matrixStack.scale(scale,scale,scale);
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.font.draw(matrixStack, text, 0, 0, 0x000000);

            matrixStack.popPose();

        }
    }

    public void setText(String text){
        this.text = text;
    }
}
