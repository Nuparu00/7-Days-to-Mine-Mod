package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAmbulance extends ModelBase {
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

	public ModelAmbulance() {
		textureWidth = 208;
		textureHeight = 208;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 16.0F, -31.0F);
		setRotationAngle(bone, 0.2618F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 106, -14.0F, -5.899F, -9.8284F, 28, 2, 11, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(13.0F, 13.5F, -14.0F);
		setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 8, 144, -1.01F, -11.9645F, -10.1924F, 2, 18, 2, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 144, -26.99F, -11.9645F, -10.1924F, 2, 18, 2, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(13.0F, 15.9F, 28.0F);
		setRotationAngle(bone3, 0.9599F, 0.0F, 0.0F);
		

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(13.5F, 22.5F, -13.0F);
		bone4.cubeList.add(new ModelBox(bone4, 0, 67, -0.5F, -12.5F, -9.0F, 1, 9, 15, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 8, 36, -0.5F, -21.5F, 5.0F, 1, 9, 1, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 8, 0, -27.5F, -21.5F, 5.0F, 1, 9, 1, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 72, 119, -27.55F, -24.5F, 0.0F, 28, 3, 6, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 29, 30, -7.5F, -32.5F, 11.0F, 1, 1, 2, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 140, 122, -24.5F, -34.5F, 10.0F, 22, 2, 4, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 0, 28, -20.5F, -32.5F, 11.0F, 1, 1, 2, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -16.5F, 8.5F);
		bone4.addChild(bone5);
		setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
		bone5.cubeList.add(new ModelBox(bone5, 42, 30, -0.53F, 0.5355F, -9.6924F, 1, 15, 1, 0.0F, false));

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(-13.5F, 22.5F, -13.0F);
		bone6.cubeList.add(new ModelBox(bone6, 0, 0, -0.5F, -12.5F, -9.0F, 1, 9, 15, 0.0F, false));

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, -16.5F, 8.5F);
		bone6.addChild(bone7);
		setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
		bone7.cubeList.add(new ModelBox(bone7, 38, 30, -0.47F, 0.5355F, -9.6924F, 1, 15, 1, 0.0F, false));

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(13.5F, 22.5F, 2.0F);
		bone8.cubeList.add(new ModelBox(bone8, 93, 67, -27.5F, -11.5F, -9.0F, 28, 8, 21, 0.0F, false));

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, -16.5F, 7.5F);
		bone8.addChild(bone9);
		setRotationAngle(bone9, 0.9599F, 0.0F, 0.0F);
		

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(-13.5F, 22.5F, 2.0F);
		

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, -16.5F, 7.5F);
		bone10.addChild(bone11);
		setRotationAngle(bone11, 0.9599F, 0.0F, 0.0F);
		

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(7.0F, 17.5F, -13.0F);
		setRotationAngle(bone12, 0.6109F, 0.0F, 0.0F);
		bone12.cubeList.add(new ModelBox(bone12, 12, 36, -0.5F, -8.9388F, -5.0781F, 1, 1, 2, 0.0F, false));
		bone12.cubeList.add(new ModelBox(bone12, 0, 24, -1.5F, -9.9388F, -3.0781F, 3, 3, 1, 0.0F, false));

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone13.cubeList.add(new ModelBox(bone13, 124, 142, -12.0F, -8.0F, -18.0F, 10, 3, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 24, 2.0F, -8.0F, -18.0F, 10, 3, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 91, -12.0F, -17.0F, -11.0F, 10, 9, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 17, 0, 2.0F, -17.0F, -11.0F, 10, 9, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 17, 67, -14.0F, -8.0F, 15.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 93, 67, 11.0F, -8.0F, 15.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 29, 18, -14.0F, -8.0F, -30.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 103, 0, 11.0F, -8.0F, -30.0F, 3, 6, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 119, -14.0F, -13.0F, 14.0F, 28, 3, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 78, 106, -14.0F, -14.0F, -32.0F, 28, 4, 9, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 64, 128, -11.0F, -10.0F, 14.0F, 22, 6, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 124, 128, -11.0F, -10.0F, -31.0F, 22, 6, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 67, -14.0F, -5.0F, -23.0F, 28, 2, 37, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 0, -14.0F, -33.0F, -7.0F, 28, 20, 47, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 103, 28, -14.0F, -9.0F, -42.0F, 28, 6, 11, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 17, 12, -6.0F, -9.0F, -43.0F, 12, 1, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 36, -7.0F, -12.0F, -44.0F, 1, 7, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 32, 14, -13.0F, -10.0F, -43.0F, 5, 3, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 10, 8.0F, -10.0F, -43.0F, 5, 3, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 0, 6.0F, -12.0F, -44.0F, 1, 7, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 93, 96, -14.0F, -12.0F, -36.0F, 28, 3, 5, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 137, -14.0F, -11.0F, -41.0F, 28, 2, 5, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 134, 119, -14.0F, -11.0F, -42.0F, 28, 2, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 103, 0, -14.0F, -13.0F, 22.0F, 28, 10, 18, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 130, -14.0F, -5.0F, 40.0F, 28, 2, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 66, 142, -14.0F, -14.0F, -23.0F, 28, 9, 1, 0.0F, false));
	}

	@Override
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
