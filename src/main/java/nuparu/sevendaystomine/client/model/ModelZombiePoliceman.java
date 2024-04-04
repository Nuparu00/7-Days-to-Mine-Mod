package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityZombiePoliceman;
import nuparu.sevendaystomine.entity.EntityZombiePoliceman.EnumAnimationState;

@SideOnly(Side.CLIENT)
public class ModelZombiePoliceman extends ModelBase {
	public ModelBiped.ArmPose leftArmPose = ModelBiped.ArmPose.EMPTY;
	public ModelBiped.ArmPose rightArmPose = ModelBiped.ArmPose.EMPTY;
	public boolean isSneak;

	private final ModelRenderer body;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;
	private final ModelRenderer head;
	private final ModelRenderer hat;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;

	public ModelZombiePoliceman() {
		textureWidth = 80;
		textureHeight = 80;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 12.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 30, -4.0F, -12.0F, -2.0F, 8, 12, 4, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 50, 20, -4.0F, -8.0F, -3.0F, 8, 8, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 22, 26, 0.0F, -11.0F, -3.0F, 4, 3, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 3, -3.0F, -10.0F, -3.0F, 3, 2, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 0, 0.0F, -9.0F, -4.0F, 3, 2, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 24, 11, -4.0F, -7.0F, -4.0F, 7, 5, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 32, 20, -4.0F, -2.0F, -4.0F, 6, 2, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 32, 56, -4.0F, -6.0F, -5.0F, 6, 5, 1, 0.0F, false));

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 24, 34, -3F, -2F, -2F, 4, 12, 4, 0.0F, false));

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(5F, 2F, 0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 40, 40, -1F, -2F, -2F, 4, 12, 4, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 10, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 40, 34, -3.0F, -2.0F, -5.0F, 6, 3, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 55, 37, -4.5F, -4.0F, -4.5F, 3, 3, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 53, 53, 1.5F, -4.0F, -4.5F, 3, 3, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 6, -2.0F, 1.0F, -4.0F, 3, 1, 1, 0.0F, false));

		hat = new ModelRenderer(this);
		hat.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(hat);
		hat.cubeList.add(new ModelBox(hat, 23, 23, -4.5F, -9.0F, -4.5F, 9, 2, 9, 0.0F, false));
		hat.cubeList.add(new ModelBox(hat, 0, 26, -4.5F, -8.0F, -6.5F, 9, 1, 2, 0.0F, false));
		hat.cubeList.add(new ModelBox(hat, 29, 1, -4.5F, -10.0F, -4.5F, 9, 1, 9, 0.0F, false));
		hat.cubeList.add(new ModelBox(hat, 0, 0, -5.0F, -11.0F, -5.0F, 10, 1, 9, 0.0F, false));
		hat.cubeList.add(new ModelBox(hat, 32, 11, -5.0F, -12.0F, -5.0F, 10, 1, 8, 0.0F, false));

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 16, 50, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 46, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		if (entityIn.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		this.head.render(scale);
		this.body.render(scale);
		this.rightArm.render(scale);
		this.leftArm.render(scale);
		this.rightLeg.render(scale);
		this.leftLeg.render(scale);
		

		GlStateManager.popMatrix();

	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used
	 * for animating the movement of arms and legs, where par1 represents the
	 * time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@SuppressWarnings("incomplete-switch")
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;

		if (flag) {
			this.head.rotateAngleX = -((float) Math.PI / 4F);
		} else {
			this.head.rotateAngleX = headPitch * 0.017453292F;
		}
		

		this.body.rotateAngleY = 0.0F;
		this.rightArm.rotationPointZ = 0.0F;
		this.rightArm.rotationPointX = -5.0F;
		this.leftArm.rotationPointZ = 0.0F;
		this.leftArm.rotationPointX = 5.0F;
		float f = 1.0F;

		if (flag) {
			f = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY
					+ entityIn.motionZ * entityIn.motionZ);
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount
				* 0.5F / f;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.rightArm.rotateAngleZ = 0.0F;
		this.leftArm.rotateAngleZ = 0.0F;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		this.rightLeg.rotateAngleY = 0.0F;
		this.leftLeg.rotateAngleY = 0.0F;
		this.rightLeg.rotateAngleZ = 0.0F;
		this.leftLeg.rotateAngleZ = 0.0F;

		if (this.isRiding) {
			this.rightArm.rotateAngleX += -((float) Math.PI / 5F);
			this.leftArm.rotateAngleX += -((float) Math.PI / 5F);
			this.rightLeg.rotateAngleX = -1.4137167F;
			this.rightLeg.rotateAngleY = ((float) Math.PI / 10F);
			this.rightLeg.rotateAngleZ = 0.07853982F;
			this.leftLeg.rotateAngleX = -1.4137167F;
			this.leftLeg.rotateAngleY = -((float) Math.PI / 10F);
			this.leftLeg.rotateAngleZ = -0.07853982F;
		}

		this.rightArm.rotateAngleY = 0.0F;
		this.rightArm.rotateAngleZ = 0.0F;

		switch (this.leftArmPose) {
		case EMPTY:
			this.leftArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - 0.9424779F;
			this.leftArm.rotateAngleY = 0.5235988F;
			break;
		case ITEM:
			this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			this.leftArm.rotateAngleY = 0.0F;
		}

		switch (this.rightArmPose) {
		case EMPTY:
			this.rightArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - 0.9424779F;
			this.rightArm.rotateAngleY = -0.5235988F;
			break;
		case ITEM:
			this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			this.rightArm.rotateAngleY = 0.0F;
		}

		if (this.swingProgress > 0.0F) {
			EnumHandSide enumhandside = this.getMainHand(entityIn);
			float f1 = this.swingProgress;
			this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

			if (enumhandside == EnumHandSide.LEFT) {
				this.body.rotateAngleY *= -1.0F;
			}

			this.rightArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
			this.rightArm.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
			this.leftArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
			this.leftArm.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
			this.rightArm.rotateAngleY += this.body.rotateAngleY;
			this.leftArm.rotateAngleY += this.body.rotateAngleY;
			this.leftArm.rotateAngleX += this.body.rotateAngleY;
			f1 = 1.0F - this.swingProgress;
			f1 = f1 * f1;
			f1 = f1 * f1;
			f1 = 1.0F - f1;
			float f2 = MathHelper.sin(f1 * (float) Math.PI);
			float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
			this.leftArm.rotateAngleX = (float) ((double) this.leftArm.rotateAngleX
					- ((double) f2 * 1.2D + (double) f3));
			this.leftArm.rotateAngleY += this.body.rotateAngleY * 2.0F;
			this.leftArm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
			
			this.rightArm.rotateAngleX = (float) ((double) this.rightArm.rotateAngleX
					- ((double) f2 * 1.2D + (double) f3));
			this.rightArm.rotateAngleY += this.body.rotateAngleY * 2.0F;
			this.rightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
		}

		if (this.isSneak) {
			this.body.rotateAngleX = 0.5F;
			this.rightArm.rotateAngleX += 0.4F;
			this.leftArm.rotateAngleX += 0.4F;
			this.rightLeg.rotationPointZ = 4.0F;
			this.leftLeg.rotationPointZ = 4.0F;
			this.rightLeg.rotationPointY = 9.0F;
			this.leftLeg.rotationPointY = 9.0F;
			this.head.rotationPointY = 1.0F;
		} else {
			this.body.rotateAngleX = 0.0F;
			this.rightLeg.rotationPointZ = 0.1F;
			this.leftLeg.rotationPointZ = 0.1F;
			this.rightLeg.rotationPointY = 12.0F;
			this.leftLeg.rotationPointY = 12.0F;
			this.head.rotationPointY = 0.0F;
		}

		this.rightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

		if (this.rightArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
			this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
			this.leftArm.rotateAngleY = 0.1F + this.head.rotateAngleY + 0.4F;
			this.rightArm.rotateAngleX = -((float) Math.PI / 2F) + this.head.rotateAngleX;
			this.leftArm.rotateAngleX = -((float) Math.PI / 2F) + this.head.rotateAngleX;
		} else if (this.leftArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
			this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY - 0.4F;
			this.leftArm.rotateAngleY = 0.1F + this.head.rotateAngleY;
			this.rightArm.rotateAngleX = -((float) Math.PI / 2F) + this.head.rotateAngleX;
			this.leftArm.rotateAngleX = -((float) Math.PI / 2F) + this.head.rotateAngleX;
		}
		
		if(entityIn instanceof EntityZombiePoliceman) {
			EntityZombiePoliceman zombie = (EntityZombiePoliceman)entityIn;
			if(zombie.getAnimation() == EnumAnimationState.VOMITING) {
				this.head.rotateAngleX -= Math.toRadians(46);
				this.rightArm.rotateAngleX += Math.toRadians(15);
				this.leftArm.rotateAngleX += Math.toRadians(15);
			}
		}
	}

	public void setModelAttributes(ModelBase model) {
		super.setModelAttributes(model);

		if (model instanceof ModelBiped) {
			ModelBiped modelbiped = (ModelBiped) model;
			this.leftArmPose = modelbiped.leftArmPose;
			this.rightArmPose = modelbiped.rightArmPose;
			this.isSneak = modelbiped.isSneak;
		}
	}

	protected ModelRenderer getArmForSide(EnumHandSide side) {
		return side == EnumHandSide.LEFT ? this.leftArm : this.rightArm;
	}

	protected EnumHandSide getMainHand(Entity entityIn) {
		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbody = (EntityLivingBase) entityIn;
			EnumHandSide enumhandside = entitylivingbody.getPrimaryHand();
			return entitylivingbody.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
		} else {
			return EnumHandSide.RIGHT;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
