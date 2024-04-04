package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelFlagPole extends ModelBase {
	private final ModelRenderer flag;
	private final ModelRenderer pole;

	public ModelFlagPole() {
		textureWidth = 64;
		textureHeight = 32;

		flag = new ModelRenderer(this);
		flag.setRotationPoint(0.0F, 24.0F, 0.0F);
		flag.cubeList.add(new ModelBox(flag, 0, 22, -2.0F, -5.0F, 7.0F, 4, 4, 2, 0.0F, false));

		pole = new ModelRenderer(this);
		pole.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(pole, -1.0472F, 0.0F, 0.0F);
		flag.addChild(pole);
		pole.cubeList.add(new ModelBox(pole, 0, 0, -1.0F, -9.0F, -18.0F, 2, 2, 20, 0.0F, false));
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
