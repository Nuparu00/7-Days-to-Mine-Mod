package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelFlagCloth extends ModelBase {
	public final ModelRenderer flag;
	public final ModelRenderer flagRotator;
	public final ModelRenderer flagBase;

	public ModelFlagCloth() {
		textureWidth = 32;
		textureHeight = 32;

		flag = new ModelRenderer(this);
		flag.setRotationPoint(0.0F, 24.0F, 0.0F);

		flagRotator = new ModelRenderer(this);
		flagRotator.setRotationPoint(0.0F, -14.0F, 0.0F);
		flag.addChild(flagRotator);

		flagBase = new ModelRenderer(this);
		flagBase.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(flagBase, 0F, 0.0F, 0.0F);
		flagRotator.addChild(flagBase);
		flagBase.cubeList.add(new ModelBox(flagBase, 0, 3, 0.0F, 0.0F, -5.8F, 0, 19, 10, 0.0F, false));
	}

	public void render(float scale) {
		flag.render(scale);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}