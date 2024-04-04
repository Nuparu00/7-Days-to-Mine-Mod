package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelChlorineGrenade extends ModelBase {
	private final ModelRenderer bone;
	private final ModelRenderer bone2;

	public ModelChlorineGrenade() {
		textureWidth = 48;
		textureHeight = 48;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 20, -3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 25, -3.0F, -11.0F, -3.0F, 6, 1, 6, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 20, 6, -3.0F, -10.0F, -3.0F, 6, 4, 6, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 16, -2.0F, -12.0F, -2.0F, 4, 1, 4, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 20, -3.0F, -14.0F, -2.0F, 5, 1, 4, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 1, -0.5F, -16.0F, -0.5F, 1, 4, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 1, -0.75F, -16.5F, -0.75F, 1, 0, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 1, -1.5F, -15.0F, -0.5F, 1, 1, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 4, 38, 0.5F, -15.5F, -1.0F, 0, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 35, 35, 0.7F, -15.5F, -0.065F, 0, 1, 0, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-11.25F, -17.5F, 8.0F);
		bone.addChild(bone2);
		setRotationAngle(bone2, 0.0F, 0.0F, 0.2618F);
		bone2.cubeList.add(new ModelBox(bone2, 38, 17, 8.4627F, 1.2932F, -10.0F, 1, 9, 4, 0.0F, false));
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