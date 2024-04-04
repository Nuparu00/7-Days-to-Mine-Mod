package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFragmentationGrenade extends ModelBase {
	private final ModelRenderer bone;
	private final ModelRenderer bone2;

	public ModelFragmentationGrenade() {
		textureWidth = 32;
		textureHeight = 32;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -2.0F, -3.0F, 6, 2, 6, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -7.0F, -3.0F, 6, 1, 6, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 16, -3.5F, -6.0F, -3.5F, 7, 4, 7, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 10, -2.0F, -8.0F, -2.0F, 4, 1, 4, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 3, 9, -2.0F, -10.0F, -1.0F, 4, 1, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 8, -0.5F, -11.0F, -0.5F, 1, 3, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 4, 8, -0.75F, -11.5F, -0.75F, 1, 0, 1, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-10.5F, -17.5F, 8.0F);
		bone.addChild(bone2);
		setRotationAngle(bone2, 0.0F, 0.0F, 0.3491F);
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, 9.7537F, 4.2814F, -9.0F, 1, 6, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}