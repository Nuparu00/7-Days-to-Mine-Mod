package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.client.renderer.entity.TextModelPart;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.entity.item.MinibikeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MinibikeModel<T extends MinibikeEntity> extends EntityModel<T> {

    private final ModelPart Seat;
    private final ModelPart Battery;
    private final ModelPart Chassis12;
    private final ModelPart Chassis11;
    private final ModelPart Front;
    private final ModelPart Chassis10;
    private final ModelPart Chassis4;
    private final ModelPart Chassis9;
    private final ModelPart Chassis5;
    private final ModelPart Chassis8;
    private final ModelPart Chassis6;
    private final ModelPart Chassis7;
    private final ModelPart Engine1;
    private final ModelPart Engine2;
    private final ModelPart Engine3;
    private final ModelPart Backwheel;
    private final ModelPart Chest;
    private final ModelPart Frontwheel;
    private final ModelPart Handle1;
    private final ModelPart Handle2;
    private final ModelPart bone;

    private final TextModelPart fuelMeter;
    private final TextModelPart speedMeter;

    public MinibikeModel(ModelPart root) {
        this.Seat = root.getChild("Seat");
        this.Battery = root.getChild("Battery");
        this.Chassis12 = root.getChild("Chassis12");
        this.Chassis11 = root.getChild("Chassis11");
        this.Front = root.getChild("Front");
        this.Chassis10 = root.getChild("Chassis10");
        this.Chassis4 = root.getChild("Chassis4");
        this.Chassis9 = root.getChild("Chassis9");
        this.Chassis5 = root.getChild("Chassis5");
        this.Chassis8 = root.getChild("Chassis8");
        this.Chassis6 = root.getChild("Chassis6");
        this.Chassis7 = root.getChild("Chassis7");
        this.Engine1 = root.getChild("Engine1");
        this.Engine2 = root.getChild("Engine2");
        this.Engine3 = root.getChild("Engine3");
        this.Backwheel = root.getChild("Backwheel");
        this.Chest = root.getChild("Chest");
        this.Frontwheel = Front.getChild("Frontwheel");
        this.Handle1 = Front.getChild("Handle1");
        this.Handle2 = Front.getChild("Handle2");
        this.bone = Handle2.getChild("bone");

        fuelMeter = new TextModelPart(0.012,-0.10,-0.0001,180,180,-45);
        speedMeter = new TextModelPart(0.012,-0.06,-0.0001,180,180,-45);

        Map<String, ModelPart> children = ObfuscationReflectionHelper.getPrivateValue(ModelPart.class,this.bone,"f_104213_");
        children.put("fuelMeter",fuelMeter);
        children.put("speedMeter",speedMeter);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Seat = partdefinition.addOrReplaceChild("Seat", CubeListBuilder.create().texOffs(15, 45).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 11.0F, -2.0F));

        PartDefinition Battery = partdefinition.addOrReplaceChild("Battery", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 14.0F, -6.0F));

        PartDefinition Chassis12 = partdefinition.addOrReplaceChild("Chassis12", CubeListBuilder.create().texOffs(21, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 21.0F, 6.0F));

        PartDefinition Chassis11 = partdefinition.addOrReplaceChild("Chassis11", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-1.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 14.0F, -8.0F, 0.6283F, -0.5236F, 0.0F));

        PartDefinition Front = partdefinition.addOrReplaceChild("Front", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.5F, -1.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 14.0F, -7.0F));

        PartDefinition Frontwheel = Front.addOrReplaceChild("Frontwheel", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-1.5F, -2.5F, -2.5F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.5F, -2.5F));

        PartDefinition Chassis2 = Front.addOrReplaceChild("Chassis2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -8.0F, -0.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition Chassis1 = Front.addOrReplaceChild("Chassis1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, -8.0F, -0.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition Handle1 = Front.addOrReplaceChild("Handle1", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, -8.0F, -0.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition Handle2 = Front.addOrReplaceChild("Handle2", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.5F, -8.0F, -0.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bone = Handle2.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 61).addBox(-0.5F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition Chassis10 = partdefinition.addOrReplaceChild("Chassis10", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 14.0F, -8.0F, 0.6283F, 0.5236F, 0.0F));

        PartDefinition Chassis4 = partdefinition.addOrReplaceChild("Chassis4", CubeListBuilder.create().texOffs(19, 2).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 13.0F, -8.0F, 0.0F, 0.1745F, 0.0F));

        PartDefinition Chassis9 = partdefinition.addOrReplaceChild("Chassis9", CubeListBuilder.create().texOffs(5, 3).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 21.0F, -3.0F));

        PartDefinition Chassis5 = partdefinition.addOrReplaceChild("Chassis5", CubeListBuilder.create().texOffs(19, 2).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 13.0F, -8.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition Chassis8 = partdefinition.addOrReplaceChild("Chassis8", CubeListBuilder.create().texOffs(5, 3).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5333F, 21.0F, -3.0F));

        PartDefinition Chassis6 = partdefinition.addOrReplaceChild("Chassis6", CubeListBuilder.create().texOffs(5, 20).mirror().addBox(0.0F, 0.0F, 15.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 15.5F, -8.0F, 0.1571F, 0.1745F, 0.0F));

        PartDefinition Chassis7 = partdefinition.addOrReplaceChild("Chassis7", CubeListBuilder.create().texOffs(5, 20).mirror().addBox(0.0F, 0.0F, 15.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 15.5F, -8.0F, 0.1571F, -0.1745F, 0.0F));

        PartDefinition Engine1 = partdefinition.addOrReplaceChild("Engine1", CubeListBuilder.create().texOffs(10, 17).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 17.0F, -3.0F));

        PartDefinition Engine2 = partdefinition.addOrReplaceChild("Engine2", CubeListBuilder.create().texOffs(17, 22).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 18.0F, -3.0F));

        PartDefinition Engine3 = partdefinition.addOrReplaceChild("Engine3", CubeListBuilder.create().texOffs(10, 30).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 18.4F, 0.0F));

        PartDefinition Backwheel = partdefinition.addOrReplaceChild("Backwheel", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-1.5F, -2.5F, -2.5F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 21.5F, 6.5F));

        PartDefinition Chest = partdefinition.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(16, 32).mirror().addBox(-4.0F, -6.0F, 7.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 13.0F, -3.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T minibike, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float partialTicks) {
        float rotation = minibike.wheelAnglePrev + (minibike.wheelAngle - minibike.wheelAnglePrev) * partialTicks;
        if (rotation != 0) {
            setRotationAngle(Frontwheel, rotation, 0, 0);
            setRotationAngle(Backwheel, rotation, 0, 0);
        }
        float frontRotation = MathUtils.lerp(partialTicks,minibike.getFrontRotationPrev(), minibike.getFrontRotation()) * 0.0174533f * 90.5f;
        //-Utils.lerp(minibike.getTurningPrev(),minibike.getTurning(),partialTicks)*0.0174533f*1.5f,0)

        fuelMeter.setText((int)(Math.ceil((minibike.getFuel()/MinibikeEntity.MAX_FUEL)*100f))+"%");
        double speed = minibike.kmh;
        //If the speed is below 1.0, we are interested only in the fist 2 decimal digits
        if(speed < 1){
            speed = MathUtils.roundToNDecimal(speed,2);
        }
        //Otherwise we wan to first 3 significant digits
        else{
            speed = MathUtils.roundToSignificantFigures(speed,3);
        }
        speedMeter.setText(speed+"");
        setRotationAngle(Front, 0f, frontRotation, 0);


        Handle1.visible = minibike.getHandles();
        Handle2.visible = minibike.getHandles();
        Frontwheel.visible = minibike.getWheels();
        Backwheel.visible = minibike.getWheels();
        Battery.visible = minibike.getBattery();
        Seat.visible = minibike.getSeat();
        Engine1.visible = minibike.getEngine();
        Engine2.visible = minibike.getEngine();
        Engine3.visible = minibike.getEngine();
        Chest.visible = minibike.getChest();
    }

    private void setRotationAngle(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Battery.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis12.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis11.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Front.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis10.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis9.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chassis7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Engine1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Engine2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Engine3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Backwheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}