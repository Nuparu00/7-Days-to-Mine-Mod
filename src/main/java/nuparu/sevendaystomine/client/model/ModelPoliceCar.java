package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPoliceCar extends ModelBase {
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer bone7;
	private final ModelRenderer bone8;
	private final ModelRenderer bone9;
	private final ModelRenderer bone10;
	private final ModelRenderer bone11;
	private final ModelRenderer bone12;
	private final ModelRenderer bone13;

	public ModelPoliceCar() {
		textureWidth = 176;
		textureHeight = 176;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 16.0F, -31.0F);
		setRotationAngle(bone, 0.2618F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 67, 67, -14.0F, -3.8978F, -6.2235F, 28, 2, 11, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(13.0F, 13.5F, -14.0F);
		setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 8, 43, -1.01F, -14.6213F, -3.1213F, 2, 15, 2, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 43, -26.99F, -14.6213F, -3.1213F, 2, 15, 2, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(13.0F, 15.9F, 28.0F);
		setRotationAngle(bone3, 0.9599F, 0.0F, 0.0F);
		bone3.cubeList.add(new ModelBox(bone3, 130, 130, -1.01F, -22.6207F, 0.4575F, 2, 20, 2, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 107, 117, -25.0F, -12.6207F, 0.4575F, 24, 10, 2, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 32, 0, -26.99F, -22.6207F, 0.4575F, 2, 20, 2, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(13.5F, 22.5F, -13.0F);
		bone4.cubeList.add(new ModelBox(bone4, 49, 128, -0.5F, -11.5F, 0.0F, 1, 9, 15, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 67, 62, -0.5F, -19.5F, 14.0F, 1, 8, 1, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 0, 142, -0.52F, -19.5F, 7.0F, 1, 1, 7, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -16.5F, 8.5F);
		bone4.addChild(bone5);
		setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
		bone5.cubeList.add(new ModelBox(bone5, 25, 0, -0.53F, -2.1213F, -2.6213F, 1, 12, 1, 0.0F, false));

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(-13.5F, 22.5F, -13.0F);
		bone6.cubeList.add(new ModelBox(bone6, 32, 118, -0.5F, -11.5F, 0.0F, 1, 9, 15, 0.0F, false));
		bone6.cubeList.add(new ModelBox(bone6, 38, 33, -0.5F, -19.5F, 14.0F, 1, 8, 1, 0.0F, false));
		bone6.cubeList.add(new ModelBox(bone6, 99, 141, -0.48F, -19.5F, 7.0F, 1, 1, 7, 0.0F, false));

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, -16.5F, 8.5F);
		bone6.addChild(bone7);
		setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
		bone7.cubeList.add(new ModelBox(bone7, 8, 0, -0.47F, -2.1213F, -2.6213F, 1, 12, 1, 0.0F, false));

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(13.5F, 22.5F, 2.0F);
		bone8.cubeList.add(new ModelBox(bone8, 0, 118, -0.5F, -11.5F, 0.0F, 1, 9, 15, 0.0F, false));
		bone8.cubeList.add(new ModelBox(bone8, 4, 24, -0.5F, -19.5F, 0.0F, 1, 8, 1, 0.0F, false));
		bone8.cubeList.add(new ModelBox(bone8, 138, 129, -0.52F, -19.5F, 1.0F, 1, 1, 7, 0.0F, false));

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, -16.5F, 7.5F);
		bone8.addChild(bone9);
		setRotationAngle(bone9, 0.9599F, 0.0F, 0.0F);
		bone9.cubeList.add(new ModelBox(bone9, 4, 62, -0.53F, -1.7207F, 1.9575F, 1, 9, 1, 0.0F, false));

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(-13.5F, 22.5F, 2.0F);
		bone10.cubeList.add(new ModelBox(bone10, 0, 0, -0.5F, -11.5F, 0.0F, 1, 9, 15, 0.0F, false));
		bone10.cubeList.add(new ModelBox(bone10, 0, 24, -0.5F, -19.5F, 0.0F, 1, 8, 1, 0.0F, false));
		bone10.cubeList.add(new ModelBox(bone10, 136, 41, -0.48F, -19.5F, 1.0F, 1, 1, 7, 0.0F, false));

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, -16.5F, 7.5F);
		bone10.addChild(bone11);
		setRotationAngle(bone11, 0.9599F, 0.0F, 0.0F);
		bone11.cubeList.add(new ModelBox(bone11, 0, 62, -0.47F, -1.7207F, 1.9575F, 1, 9, 1, 0.0F, false));

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(7.0F, 17.5F, -13.0F);
		setRotationAngle(bone12, 0.6109F, 0.0F, 0.0F);
		bone12.cubeList.add(new ModelBox(bone12, 17, 10, -0.5F, -2.9575F, 1.7207F, 1, 1, 2, 0.0F, false));
		bone12.cubeList.add(new ModelBox(bone12, 0, 10, -1.5F, -3.9575F, 3.7207F, 3, 3, 1, 0.0F, false));

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone13.cubeList.add(new ModelBox(bone13, 53, 117, -13.0F, -14.0F, 2.0F, 26, 10, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 120, 109, -10.0F, -26.0F, 0.0F, 20, 2, 4, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 24, 36, -6.0F, -24.0F, 1.0F, 1, 1, 2, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 21, 11, 5.0F, -24.0F, 1.0F, 1, 1, 2, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 66, 128, -12.0F, -7.0F, -9.0F, 10, 3, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 24, 2.0F, -7.0F, -9.0F, 10, 3, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 68, 91, -12.0F, -7.0F, 6.0F, 24, 3, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 104, 129, -12.0F, -16.0F, -2.0F, 10, 9, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 17, 118, 2.0F, -16.0F, -2.0F, 10, 9, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 106, -12.0F, -16.0F, 13.0F, 24, 9, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 43, -14.0F, -23.0F, -6.0F, 28, 2, 17, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 138, 138, -14.0F, -8.0F, 24.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 139, 74, 11.0F, -8.0F, 24.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 134, 91, -14.0F, -8.0F, -26.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 81, 140, 11.0F, -8.0F, -26.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 74, 80, -14.0F, -12.0F, 23.0F, 28, 3, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 78, -14.0F, -13.0F, -28.0F, 28, 4, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 98, 21, -11.0F, -9.0F, 23.0F, 22, 6, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 60, 103, -11.0F, -9.0F, -27.0F, 22, 6, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 112, 103, -14.0F, -15.0F, 19.0F, 28, 1, 2, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 98, 35, -14.0F, -17.0F, 16.0F, 28, 3, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 0, -14.0F, -4.0F, -19.0F, 28, 1, 42, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 62, -14.0F, -8.0F, -38.0F, 28, 5, 11, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 36, -6.0F, -8.0F, -39.0F, 12, 1, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 17, 0, -7.0F, -11.0F, -40.0F, 1, 7, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 29, 24, -13.0F, -9.0F, -39.0F, 5, 3, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 29, 29, 8.0F, -9.0F, -39.0F, 5, 3, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 0, 6.0F, -11.0F, -40.0F, 1, 7, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 98, 13, -14.0F, -11.0F, -32.0F, 28, 3, 5, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 85, 60, -14.0F, -10.0F, -37.0F, 28, 2, 5, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 112, 106, -14.0F, -10.0F, -38.0F, 28, 2, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 98, 0, -14.0F, -12.0F, 31.0F, 28, 9, 4, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 91, -14.0F, -13.0F, -19.0F, 28, 9, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 73, 43, -14.0F, -14.0F, 16.0F, 28, 10, 7, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 73, 43, -14.0F, -14.0F, 16.0F, 28, 10, 7, 0.0F, false));
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
		bone2.render(f5);
		bone3.render(f5);
		bone4.render(f5);
		bone6.render(f5);
		bone8.render(f5);
		bone10.render(f5);
		bone12.render(f5);
		bone13.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
