package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import nuparu.sevendaystomine.world.entity.zombie.BloatedZombieEntity;
import org.jetbrains.annotations.NotNull;

public class BloatedZombieModel<T extends BloatedZombieEntity> extends EntityModel<T> implements ArmedModel, HeadedModel {

	public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
	public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
	public float swimAmount;
	public boolean crouching;
	private final ModelPart body;
	private final ModelPart leftArm;
	private final ModelPart head;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public BloatedZombieModel(ModelPart root) {
		this.body = root.getChild("body");
		this.leftArm = root.getChild("leftArm");
		this.head = root.getChild("head");
		this.rightArm = root.getChild("rightArm");
		this.leftLeg = root.getChild("leftLeg");
		this.rightLeg = root.getChild("rightLeg");

	}
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).mirror().addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape1 = body.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 33).mirror().addBox(-4.0F, 6.0F, -3.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape2 = body.addOrReplaceChild("shape2", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(0.0F, 1.0F, 2.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape3 = body.addOrReplaceChild("shape3", CubeListBuilder.create().texOffs(24, 33).mirror().addBox(0.0F, 2.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape4 = body.addOrReplaceChild("shape4", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.0F, 4.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape9 = body.addOrReplaceChild("shape9", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape10 = body.addOrReplaceChild("shape10", CubeListBuilder.create().texOffs(13, 33).mirror().addBox(-4.0F, 7.0F, 2.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape11 = body.addOrReplaceChild("shape11", CubeListBuilder.create().texOffs(0, 47).mirror().addBox(-3.0F, 3.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape12 = body.addOrReplaceChild("shape12", CubeListBuilder.create().texOffs(11, 40).mirror().addBox(1.0F, 7.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(44, 45).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape5 = head.addOrReplaceChild("shape5", CubeListBuilder.create().texOffs(11, 54).mirror().addBox(0.5F, -2.5F, -4.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape6 = head.addOrReplaceChild("shape6", CubeListBuilder.create().texOffs(11, 45).mirror().addBox(0.5F, -6.5F, 1.5F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape7 = head.addOrReplaceChild("shape7", CubeListBuilder.create().texOffs(35, 33).mirror().addBox(-4.4667F, -5.0F, -4.5F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape8 = head.addOrReplaceChild("shape8", CubeListBuilder.create().texOffs(33, 0).mirror().addBox(-1.0F, -8.5F, -4.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition shape13 = rightArm.addOrReplaceChild("shape13", CubeListBuilder.create().texOffs(33, 7).mirror().addBox(-3.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition shape14 = leftLeg.addOrReplaceChild("shape14", CubeListBuilder.create().texOffs(40, 7).mirror().addBox(0.2F, 2.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape15 = leftLeg.addOrReplaceChild("shape15", CubeListBuilder.create().texOffs(40, 11).mirror().addBox(1.4F, 5.0F, 0.2F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(27, 45).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition shape16 = rightLeg.addOrReplaceChild("shape16", CubeListBuilder.create().texOffs(50, 0).mirror().addBox(0.0F, 6.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape17 = rightLeg.addOrReplaceChild("shape17", CubeListBuilder.create().texOffs(50, 4).mirror().addBox(-1.0F, 3.0F, 1.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setupAnim(T p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
		boolean flag = p_102866_.getFallFlyingTicks() > 4;
		boolean flag1 = p_102866_.isVisuallySwimming();
		this.head.yRot = p_102870_ * ((float)Math.PI / 180F);
		if (flag) {
			this.head.xRot = (-(float)Math.PI / 4F);
		} else if (this.swimAmount > 0.0F) {
			if (flag1) {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float)Math.PI / 4F));
			} else {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, p_102871_ * ((float)Math.PI / 180F));
			}
		} else {
			this.head.xRot = p_102871_ * ((float)Math.PI / 180F);
		}

		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;
		float f = 1.0F;
		if (flag) {
			f = (float)p_102866_.getDeltaMovement().lengthSqr();
			f /= 0.2F;
			f *= f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		this.rightArm.xRot = Mth.cos(p_102867_ * 0.6662F + (float)Math.PI) * 2.0F * p_102868_ * 0.5F / f;
		this.leftArm.xRot = Mth.cos(p_102867_ * 0.6662F) * 2.0F * p_102868_ * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(p_102867_ * 0.6662F) * 1.4F * p_102868_ / f;
		this.leftLeg.xRot = Mth.cos(p_102867_ * 0.6662F + (float)Math.PI) * 1.4F * p_102868_ / f;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;
		if (this.riding) {
			this.rightArm.xRot += (-(float)Math.PI / 5F);
			this.leftArm.xRot += (-(float)Math.PI / 5F);
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean flag2 = p_102866_.getMainArm() == HumanoidArm.RIGHT;
		if (p_102866_.isUsingItem()) {
			boolean flag3 = p_102866_.getUsedItemHand() == InteractionHand.MAIN_HAND;
			if (flag3 == flag2) {
				this.poseRightArm(p_102866_);
			} else {
				this.poseLeftArm(p_102866_);
			}
		} else {
			boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
			if (flag2 != flag4) {
				this.poseLeftArm(p_102866_);
				this.poseRightArm(p_102866_);
			} else {
				this.poseRightArm(p_102866_);
				this.poseLeftArm(p_102866_);
			}
		}

		this.setupAttackAnimation(p_102866_, p_102869_);
		if (this.crouching) {
			this.body.xRot = 0.5F;
			this.rightArm.xRot += 0.4F;
			this.leftArm.xRot += 0.4F;
			this.rightLeg.z = 4.0F;
			this.leftLeg.z = 4.0F;
			this.rightLeg.y = 12.2F;
			this.leftLeg.y = 12.2F;
			this.head.y = 4.2F;
			this.body.y = 3.2F;
			this.leftArm.y = 5.2F;
			this.rightArm.y = 5.2F;
		} else {
			this.body.xRot = 0.0F;
			this.rightLeg.z = 0.1F;
			this.leftLeg.z = 0.1F;
			this.rightLeg.y = 12.0F;
			this.leftLeg.y = 12.0F;
			this.head.y = 0.0F;
			this.body.y = 0.0F;
			this.leftArm.y = 2.0F;
			this.rightArm.y = 2.0F;
		}

		if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
			AnimationUtils.bobModelPart(this.rightArm, p_102869_, 1.0F);
		}

		if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
			AnimationUtils.bobModelPart(this.leftArm, p_102869_, -1.0F);
		}

		if (this.swimAmount > 0.0F) {
			float f5 = p_102867_ % 26.0F;
			HumanoidArm humanoidarm = this.getAttackArm(p_102866_);
			float f1 = humanoidarm == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			float f2 = humanoidarm == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			if (!p_102866_.isUsingItem()) {
				if (f5 < 14.0F) {
					this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, 0.0F);
					this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, 0.0F);
					this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float)Math.PI);
					this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float)Math.PI);
					this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float)Math.PI + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
					this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float)Math.PI - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
				} else if (f5 >= 14.0F && f5 < 22.0F) {
					float f6 = (f5 - 14.0F) / 8.0F;
					this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, ((float)Math.PI / 2F) * f6);
					this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, ((float)Math.PI / 2F) * f6);
					this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float)Math.PI);
					this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float)Math.PI);
					this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
					this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
				} else if (f5 >= 22.0F && f5 < 26.0F) {
					float f3 = (f5 - 22.0F) / 4.0F;
					this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f3);
					this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f3);
					this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float)Math.PI);
					this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float)Math.PI);
					this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float)Math.PI);
					this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float)Math.PI);
				}
			}

			float f7 = 0.3F;
			float f4 = 0.33333334F;
			this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos(p_102867_ * 0.33333334F + (float)Math.PI));
			this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos(p_102867_ * 0.33333334F));
		}
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	private void poseRightArm(T p_102876_) {
		switch (this.rightArmPose) {
			case EMPTY -> this.rightArm.yRot = 0.0F;
			case BLOCK -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
				this.rightArm.yRot = (-(float) Math.PI / 6F);
			}
			case ITEM -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
				this.rightArm.yRot = 0.0F;
			}
			case THROW_SPEAR -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
				this.rightArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
				this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			}
			case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102876_, true);
			case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
			case SPYGLASS -> {
				this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102876_.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.rightArm.yRot = this.head.yRot - 0.2617994F;
			}
			case TOOT_HORN -> {
				this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
				this.rightArm.yRot = this.head.yRot - ((float) Math.PI / 6F);
			}
		}

	}

	private void poseLeftArm(T p_102879_) {
		switch (this.leftArmPose) {
			case EMPTY -> this.leftArm.yRot = 0.0F;
			case BLOCK -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
				this.leftArm.yRot = ((float) Math.PI / 6F);
			}
			case ITEM -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
				this.leftArm.yRot = 0.0F;
			}
			case THROW_SPEAR -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
				this.leftArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
				this.leftArm.yRot = 0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			}
			case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102879_, false);
			case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
			case SPYGLASS -> {
				this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102879_.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.leftArm.yRot = this.head.yRot + 0.2617994F;
			}
			case TOOT_HORN -> {
				this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
				this.leftArm.yRot = this.head.yRot + ((float) Math.PI / 6F);
			}
		}

	}

	protected void setupAttackAnimation(T p_102858_, float p_102859_) {
		if (!(this.attackTime <= 0.0F)) {
			HumanoidArm humanoidarm = this.getAttackArm(p_102858_);
			ModelPart modelpart = this.getArm(humanoidarm);
			float f = this.attackTime;
			this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
			if (humanoidarm == HumanoidArm.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
			this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
			this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
			this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			f = 1.0F - this.attackTime;
			f *= f;
			f *= f;
			f = 1.0F - f;
			float f1 = Mth.sin(f * (float)Math.PI);
			float f2 = Mth.sin(this.attackTime * (float)Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			modelpart.xRot -= f1 * 1.2F + f2;
			modelpart.yRot += this.body.yRot * 2.0F;
			modelpart.zRot += Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
		}
	}

	protected float rotlerpRad(float p_102836_, float p_102837_, float p_102838_) {
		float f = (p_102838_ - p_102837_) % ((float)Math.PI * 2F);
		if (f < -(float)Math.PI) {
			f += ((float)Math.PI * 2F);
		}

		if (f >= (float)Math.PI) {
			f -= ((float)Math.PI * 2F);
		}

		return p_102837_ + p_102836_ * f;
	}

	private float quadraticArmUpdate(float p_102834_) {
		return -65.0F * p_102834_ + p_102834_ * p_102834_;
	}

	public void copyPropertiesTo(HumanoidModel<T> p_102873_) {
		super.copyPropertiesTo(p_102873_);
		p_102873_.leftArmPose = this.leftArmPose;
		p_102873_.rightArmPose = this.rightArmPose;
		p_102873_.crouching = this.crouching;
		p_102873_.head.copyFrom(this.head);
		p_102873_.body.copyFrom(this.body);
		p_102873_.rightArm.copyFrom(this.rightArm);
		p_102873_.leftArm.copyFrom(this.leftArm);
		p_102873_.rightLeg.copyFrom(this.rightLeg);
		p_102873_.leftLeg.copyFrom(this.leftLeg);
	}

	public void setAllVisible(boolean p_102880_) {
		this.head.visible = p_102880_;
		this.body.visible = p_102880_;
		this.rightArm.visible = p_102880_;
		this.leftArm.visible = p_102880_;
		this.rightLeg.visible = p_102880_;
		this.leftLeg.visible = p_102880_;
	}

	public void translateToHand(@NotNull HumanoidArm p_102854_, @NotNull PoseStack p_102855_) {
		this.getArm(p_102854_).translateAndRotate(p_102855_);
	}

	protected ModelPart getArm(HumanoidArm p_102852_) {
		return p_102852_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
	}

	public @NotNull ModelPart getHead() {
		return this.head;
	}

	private HumanoidArm getAttackArm(T p_102857_) {
		HumanoidArm humanoidarm = p_102857_.getMainArm();
		return p_102857_.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
	}

}
