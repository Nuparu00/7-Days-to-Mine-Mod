package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAirplane extends ModelBase {
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;

	public ModelAirplane() {
		textureWidth = 256;
		textureHeight = 128;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(0F, 0F, 0F, 12, 12, 50);
		Shape1.setRotationPoint(-6F, 12F, -16F);
		Shape1.setTextureSize(256, 128);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 128, 0);
		Shape2.addBox(0F, 0F, 0F, 10, 10, 5);
		Shape2.setRotationPoint(-5F, 13F, -21F);
		Shape2.setTextureSize(256, 128);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 128, 20);
		Shape3.addBox(0F, 0F, 0F, 10, 8, 3);
		Shape3.setRotationPoint(-5F, 15F, -24F);
		Shape3.setTextureSize(256, 128);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 0, 87);
		Shape4.addBox(0F, 0F, 0F, 35, 3, 18);
		Shape4.setRotationPoint(6F, 17F, -7F);
		Shape4.setTextureSize(256, 128);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 0, 64);
		Shape5.addBox(-35F, 0F, 0F, 35, 3, 18);
		Shape5.setRotationPoint(-6F, 17F, -7F);
		Shape5.setTextureSize(256, 128);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape6 = new ModelRenderer(this, 127, 80);
		Shape6.addBox(0F, 0F, 0F, 8, 8, 22);
		Shape6.setRotationPoint(25F, 14F, -8F);
		Shape6.setTextureSize(256, 128);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 127, 40);
		Shape7.addBox(-8F, 0F, 0F, 8, 8, 22);
		Shape7.setRotationPoint(-25F, 14F, -8F);
		Shape7.setTextureSize(256, 128);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		Shape8 = new ModelRenderer(this, 160, 0);
		Shape8.addBox(0F, 0F, 0F, 10, 12, 18);
		Shape8.setRotationPoint(-5F, 12F, 24F);
		Shape8.setTextureSize(256, 128);
		Shape8.mirror = true;
		setRotation(Shape8, 0.7853982F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
