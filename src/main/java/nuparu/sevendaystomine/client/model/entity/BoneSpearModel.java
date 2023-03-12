package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import nuparu.sevendaystomine.SevenDaysToMine;
import org.jetbrains.annotations.NotNull;

public class BoneSpearModel<T extends Entity> extends EntityModel<T> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/bone_spear.png");
    private static MeshDefinition meshdefinition;
    private final ModelPart bone;
    public BoneSpearModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -16.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-1.0F, -25.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 15).addBox(-0.5F, -25.0F, -1.125F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(15, 9).addBox(-0.5F, -30.0F, -1.125F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 11).addBox(-1.0F, -28.0F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-0.5F, -37.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, buffer, packedLight, packedOverlay);
    }
}