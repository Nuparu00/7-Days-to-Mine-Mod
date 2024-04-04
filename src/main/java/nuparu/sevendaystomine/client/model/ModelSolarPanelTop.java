package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSolarPanelTop extends ModelBase {
	ModelRenderer Shape4;

	public ModelSolarPanelTop() {
		textureWidth = 128;
		textureHeight = 64;

		Shape4 = new ModelRenderer(this, 72, 0);
		Shape4.addBox(-7F, -1F, -7F, 14, 2, 14);
		Shape4.setRotationPoint(0F, 0F, 0F);
		Shape4.setTextureSize(128, 64);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
	}

	public void render() {
		Shape4.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}