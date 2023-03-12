package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import nuparu.sevendaystomine.world.entity.zombie.SpiderZombieEntity;
import org.jetbrains.annotations.NotNull;

public class SpiderZombieModel<T extends SpiderZombieEntity> extends EntityModel<T> implements ArmedModel, HeadedModel {

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLegUp;
	private final ModelPart leftLegUp;

	public SpiderZombieModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
		this.rightLegUp = root.getChild("rightLegUp");
		this.leftLegUp = root.getChild("leftLegUp");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, -5.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).mirror().addBox(-4.0F, -11.5275F, 0.0291F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 13.0F, 6.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-4.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 11.0F, -4.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).addBox(0.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 11.0F, -4.0F));

		PartDefinition rightLegUp = partdefinition.addOrReplaceChild("rightLegUp", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 12.0F, 6.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition rightLegDown = rightLegUp.addOrReplaceChild("rightLegDown", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 2.0F));

		PartDefinition leftLegUp = partdefinition.addOrReplaceChild("leftLegUp", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 12.0F, 6.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition leftLegDown = leftLegUp.addOrReplaceChild("leftLegDown", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLegUp.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLegUp.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}


	public void setAllVisible(boolean p_102880_) {
		this.head.visible = p_102880_;
		this.body.visible = p_102880_;
		this.rightArm.visible = p_102880_;
		this.leftArm.visible = p_102880_;
		this.rightLegUp.visible = p_102880_;
		this.leftLegUp.visible = p_102880_;
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

}
