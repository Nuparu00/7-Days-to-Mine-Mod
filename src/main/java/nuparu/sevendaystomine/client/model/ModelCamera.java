package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCamera extends ModelBase {
	private final ModelRenderer spine;
	private final ModelRenderer arm_joint;
	private final ModelRenderer head_joint;

	public ModelCamera() {
		textureWidth = 32;
		textureHeight = 32;

		spine = new ModelRenderer(this);
		spine.setRotationPoint(0.0F, 24.0F, 0.0F);
		spine.setTextureOffset(8, 9).addBox(-2.0F, -8.0F, 7.0F, 4, 3, 1,false);

		arm_joint = new ModelRenderer(this);
		arm_joint.setRotationPoint(0.0F, 0.0F, 0.0F);
		spine.addChild(arm_joint);
		arm_joint.setTextureOffset(0, 9).addBox(-1.0F, -7.0F, 3.0F, 2, 1, 4,false);
		arm_joint.setTextureOffset(0, 0).addBox(-1.0F, -8.0F, 3.0F, 2, 1, 1,false);

		head_joint = new ModelRenderer(this);
		head_joint.setRotationPoint(0.0F, -8.0F, 3.5F);
		arm_joint.addChild(head_joint);
		head_joint.setTextureOffset(0, 0).addBox(-1.5F, -3.0F, -3.5F, 3, 3, 6,false);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		spine.render(scale);
	}
	
	public void setAngle(float angle) {
		head_joint.rotateAngleY = angle/90f;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}