package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * WallClock - Nuparu00
 * Created using Tabula 7.0.1
 */

@SideOnly(Side.CLIENT)
public class ModelWallClock extends ModelBase {
    public ModelRenderer Hours;
    public ModelRenderer FrameRight;
    public ModelRenderer FrameTop;
    public ModelRenderer FrameLeft;
    public ModelRenderer FrameBottom;
    public ModelRenderer Base;
    public ModelRenderer Center;
    public ModelRenderer Minutes;
    public ModelRenderer Center_1;

    public ModelWallClock() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Minutes = new ModelRenderer(this, 0, 0);
        this.Minutes.setRotationPoint(0.0F, 16.0F, 6.8F);
        this.Minutes.addBox(-0.5F, -0.4F, -2.0F, 1, 7, 1, 0.0F);
        this.FrameRight = new ModelRenderer(this, 0, 1);
        this.FrameRight.setRotationPoint(4.0F, 11.0F, 6.5F);
        this.FrameRight.addBox(0.0F, 0.0F, 0.0F, 1, 10, 3, 0.0F);
        this.Base = new ModelRenderer(this, 42, 0);
        this.Base.setRotationPoint(-4.0F, 12.0F, 7.0F);
        this.Base.addBox(0.0F, 0.0F, 0.0F, 8, 8, 1, 0.0F);
        this.FrameBottom = new ModelRenderer(this, 0, 0);
        this.FrameBottom.setRotationPoint(-4.0F, 20.0F, 6.5F);
        this.FrameBottom.addBox(0.0F, 0.0F, 0.0F, 8, 1, 3, 0.0F);
        this.Center = new ModelRenderer(this, 0, 0);
        this.Center.setRotationPoint(0.0F, 16.0F, 6.8F);
        this.Center.addBox(-0.5F, -0.4F, -1.0F, 1, 1, 1, 0.0F);
        this.Hours = new ModelRenderer(this, 0, 0);
        this.Hours.setRotationPoint(0.0F, 16.0F, 6.8F);
        this.Hours.addBox(-0.5F, -0.4F, -1.0F, 1, 7, 1, 0.0F);
        this.FrameTop = new ModelRenderer(this, 12, 0);
        this.FrameTop.setRotationPoint(-4.0F, 11.0F, 6.5F);
        this.FrameTop.addBox(0.0F, 0.0F, 0.0F, 8, 1, 3, 0.0F);
        this.Center_1 = new ModelRenderer(this, 0, 0);
        this.Center_1.setRotationPoint(-0.5F, 15.5F, 6.8F);
        this.Center_1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.FrameLeft = new ModelRenderer(this, 0, 1);
        this.FrameLeft.setRotationPoint(-5.0F, 11.0F, 6.5F);
        this.FrameLeft.addBox(0.0F, 0.0F, 0.0F, 1, 10, 3, 0.0F);
    }

    public void render(float scale) { 
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.Minutes.offsetX, this.Minutes.offsetY, this.Minutes.offsetZ);
        GlStateManager.translate(this.Minutes.rotationPointX * scale, this.Minutes.rotationPointY * scale, this.Minutes.rotationPointZ * scale);
        GlStateManager.scale(0.5D, 0.55D, 0.1D);
        GlStateManager.translate(-this.Minutes.offsetX, -this.Minutes.offsetY, -this.Minutes.offsetZ);
        GlStateManager.translate(-this.Minutes.rotationPointX * scale, -this.Minutes.rotationPointY * scale, -this.Minutes.rotationPointZ * scale);
        this.Minutes.render(scale);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.FrameRight.offsetX, this.FrameRight.offsetY, this.FrameRight.offsetZ);
        GlStateManager.translate(this.FrameRight.rotationPointX * scale, this.FrameRight.rotationPointY * scale, this.FrameRight.rotationPointZ * scale);
        GlStateManager.scale(1.0D, 1.0D, 0.5D);
        GlStateManager.translate(-this.FrameRight.offsetX, -this.FrameRight.offsetY, -this.FrameRight.offsetZ);
        GlStateManager.translate(-this.FrameRight.rotationPointX * scale, -this.FrameRight.rotationPointY * scale, -this.FrameRight.rotationPointZ * scale);
        this.FrameRight.render(scale);
        GlStateManager.popMatrix();
        this.Base.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.FrameBottom.offsetX, this.FrameBottom.offsetY, this.FrameBottom.offsetZ);
        GlStateManager.translate(this.FrameBottom.rotationPointX * scale, this.FrameBottom.rotationPointY * scale, this.FrameBottom.rotationPointZ * scale);
        GlStateManager.scale(1.0D, 1.0D, 0.5D);
        GlStateManager.translate(-this.FrameBottom.offsetX, -this.FrameBottom.offsetY, -this.FrameBottom.offsetZ);
        GlStateManager.translate(-this.FrameBottom.rotationPointX * scale, -this.FrameBottom.rotationPointY * scale, -this.FrameBottom.rotationPointZ * scale);
        this.FrameBottom.render(scale);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.Center.offsetX, this.Center.offsetY, this.Center.offsetZ);
        GlStateManager.translate(this.Center.rotationPointX * scale, this.Center.rotationPointY * scale, this.Center.rotationPointZ * scale);
        GlStateManager.scale(0.8D, 0.8D, 0.3D);
        GlStateManager.translate(-this.Center.offsetX, -this.Center.offsetY, -this.Center.offsetZ);
        GlStateManager.translate(-this.Center.rotationPointX * scale, -this.Center.rotationPointY * scale, -this.Center.rotationPointZ * scale);
        this.Center.render(scale);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.Hours.offsetX, this.Hours.offsetY, this.Hours.offsetZ);
        GlStateManager.translate(this.Hours.rotationPointX * scale, this.Hours.rotationPointY * scale, this.Hours.rotationPointZ * scale);
        GlStateManager.scale(0.5D, 0.5D, 0.1D);
        GlStateManager.translate(-this.Hours.offsetX, -this.Hours.offsetY, -this.Hours.offsetZ);
        GlStateManager.translate(-this.Hours.rotationPointX * scale, -this.Hours.rotationPointY * scale, -this.Hours.rotationPointZ * scale);
        this.Hours.render(scale);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.FrameTop.offsetX, this.FrameTop.offsetY, this.FrameTop.offsetZ);
        GlStateManager.translate(this.FrameTop.rotationPointX * scale, this.FrameTop.rotationPointY * scale, this.FrameTop.rotationPointZ * scale);
        GlStateManager.scale(1.0D, 1.0D, 0.5D);
        GlStateManager.translate(-this.FrameTop.offsetX, -this.FrameTop.offsetY, -this.FrameTop.offsetZ);
        GlStateManager.translate(-this.FrameTop.rotationPointX * scale, -this.FrameTop.rotationPointY * scale, -this.FrameTop.rotationPointZ * scale);
        this.FrameTop.render(scale);
        GlStateManager.popMatrix();
        this.Center_1.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.FrameLeft.offsetX, this.FrameLeft.offsetY, this.FrameLeft.offsetZ);
        GlStateManager.translate(this.FrameLeft.rotationPointX * scale, this.FrameLeft.rotationPointY * scale, this.FrameLeft.rotationPointZ * scale);
        GlStateManager.scale(1.0D, 1.0D, 0.5D);
        GlStateManager.translate(-this.FrameLeft.offsetX, -this.FrameLeft.offsetY, -this.FrameLeft.offsetZ);
        GlStateManager.translate(-this.FrameLeft.rotationPointX * scale, -this.FrameLeft.rotationPointY * scale, -this.FrameLeft.rotationPointZ * scale);
        this.FrameLeft.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
