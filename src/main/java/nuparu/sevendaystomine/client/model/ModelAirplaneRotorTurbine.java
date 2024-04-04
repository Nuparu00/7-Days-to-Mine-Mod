package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAirplaneRotorTurbine extends ModelBase {
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
    ModelRenderer Shape15;

	public ModelAirplaneRotorTurbine() {
		textureWidth = 64;
	    textureHeight = 64;
	    
	      Shape1 = new ModelRenderer(this, 0, 15);
	      Shape1.addBox(-1F, -1F, -4F, 2, 2, 3);
	      Shape1.setRotationPoint(0F, 0F, 0F);
	      Shape1.setTextureSize(64, 64);
	      Shape1.mirror = true;
	      setRotation(Shape1, 0F, 0F, 0F);
	      Shape2 = new ModelRenderer(this, 0, 0);
	      Shape2.addBox(-1.5F, -1.5F, -1F, 3, 3, 1);
	      Shape2.setRotationPoint(0F, 0F, 0F);
	      Shape2.setTextureSize(64, 64);
	      Shape2.mirror = true;
	      setRotation(Shape2, 0F, 0F, 0F);
	      Shape3 = new ModelRenderer(this, 0, 8);
	      Shape3.addBox(-0.5F, -0.5F, -6F, 1, 1, 2);
	      Shape3.setRotationPoint(0F, 0F, 0F);
	      Shape3.setTextureSize(64, 64);
	      Shape3.mirror = true;
	      setRotation(Shape3, 0F, 0F, 0F);
	      Shape4 = new ModelRenderer(this, 30, 0);
	      Shape4.addBox(-1.5F, -14F, -2F, 3, 11, 1);
	      Shape4.setRotationPoint(0F, 0F, 0F);
	      Shape4.setTextureSize(64, 64);
	      Shape4.mirror = true;
	      setRotation(Shape4, 0F, 0F, 0F);
	      Shape5 = new ModelRenderer(this, 30, 20);
	      Shape5.addBox(-1F, -3F, -2F, 2, 2, 1);
	      Shape5.setRotationPoint(0F, 0F, 0F);
	      Shape5.setTextureSize(64, 64);
	      Shape5.mirror = true;
	      setRotation(Shape5, 0F, 0F, 0F);
	      Shape6 = new ModelRenderer(this, 30, 15);
	      Shape6.addBox(-1F, -15F, -2F, 2, 1, 1);
	      Shape6.setRotationPoint(0F, 0F, 0F);
	      Shape6.setTextureSize(64, 64);
	      Shape6.mirror = true;
	      setRotation(Shape6, 0F, 0F, 0F);
	      Shape7 = new ModelRenderer(this, 20, 20);
	      Shape7.addBox(-1F, 1F, -2F, 2, 2, 1);
	      Shape7.setRotationPoint(0F, 0F, 0F);
	      Shape7.setTextureSize(64, 64);
	      Shape7.mirror = true;
	      setRotation(Shape7, 0F, 0F, 0F);
	      Shape8 = new ModelRenderer(this, 20, 0);
	      Shape8.addBox(-1.5F, 3F, -2F, 3, 11, 1);
	      Shape8.setRotationPoint(0F, 0F, 0F);
	      Shape8.setTextureSize(64, 64);
	      Shape8.mirror = true;
	      setRotation(Shape8, 0F, 0F, 0F);
	      Shape9 = new ModelRenderer(this, 20, 15);
	      Shape9.addBox(-1F, 14F, -2F, 2, 1, 1);
	      Shape9.setRotationPoint(0F, 0F, 0F);
	      Shape9.setTextureSize(64, 64);
	      Shape9.mirror = true;
	      setRotation(Shape9, 0F, 0F, 0F);
	      Shape10 = new ModelRenderer(this, 58, 20);
	      Shape10.addBox(1F, -1F, -2F, 2, 2, 1);
	      Shape10.setRotationPoint(0F, 0F, 0F);
	      Shape10.setTextureSize(64, 64);
	      Shape10.mirror = true;
	      setRotation(Shape10, 0F, 0F, 0F);
	      Shape11 = new ModelRenderer(this, 40, 28);
	      Shape11.addBox(3F, -1.5F, -2F, 11, 3, 1);
	      Shape11.setRotationPoint(0F, 0F, 0F);
	      Shape11.setTextureSize(64, 64);
	      Shape11.mirror = true;
	      setRotation(Shape11, 0F, 0F, 0F);
	      Shape12 = new ModelRenderer(this, 60, 0);
	      Shape12.addBox(14F, -1F, -2F, 1, 2, 1);
	      Shape12.setRotationPoint(0F, 0F, 0F);
	      Shape12.setTextureSize(64, 64);
	      Shape12.mirror = true;
	      setRotation(Shape12, 0F, 0F, 0F);
	      Shape13 = new ModelRenderer(this, 0, 22);
	      Shape13.addBox(-3F, -1F, -2F, 2, 2, 1);
	      Shape13.setRotationPoint(0F, 0F, 0F);
	      Shape13.setTextureSize(64, 64);
	      Shape13.mirror = true;
	      setRotation(Shape13, 0F, 0F, 0F);
	      Shape14 = new ModelRenderer(this, 0, 28);
	      Shape14.addBox(-14F, -1.5F, -2F, 11, 3, 1);
	      Shape14.setRotationPoint(0F, 0F, 0F);
	      Shape14.setTextureSize(64, 64);
	      Shape14.mirror = true;
	      setRotation(Shape14, 0F, 0F, 0F);
	      Shape15 = new ModelRenderer(this, 10, 22);
	      Shape15.addBox(-15F, -1F, -2F, 1, 2, 1);
	      Shape15.setRotationPoint(0F, 0F, 0F);
	      Shape15.setTextureSize(64, 64);
	      Shape15.mirror = true;
	      setRotation(Shape15, 0F, 0F, 0F);
	}

	public void render() {

		Shape1.render(0.0625F);
	    Shape2.render(0.0625F);
	    Shape3.render(0.0625F);
	    Shape4.render(0.0625F);
	    Shape5.render(0.0625F);
	    Shape6.render(0.0625F);
	    Shape7.render(0.0625F);
	    Shape8.render(0.0625F);
	    Shape9.render(0.0625F);
	    Shape10.render(0.0625F);
	    Shape11.render(0.0625F);
	    Shape12.render(0.0625F);
	    Shape13.render(0.0625F);
	    Shape14.render(0.0625F);
	    Shape15.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
