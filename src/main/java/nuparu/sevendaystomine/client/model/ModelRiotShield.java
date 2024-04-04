package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRiotShield extends ModelBase {
	private final ModelRenderer bone;

	public ModelRiotShield() {
		textureWidth = 64;
		textureHeight = 64;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 16, 24, -6.0F, -11.0F, -2.0F, 2, 8, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 15, -1.0F, -3.0F, -1.0F, 2, 6, 6, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 0, -6.0F, -3.0F, -2.0F, 12, 14, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 22, 24, 4.0F, -11.0F, -2.0F, 2, 8, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 10, 15, -4.0F, -11.0F, -2.0F, 8, 4, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 16, 20, -4.0F, -7.0F, -1.5F, 8, 4, 0, 0.0F, false));
	}

	public void render() {
		bone.render(0.0625F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}