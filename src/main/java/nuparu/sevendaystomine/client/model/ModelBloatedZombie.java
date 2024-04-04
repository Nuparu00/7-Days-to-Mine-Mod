package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBloatedZombie extends ModelBase {
	public ModelBiped.ArmPose leftArmPose = ModelBiped.ArmPose.EMPTY;
	public ModelBiped.ArmPose rightArmPose = ModelBiped.ArmPose.EMPTY;

	public ModelRenderer body;
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape11;
	public ModelRenderer shape4;
	public ModelRenderer shape9;
	public ModelRenderer shape10;
	public ModelRenderer shape3;
	public ModelRenderer shape12;
	public ModelRenderer leftArm;
	public ModelRenderer head;
	public ModelRenderer shape6;
	public ModelRenderer shape5;
	public ModelRenderer shape7;
	public ModelRenderer shape8;
	public ModelRenderer rightArm;
	public ModelRenderer shape13;
	public ModelRenderer leftLeg;
	public ModelRenderer shape14;
	public ModelRenderer shape15;
	public ModelRenderer rightLeg;
	public ModelRenderer shape16;
	public ModelRenderer shape17;

	public boolean isSneak;

	public ModelBloatedZombie() {
		this(0);
	}

	public ModelBloatedZombie(float scale) {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
		body.setRotationPoint(0F, 0F, 0F);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		shape1 = new ModelRenderer(this, 0, 33);
		shape1.addBox(-4F, 6F, -3F, 5, 5, 1, scale);
		shape1.setRotationPoint(0F, 0F, 0F);
		shape1.setTextureSize(64, 64);
		shape1.mirror = true;
		setRotation(shape1, 0F, 0F, 0F);
		shape2 = new ModelRenderer(this, 0, 40);
		shape2.addBox(0F, 1F, 2F, 4, 5, 1, scale);
		shape2.setRotationPoint(0F, 0F, 0F);
		shape2.setTextureSize(64, 64);
		shape2.mirror = true;
		setRotation(shape2, 0F, 0F, 0F);
		shape11 = new ModelRenderer(this, 0, 47);
		shape11.addBox(-3F, 3F, -3F, 3, 3, 1, scale);
		shape11.setRotationPoint(0F, 0F, 0F);
		shape11.setTextureSize(64, 64);
		shape11.mirror = true;
		setRotation(shape11, 0F, 0F, 0F);
		shape4 = new ModelRenderer(this, 0, 52);
		shape4.addBox(-1F, 4F, -4F, 2, 2, 1, scale);
		shape4.setRotationPoint(0F, 0F, 0F);
		shape4.setTextureSize(64, 64);
		shape4.mirror = true;
		setRotation(shape4, 0F, 0F, 0F);
		shape9 = new ModelRenderer(this, 0, 56);
		shape9.addBox(0F, 0F, 0F, 4, 5, 1, scale);
		shape9.setRotationPoint(0F, 0F, 0F);
		shape9.setTextureSize(64, 64);
		shape9.mirror = true;
		setRotation(shape9, 0F, 0F, 0F);
		shape10 = new ModelRenderer(this, 13, 33);
		shape10.addBox(-4F, 7F, 2F, 4, 4, 1, scale);
		shape10.setRotationPoint(0F, 0F, 0F);
		shape10.setTextureSize(64, 64);
		shape10.mirror = true;
		setRotation(shape10, 0F, 0F, 0F);
		shape3 = new ModelRenderer(this, 24, 33);
		shape3.addBox(0F, 2F, -3F, 4, 4, 1, scale);
		shape3.setRotationPoint(0F, 0F, 0F);
		shape3.setTextureSize(64, 64);
		shape3.mirror = true;
		setRotation(shape3, 0F, 0F, 0F);
		shape12 = new ModelRenderer(this, 11, 40);
		shape12.addBox(1F, 7F, -3F, 3, 3, 1, scale);
		shape12.setRotationPoint(0F, 0F, 0F);
		shape12.setTextureSize(64, 64);
		shape12.mirror = true;
		setRotation(shape12, 0F, 0F, 0F);
		leftArm = new ModelRenderer(this, 44, 45);
		leftArm.addBox(-1F, -2F, -2F, 4, 12, 4, scale);
		leftArm.setRotationPoint(5F, 2F, 0F);
		leftArm.setTextureSize(64, 64);
		leftArm.mirror = true;
		setRotation(leftArm, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
		head.setRotationPoint(0F, 0F, 0F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		shape6 = new ModelRenderer(this, 11, 45);
		shape6.addBox(0.5F, -6.5F, 1.5F, 4, 5, 3, scale);
		shape6.setRotationPoint(0F, 0F, 0F);
		shape6.setTextureSize(64, 64);
		shape6.mirror = true;
		setRotation(shape6, 0F, 0F, 0F);
		shape5 = new ModelRenderer(this, 11, 54);
		shape5.addBox(0.5F, -2.5F, -4.5F, 4, 3, 3, scale);
		shape5.setRotationPoint(0F, 0F, 0F);
		shape5.setTextureSize(64, 64);
		shape5.mirror = true;
		setRotation(shape5, 0F, 0F, 0F);
		shape7 = new ModelRenderer(this, 35, 33);
		shape7.addBox(-4.466667F, -5F, -4.5F, 2, 4, 5, scale);
		shape7.setRotationPoint(0F, 0F, 0F);
		shape7.setTextureSize(64, 64);
		shape7.mirror = true;
		setRotation(shape7, 0F, 0F, 0F);
		shape8 = new ModelRenderer(this, 33, 0);
		shape8.addBox(-1F, -8.5F, -4.5F, 4, 2, 4, scale);
		shape8.setRotationPoint(0F, 0F, 0F);
		shape8.setTextureSize(64, 64);
		shape8.mirror = true;
		setRotation(shape8, 0F, 0F, 0F);
		rightArm = new ModelRenderer(this, 40, 16);
		rightArm.addBox(-3F, -2F, -2F, 4, 12, 4, scale);
		rightArm.setRotationPoint(-5F, 2F, 0F);
		rightArm.setTextureSize(64, 64);
		rightArm.mirror = true;
		setRotation(rightArm, 0F, 0F, 0F);
		shape13 = new ModelRenderer(this, 33, 7);
		shape13.addBox(-3.5F, 0F, -1F, 1, 2, 2, scale);
		shape13.setRotationPoint(0F, 0F, 0F);
		shape13.setTextureSize(64, 64);
		shape13.mirror = true;
		setRotation(shape13, 0F, 0F, 0F);
		leftLeg = new ModelRenderer(this, 0, 16);
		leftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
		leftLeg.setRotationPoint(2F, 12F, 0F);
		leftLeg.setTextureSize(64, 64);
		leftLeg.mirror = true;
		setRotation(leftLeg, 0F, 0F, 0F);
		shape14 = new ModelRenderer(this, 40, 7);
		shape14.addBox(0.2F, 2F, -2.5F, 2, 2, 1, scale);
		shape14.setRotationPoint(0F, 0F, 0F);
		shape14.setTextureSize(64, 64);
		shape14.mirror = true;
		setRotation(shape14, 0F, 0F, 0F);
		shape15 = new ModelRenderer(this, 40, 11);
		shape15.addBox(1.4F, 5F, 0.2F, 1, 2, 2, scale);
		shape15.setRotationPoint(0F, 0F, 0F);
		shape15.setTextureSize(64, 64);
		shape15.mirror = true;
		setRotation(shape15, 0F, 0F, 0F);
		rightLeg = new ModelRenderer(this, 27, 45);
		rightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
		rightLeg.setRotationPoint(-2F, 12F, 0F);
		rightLeg.setTextureSize(64, 64);
		rightLeg.mirror = true;
		setRotation(rightLeg, 0F, 0F, 0F);
		shape16 = new ModelRenderer(this, 50, 0);
		shape16.addBox(0F, 6F, -2.5F, 2, 2, 1, scale);
		shape16.setRotationPoint(0F, 0F, 0F);
		shape16.setTextureSize(64, 64);
		shape16.mirror = true;
		setRotation(shape16, 0F, 0F, 0F);
		shape17 = new ModelRenderer(this, 50, 4);
		shape17.addBox(-1F, 3F, 1.5F, 2, 2, 1, scale);
		shape17.setRotationPoint(0F, 0F, 0F);
		shape17.setTextureSize(64, 64);
		shape17.mirror = true;
		setRotation(shape17, 0F, 0F, 0F);

		body.addChild(shape1);
		body.addChild(shape2);
		body.addChild(shape3);
		body.addChild(shape4);
		body.addChild(shape9);
		body.addChild(shape10);
		body.addChild(shape11);
		body.addChild(shape12);

		head.addChild(shape5);
		head.addChild(shape6);
		head.addChild(shape7);
		head.addChild(shape8);

		rightArm.addChild(shape13);

		leftLeg.addChild(shape14);
		leftLeg.addChild(shape15);

		rightLeg.addChild(shape16);
		rightLeg.addChild(shape17);

	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			float f = 2.0F;
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.head.render(scale);
			this.body.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);
		}

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
			ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
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
			modelrenderer.rotateAngleX = (float) ((double) modelrenderer.rotateAngleX
					- ((double) f2 * 1.2D + (double) f3));
			modelrenderer.rotateAngleY += this.body.rotateAngleY * 2.0F;
			modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
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
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
			return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
		} else {
			return EnumHandSide.RIGHT;
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
