package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGlobe extends ModelBase {

	ModelRenderer globe;

	public ModelGlobe() {
		textureWidth = 64;
		textureHeight = 64;

		globe = new ModelRenderer(this, 0, 0);
		globe.addBox(0F, 0F, 0F, 10, 10, 10);
		globe.setRotationPoint(-5F, 13F, -5F);
		globe.setTextureSize(64, 64);
		globe.mirror = true;
		setRotation(globe, 0F, 0F, 0F);
	}

	public void render() {

		globe.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}