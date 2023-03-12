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
import nuparu.sevendaystomine.world.entity.item.CarEntity;
import nuparu.sevendaystomine.world.entity.item.MinibikeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CarModel<T extends CarEntity> extends EntityModel<T> {
    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart bone3;
    private final ModelPart frontLeftDoor;
    private final ModelPart frontRightDoor;
    private final ModelPart backLeftDoor;
    private final ModelPart backRightDoor;
    private final ModelPart steering_wheel;
    private final ModelPart steering_wheel_axis;
    private final ModelPart bone13;
    private final ModelPart frontWheels;
    private final ModelPart rearWheels;
    private final ModelPart engine;
    private final ModelPart seats;
    private final ModelPart meters;

    private final TextModelPart fuelMeter;
    private final TextModelPart speedMeter;

    public CarModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.bone2 = root.getChild("bone2");
        this.bone3 = root.getChild("bone3");
        this.frontLeftDoor = root.getChild("frontLeftDoor");
        this.frontRightDoor = root.getChild("frontRightDoor");
        this.backLeftDoor = root.getChild("backLeftDoor");
        this.backRightDoor = root.getChild("backRightDoor");
        this.steering_wheel = root.getChild("steering_wheel");
        this.steering_wheel_axis = steering_wheel.getChild("steering_wheel_axis");
        this.bone13 = root.getChild("bone13");
        this.frontWheels = root.getChild("frontWheels");
        this.rearWheels = root.getChild("rearWheels");
        this.engine = root.getChild("engine");
        this.seats = root.getChild("seats");
        this.meters = root.getChild("meters");

        fuelMeter = new TextModelPart(0,0,-0.01,180,180,0);
        fuelMeter.scale = 0.0075f;
        speedMeter = new TextModelPart(0,-0.06,-0.01,180,180,0);
        speedMeter.scale = 0.0075f;

        Map<String, ModelPart> children = ObfuscationReflectionHelper.getPrivateValue(ModelPart.class,this.meters,"f_104213_");
        children.put("fuelMeter",fuelMeter);
        children.put("speedMeter",speedMeter);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(77, 39).addBox(-14.0F, -3.8978F, -6.2235F, 28.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -31.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(8, 39).addBox(-1.01F, -18.6213F, -3.1213F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 39).addBox(-26.99F, -18.6213F, -3.1213F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 13.5F, -14.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(30, 147).addBox(-1.01F, -20.6207F, 0.4575F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 127).addBox(-25.0F, -7.6207F, 0.4575F, 24.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(22, 147).addBox(-26.99F, -20.6207F, 0.4575F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 15.9F, 29.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition frontLeftDoor = partdefinition.addOrReplaceChild("frontLeftDoor", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(67, 62).addBox(-0.5F, -21.5F, 16.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -22.5F, 11.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(13.5F, 22.5F, -13.0F));

        PartDefinition bone5 = frontLeftDoor.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -6.1213F, -2.6213F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.5F, 8.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition frontRightDoor = partdefinition.addOrReplaceChild("frontRightDoor", CubeListBuilder.create().texOffs(110, 110).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(4, 62).addBox(-0.5F, -21.5F, 16.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-0.5F, -22.5F, 11.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.5F, 22.5F, -13.0F));

        PartDefinition bone7 = frontRightDoor.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, -6.1213F, -2.6213F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.5F, 8.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition backLeftDoor = partdefinition.addOrReplaceChild("backLeftDoor", CubeListBuilder.create().texOffs(55, 127).addBox(-0.5F, -11.5F, 3.0F, 1.0F, 9.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 62).addBox(-0.5F, -21.5F, 2.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 134).addBox(-0.5F, -22.5F, 3.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.5F, 22.5F, 2.0F));

        PartDefinition bone9 = backLeftDoor.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(0, 121).addBox(-0.52F, -3.1852F, 5.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.5F, 11.5F, 0.6981F, 0.0F, 0.0F));

        PartDefinition backRightDoor = partdefinition.addOrReplaceChild("backRightDoor", CubeListBuilder.create().texOffs(0, 121).addBox(-0.5F, -11.5F, 2.0F, 1.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(31, 27).addBox(-0.5F, -21.5F, 2.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(133, 133).addBox(-0.5F, -22.5F, 2.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.5F, 22.5F, 2.0F));

        PartDefinition bone11 = backRightDoor.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(16, 39).addBox(-0.48F, -0.1852F, 8.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.5F, 7.5F, 0.6981F, 0.0F, 0.0F));

        PartDefinition steering_wheel = partdefinition.addOrReplaceChild("steering_wheel", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, 13.0425F, -11.5293F, 0.6109F, 0.0F, 0.0F));

        PartDefinition steering_wheel_axis = steering_wheel.addOrReplaceChild("steering_wheel_axis", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 8).addBox(-1.5F, -1.5F, 0.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone13 = partdefinition.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(0, 39).addBox(-14.0F, -26.0F, -3.5F, 28.0F, 2.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(72, 82).addBox(-14.0F, -12.0F, 21.0F, 28.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(67, 67).addBox(-14.0F, -13.0F, -28.0F, 28.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(94, 0).addBox(-11.0F, -9.0F, 21.0F, 22.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(94, 16).addBox(-11.0F, -9.0F, -27.0F, 22.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 95).addBox(-14.0F, -15.0F, 20.0F, 28.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-14.0F, -4.0F, -17.0F, 28.0F, 1.0F, 38.0F, new CubeDeformation(0.0F))
                .texOffs(0, 62).addBox(-14.0F, -8.0F, -38.0F, 28.0F, 5.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-6.0F, -8.0F, -39.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-7.0F, -11.0F, -40.0F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(11, 32).addBox(-13.0F, -9.0F, -39.0F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(8.0F, -9.0F, -39.0F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(23, 27).addBox(6.0F, -11.0F, -40.0F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 104).addBox(-14.0F, -11.0F, -32.0F, 28.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(61, 108).addBox(-14.0F, -10.0F, -37.0F, 28.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(94, 32).addBox(-14.0F, -10.0F, -38.0F, 28.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 78).addBox(-14.0F, -12.0F, 31.0F, 28.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(68, 95).addBox(-14.0F, -13.0F, -17.0F, 28.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 112).addBox(-14.0F, -12.0F, 20.0F, 28.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition frontWheels = partdefinition.addOrReplaceChild("frontWheels", CubeListBuilder.create().texOffs(0, 146).addBox(11.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(87, 140).addBox(-14.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, -22.0F));

        PartDefinition rearWheels = partdefinition.addOrReplaceChild("rearWheels", CubeListBuilder.create().texOffs(144, 32).addBox(11.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 143).addBox(-14.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 26.0F));

        PartDefinition engine = partdefinition.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(129, 111).addBox(-7.0F, -4.0F, 30.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition seats = partdefinition.addOrReplaceChild("seats", CubeListBuilder.create().texOffs(129, 99).addBox(-12.0F, -7.0F, -7.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(72, 127).addBox(2.0F, -7.0F, -7.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(134, 65).addBox(2.0F, -16.0F, 0.0F, 10.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(107, 136).addBox(-12.0F, -16.0F, 0.0F, 10.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(89, 53).addBox(-12.0F, -7.0F, 6.0F, 24.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(58, 115).addBox(-12.0F, -16.0F, 13.0F, 24.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition meters = partdefinition.addOrReplaceChild("meters", CubeListBuilder.create().texOffs(0, 0), PartPose.offset(-5, 12, -13));

        return LayerDefinition.create(meshdefinition, 176, 176);
    }

    @Override
    public void setupAnim(T car, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float partialTicks) {
        float rotation = car.wheelAnglePrev + (car.wheelAngle - car.wheelAnglePrev) * partialTicks;
        if (rotation != 0) {
            setRotationAngle(frontWheels, rotation, 0, 0);
            setRotationAngle(rearWheels, rotation, 0, 0);
        }
        float frontRotation = MathUtils.lerp(car.getFrontRotationPrev(), car.getFrontRotation(), partialTicks) * 0.0174533f * 90.5f;
        //-Utils.lerp(car.getTurningPrev(),car.getTurning(),partialTicks)*0.0174533f*1.5f,0)

        double speed = car.kmh;
        //If the speed is below 1.0, we are interested only in the fist 2 decimal digits
        if(speed < 1){
            speed = MathUtils.roundToNDecimal(speed,2);
        }
        //Otherwise we wan to first 3 significant digits
        else{
            speed = MathUtils.roundToSignificantFigures(speed,3);
        }


        fuelMeter.setText((int)(Math.ceil((car.getFuel()/ MinibikeEntity.MAX_FUEL)*100f))+"%");
        speedMeter.setText(speed+"");
        setRotationAngle(steering_wheel_axis, 0,0, -frontRotation);

        frontWheels.visible = car.getWheelsFront();
        rearWheels.visible = car.getWheelsBack();
        rearWheels.visible = car.getWheelsBack();
        rearWheels.visible = car.getWheelsBack();
        engine.visible = car.getEngine();
        seats.visible = car.getSeat();
        steering_wheel.visible = car.getHandles();
    }

    private void setRotationAngle(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bone3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        frontLeftDoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        frontRightDoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        backLeftDoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        backRightDoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        steering_wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bone13.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        frontWheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rearWheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        engine.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        seats.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        meters.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}