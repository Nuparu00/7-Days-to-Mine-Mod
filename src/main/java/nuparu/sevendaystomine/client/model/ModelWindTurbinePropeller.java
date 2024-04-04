package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelWindTurbinePropeller extends ModelBase {
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;

	public ModelWindTurbinePropeller() {
		textureWidth = 64;
		textureHeight = 32;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-2F, -2F, -1F, 4, 4, 2);
		Shape1.setRotationPoint(0F, 0F, -10F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 20, 0);
		Shape2.addBox(-2F, -22F, -1F, 4, 20, 1);
		Shape2.setRotationPoint(0F, 0F, -9.5F);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, -0.1745329F, 0F);
		Shape3 = new ModelRenderer(this, 20, 0);
		Shape3.addBox(-2F, 2F, -1F, 4, 20, 1);
		Shape3.setRotationPoint(0F, 0F, -9.5F);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0.1745329F, 0F);
		Shape4 = new ModelRenderer(this, 0, 27);
		Shape4.addBox(2F, -2F, -1F, 20, 4, 1);
		Shape4.setRotationPoint(0F, 0F, -9.5F);
		Shape4.setTextureSize(64, 32);
		Shape4.mirror = true;
		setRotation(Shape4, 0.1745329F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 0, 27);
		Shape5.addBox(-22F, -2F, -1F, 20, 4, 1);
		Shape5.setRotationPoint(0F, 0F, -9.5F);
		Shape5.setTextureSize(64, 32);
		Shape5.mirror = true;
		setRotation(Shape5, -0.1745329F, 0F, 0F);
	}

	public void render() {
		Shape1.render(0.0625F);
		Shape2.render(0.0625F);
		Shape3.render(0.0625F);
		Shape4.render(0.0625F);
		Shape5.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
