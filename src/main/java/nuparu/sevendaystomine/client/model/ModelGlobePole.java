package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGlobePole extends ModelBase {

	ModelRenderer Shape1;
	ModelRenderer Shape2;

	public ModelGlobePole() {
		textureWidth = 64;
		textureHeight = 64;

		Shape1 = new ModelRenderer(this, 0, 22);
		Shape1.addBox(0F, 0F, 0F, 8, 1, 8);
		Shape1.setRotationPoint(-4F, 23F, -4F);
		Shape1.setTextureSize(64, 64);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 42, 0);
		Shape2.addBox(0F, 0F, 0F, 2, 16, 2);
		Shape2.setRotationPoint(-1F, 9F, -7F);
		Shape2.setTextureSize(64, 64);
		Shape2.mirror = true;
		setRotation(Shape2, 0.4014257F, 0F, 0F);

	}

	public void render() {

		Shape1.render(0.0625F);
		Shape2.render(0.0625F);

	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}