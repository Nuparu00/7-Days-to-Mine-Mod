package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import nuparu.sevendaystomine.world.entity.item.AirdropEntity;
import org.jetbrains.annotations.NotNull;

public class AirdropModel<T extends AirdropEntity> extends EntityModel<T> {
    private final ModelPart parachute;
    private final ModelPart bb_main;

    public AirdropModel(ModelPart root) {
        this.parachute = root.getChild("parachute");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition parachute = partdefinition.addOrReplaceChild("parachute", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition ropes1 = parachute.addOrReplaceChild("ropes1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = ropes1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 77).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -16.0F, -6.5F, 0.0873F, 0.0F, 0.1571F));

        PartDefinition cube_r2 = ropes1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -16.0F, -6.5F, 0.082F, -0.0298F, 0.5049F));

        PartDefinition tarp = parachute.addOrReplaceChild("tarp", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -49.0F, -10.0F, 24.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = tarp.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(45, 35).addBox(0.0F, 0.0F, -10.0F, 12.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, -49.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r4 = tarp.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 55).addBox(-12.0F, 0.0F, -10.0F, 12.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -49.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition ropes2 = parachute.addOrReplaceChild("ropes2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 13.0F));

        PartDefinition cube_r5 = ropes2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(75, 57).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -16.0F, -6.5F, -0.0873F, 0.0F, 0.1571F));

        PartDefinition cube_r6 = ropes2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(15, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -16.0F, -6.5F, -0.082F, -0.0298F, 0.5049F));

        PartDefinition ropes3 = parachute.addOrReplaceChild("ropes3", CubeListBuilder.create(), PartPose.offset(-13.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = ropes3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(70, 57).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -16.0F, -6.5F, 0.0873F, 0.0F, -0.1571F));

        PartDefinition cube_r8 = ropes3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(10, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -16.0F, -6.5F, 0.082F, -0.0298F, -0.5049F));

        PartDefinition ropes4 = parachute.addOrReplaceChild("ropes4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 13.0F));

        PartDefinition cube_r9 = ropes4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(65, 57).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -16.0F, -6.5F, -0.0873F, 0.0F, -0.1571F));

        PartDefinition cube_r10 = ropes4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(5, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -16.0F, -6.5F, -0.082F, -0.0298F, -0.5049F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 22).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        parachute.visible = (!entity.onGround() && !entity.getLanded());
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        parachute.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
