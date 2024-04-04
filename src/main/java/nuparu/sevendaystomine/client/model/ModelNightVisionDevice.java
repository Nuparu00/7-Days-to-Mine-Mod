package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelNightVisionDevice extends ModelBiped {
	// fields
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape9;
	ModelRenderer Shape10;
	ModelRenderer Shape11;
	ModelRenderer Shape12;
	ModelRenderer Shape13;
	ModelRenderer Shape14;

	public ModelNightVisionDevice() {
		textureWidth = 64;
		textureHeight = 32;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-4F, -5F, -6F, 8, 2, 2);
		Shape1.setRotationPoint(0F, 0F, 0F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 0, 10);
		Shape2.addBox(-3F, -5F, -7F, 2, 2, 1);
		Shape2.setRotationPoint(0F, 0F, 0F);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 5, 10);
		Shape3.addBox(1F, -5F, -7F, 2, 2, 1);
		Shape3.setRotationPoint(0F, 0F, 0F);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 0, 20);
		Shape4.addBox(-2F, -6F, -6F, 4, 1, 2);
		Shape4.setRotationPoint(0F, 0F, 0F);
		Shape4.setTextureSize(64, 32);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 0, 15);
		Shape5.addBox(-0.5F, -6F, -7F, 1, 1, 1);
		Shape5.setRotationPoint(0F, 0F, 0F);
		Shape5.setTextureSize(64, 32);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape6 = new ModelRenderer(this, 30, 0);
		Shape6.addBox(-5F, -4.5F, -5F, 1, 1, 10);
		Shape6.setRotationPoint(0F, 0F, 0F);
		Shape6.setTextureSize(64, 32);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 30, 0);
		Shape7.addBox(4F, -4.5F, -5F, 1, 1, 10);
		Shape7.setRotationPoint(0F, 0F, 0F);
		Shape7.setTextureSize(64, 32);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		Shape8 = new ModelRenderer(this, 30, 15);
		Shape8.addBox(-4F, -4.5F, 4F, 8, 1, 1);
		Shape8.setRotationPoint(0F, 0F, 0F);
		Shape8.setTextureSize(64, 32);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, 0F);
		Shape9 = new ModelRenderer(this, 0, 24);
		Shape9.addBox(-0.5F, -8F, -5F, 1, 2, 1);
		Shape9.setRotationPoint(0F, 0F, 0F);
		Shape9.setTextureSize(64, 32);
		Shape9.mirror = true;
		setRotation(Shape9, 0F, 0F, 0F);
		Shape10 = new ModelRenderer(this, 30, 20);
		Shape10.addBox(-0.5F, -8F, -4F, 1, 1, 8);
		Shape10.setRotationPoint(0F, 0F, 0F);
		Shape10.setTextureSize(64, 32);
		Shape10.mirror = true;
		setRotation(Shape10, 0F, 0F, 0F);
		Shape11 = new ModelRenderer(this, 15, 15);
		Shape11.addBox(-0.5F, -8F, 4F, 1, 4, 1);
		Shape11.setRotationPoint(0F, 0F, 0F);
		Shape11.setTextureSize(64, 32);
		Shape11.mirror = true;
		setRotation(Shape11, 0F, 0F, 0F);
		Shape12 = new ModelRenderer(this, 15, 15);
		Shape12.addBox(-5F, -8F, -0.5F, 1, 4, 1);
		Shape12.setRotationPoint(0F, 0F, 0F);
		Shape12.setTextureSize(64, 32);
		Shape12.mirror = true;
		setRotation(Shape12, 0F, 0F, 0F);
		Shape13 = new ModelRenderer(this, 15, 15);
		Shape13.addBox(4F, -8F, -0.5F, 1, 4, 1);
		Shape13.setRotationPoint(0F, 0F, 0F);
		Shape13.setTextureSize(64, 32);
		Shape13.mirror = true;
		setRotation(Shape13, 0F, 0F, 0F);
		Shape14 = new ModelRenderer(this, 30, 15);
		Shape14.addBox(-4F, -8F, -0.5F, 8, 1, 1);
		Shape14.setRotationPoint(0F, 0F, 0F);
		Shape14.setTextureSize(64, 32);
		Shape14.mirror = true;
		setRotation(Shape14, 0F, 0F, 0F);

		this.Shape1.addChild(Shape2);
		this.Shape1.addChild(Shape3);
		this.Shape1.addChild(Shape4);
		this.Shape1.addChild(Shape5);
		this.Shape1.addChild(Shape6);
		this.Shape1.addChild(Shape7);
		this.Shape1.addChild(Shape8);
		this.Shape1.addChild(Shape9);
		this.Shape1.addChild(Shape10);
		this.Shape1.addChild(Shape11);
		this.Shape1.addChild(Shape12);
		this.Shape1.addChild(Shape13);
		this.Shape1.addChild(Shape14);

		// bipedHead.addChild(Shape1);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		bipedHead.showModel = false;
		bipedHeadwear.showModel = false;
		bipedBody.showModel = false;
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		ModelBase.copyModelAngles(bipedHead, Shape1);
		if (entity.isSneaking()) {
			GlStateManager.translate(0.0f, 0.2f, 0.0f);
		}
		Shape1.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
