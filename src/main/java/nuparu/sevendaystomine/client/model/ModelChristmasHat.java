package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelChristmasHat extends ModelBiped {

	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;

	public ModelChristmasHat() {
		textureWidth = 64;
		textureHeight = 32;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-5F, -8F, -5F, 10, 2, 10);
		Shape1.setRotationPoint(0F, 0F, 0F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 0, 13);
		Shape2.addBox(-4F, -10F, -4F, 8, 2, 8);
		Shape2.setRotationPoint(0F, 0F, 0F);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 23);
		Shape3.addBox(-2.5F, -12F, -3F, 6, 3, 6);
		Shape3.setRotationPoint(0F, 0F, 0F);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, -0.0981748F);
		Shape4 = new ModelRenderer(this, 42, 0);
		Shape4.addBox(-0.5F, -14F, -2F, 4, 3, 4);
		Shape4.setRotationPoint(0F, 0F, 0F);
		Shape4.setTextureSize(64, 32);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, -0.1963495F);
		Shape5 = new ModelRenderer(this, 43, 17);
		Shape5.addBox(4.5F, -14F, -1.5F, 3, 2, 3);
		Shape5.setRotationPoint(0F, 0F, 0F);
		Shape5.setTextureSize(64, 32);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, -0.5890486F);
		Shape6 = new ModelRenderer(this, 45, 23);
		Shape6.addBox(-14F, -9F, -1F, 2, 4, 2);
		Shape6.setRotationPoint(0F, 0F, 0F);
		Shape6.setTextureSize(64, 32);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0.7853982F);
		Shape7 = new ModelRenderer(this, 33, 18);
		Shape7.addBox(-5F, 3F, -0.5F, 1, 3, 1);
		Shape7.setRotationPoint(0F, -15F, 0F);
		Shape7.setTextureSize(64, 32);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0.3926991F);
		Shape8 = new ModelRenderer(this, 32, 26);
		Shape8.addBox(-11.25F, -8F, -1F, 2, 2, 2);
		Shape8.setRotationPoint(0F, 0F, 0F);
		Shape8.setTextureSize(64, 32);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, 0.3926991F);

		this.Shape1.addChild(Shape2);
		this.Shape1.addChild(Shape3);
		this.Shape1.addChild(Shape4);
		this.Shape1.addChild(Shape5);
		this.Shape1.addChild(Shape6);
		this.Shape1.addChild(Shape7);
		this.Shape1.addChild(Shape8);
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
