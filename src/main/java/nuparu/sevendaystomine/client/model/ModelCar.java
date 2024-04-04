package nuparu.sevendaystomine.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.GuiPlayerUI;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.util.MathUtils;

@SideOnly(Side.CLIENT)
public class ModelCar extends ModelBase {

	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer frontLeftDoor;
	private final ModelRenderer bone5;
	private final ModelRenderer frontRightDoor;
	private final ModelRenderer bone7;
	private final ModelRenderer backLeftDoor;
	private final ModelRenderer bone9;
	private final ModelRenderer backRightDoor;
	private final ModelRenderer bone11;
	private final ModelRenderer steering_wheel;
	public final ModelRenderer steering_wheel_axis;
	private final ModelRenderer bone13;
	private final ModelRenderer frontWheels;
	private final ModelRenderer rearWheels;
	private final ModelRenderer engine;
	private final ModelRenderer seats;

	public ModelCar() {
		textureWidth = 176;
		textureHeight = 176;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 16.0F, -31.0F);
		setRotationAngle(bone, 0.2618F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 77, 39, -14.0F, -3.8978F, -6.2235F, 28, 2, 11, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(13.0F, 13.5F, -14.0F);
		setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 8, 39, -1.01F, -18.6213F, -3.1213F, 2, 19, 2, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 39, -26.99F, -18.6213F, -3.1213F, 2, 19, 2, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(13.0F, 15.9F, 29.0F);
		setRotationAngle(bone3, 0.6981F, 0.0F, 0.0F);
		bone3.cubeList.add(new ModelBox(bone3, 30, 147, -1.01F, -20.6207F, 0.4575F, 2, 18, 2, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 18, 127, -25.0F, -7.6207F, 0.4575F, 24, 5, 2, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 22, 147, -26.99F, -20.6207F, 0.4575F, 2, 18, 2, 0.0F, false));

		frontLeftDoor = new ModelRenderer(this);
		frontLeftDoor.setRotationPoint(13.5F, 22.5F, -13.0F);
		frontLeftDoor.cubeList.add(new ModelBox(frontLeftDoor, 0, 0, -0.5F, -11.5F, 0.0F, 1, 9, 18, 0.0F, false));
		frontLeftDoor.cubeList.add(new ModelBox(frontLeftDoor, 67, 62, -0.5F, -21.5F, 16.0F, 1, 10, 1, 0.0F, false));
		frontLeftDoor.cubeList.add(new ModelBox(frontLeftDoor, 0, 0, -0.5F, -22.5F, 11.0F, 1, 1, 7, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -16.5F, 8.5F);
		frontLeftDoor.addChild(bone5);
		setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
		bone5.cubeList.add(new ModelBox(bone5, 24, 0, -0.5F, -6.1213F, -2.6213F, 1, 16, 1, 0.0F, false));

		frontRightDoor = new ModelRenderer(this);
		frontRightDoor.setRotationPoint(-13.5F, 22.5F, -13.0F);
		frontRightDoor.cubeList.add(new ModelBox(frontRightDoor, 110, 110, -0.5F, -11.5F, 0.0F, 1, 9, 17, 0.0F, false));
		frontRightDoor.cubeList.add(new ModelBox(frontRightDoor, 4, 62, -0.5F, -21.5F, 16.0F, 1, 10, 1, 0.0F, false));
		frontRightDoor.cubeList.add(new ModelBox(frontRightDoor, 0, 8, -0.5F, -22.5F, 11.0F, 1, 1, 6, 0.0F, false));

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, -16.5F, 8.5F);
		frontRightDoor.addChild(bone7);
		setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
		bone7.cubeList.add(new ModelBox(bone7, 20, 0, -0.5F, -6.1213F, -2.6213F, 1, 16, 1, 0.0F, false));

		backLeftDoor = new ModelRenderer(this);
		backLeftDoor.setRotationPoint(13.5F, 22.5F, 2.0F);
		backLeftDoor.cubeList.add(new ModelBox(backLeftDoor, 55, 127, -0.5F, -11.5F, 3.0F, 1, 9, 15, 0.0F, false));
		backLeftDoor.cubeList.add(new ModelBox(backLeftDoor, 0, 62, -0.5F, -21.5F, 2.0F, 1, 10, 1, 0.0F, false));
		backLeftDoor.cubeList.add(new ModelBox(backLeftDoor, 22, 134, -0.5F, -22.5F, 3.0F, 1, 1, 12, 0.0F, false));

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, -16.5F, 11.5F);
		backLeftDoor.addChild(bone9);
		setRotationAngle(bone9, 0.6981F, 0.0F, 0.0F);
		bone9.cubeList.add(new ModelBox(bone9, 0, 121, -0.52F, -3.1852F, 5.0F, 1, 12, 1, 0.0F, false));

		backRightDoor = new ModelRenderer(this);
		backRightDoor.setRotationPoint(-13.5F, 22.5F, 2.0F);
		backRightDoor.cubeList.add(new ModelBox(backRightDoor, 0, 121, -0.5F, -11.5F, 2.0F, 1, 9, 16, 0.0F, false));
		backRightDoor.cubeList.add(new ModelBox(backRightDoor, 31, 27, -0.5F, -21.5F, 2.0F, 1, 10, 1, 0.0F, false));
		backRightDoor.cubeList.add(new ModelBox(backRightDoor, 133, 133, -0.5F, -22.5F, 2.0F, 1, 1, 13, 0.0F, false));

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, -16.5F, 7.5F);
		backRightDoor.addChild(bone11);
		setRotationAngle(bone11, 0.6981F, 0.0F, 0.0F);
		bone11.cubeList.add(new ModelBox(bone11, 16, 39, -0.48F, -0.1852F, 8.0F, 1, 12, 1, 0.0F, false));

		steering_wheel = new ModelRenderer(this);
		steering_wheel.setRotationPoint(7.0F, 13.0425F, -11.5293F);
		setRotationAngle(steering_wheel, 0.6109F, 0.0F, 0.0F);
		

		steering_wheel_axis = new ModelRenderer(this);
		steering_wheel_axis.setRotationPoint(0.0F, 0.0F, 0.0F);
		steering_wheel.addChild(steering_wheel_axis);
		steering_wheel_axis.cubeList.add(new ModelBox(steering_wheel_axis, 0, 0, -0.5F, -0.5F, -1.75F, 1, 1, 2, 0.0F, false));
		steering_wheel_axis.cubeList.add(new ModelBox(steering_wheel_axis, 8, 8, -1.5F, -1.5F, 0.25F, 3, 3, 1, 0.0F, false));

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone13.cubeList.add(new ModelBox(bone13, 0, 39, -14.0F, -26.0F, -3.5F, 28, 2, 21, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 72, 82, -14.0F, -12.0F, 21.0F, 28, 3, 10, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 67, 67, -14.0F, -13.0F, -28.0F, 28, 4, 11, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 94, 0, -11.0F, -9.0F, 21.0F, 22, 6, 10, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 94, 16, -11.0F, -9.0F, -27.0F, 22, 6, 10, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 95, -14.0F, -15.0F, 20.0F, 28, 3, 6, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 0, -14.0F, -4.0F, -17.0F, 28, 1, 38, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 62, -14.0F, -8.0F, -38.0F, 28, 5, 11, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 27, -6.0F, -8.0F, -39.0F, 12, 1, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 28, 0, -7.0F, -11.0F, -40.0F, 1, 7, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 11, 32, -13.0F, -9.0F, -39.0F, 5, 3, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 29, 8.0F, -9.0F, -39.0F, 5, 3, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 23, 27, 6.0F, -11.0F, -40.0F, 1, 7, 3, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 104, -14.0F, -11.0F, -32.0F, 28, 3, 5, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 61, 108, -14.0F, -10.0F, -37.0F, 28, 2, 5, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 94, 32, -14.0F, -10.0F, -38.0F, 28, 2, 1, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 78, -14.0F, -12.0F, 31.0F, 28, 9, 8, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 68, 95, -14.0F, -13.0F, -17.0F, 28, 9, 4, 0.0F, false));
		bone13.cubeList.add(new ModelBox(bone13, 0, 112, -14.0F, -12.0F, 20.0F, 28, 8, 1, 0.0F, false));

		frontWheels = new ModelRenderer(this);
		frontWheels.setRotationPoint(0.0F, 20.0F, -22.0F);
		frontWheels.cubeList.add(new ModelBox(frontWheels, 0, 146, 11.0F, -4.0F, -4.0F, 3, 8, 8, 0.0F, false));
		frontWheels.cubeList.add(new ModelBox(frontWheels, 87, 140, -14.0F, -4.0F, -4.0F, 3, 8, 8, 0.0F, false));

		rearWheels = new ModelRenderer(this);
		rearWheels.setRotationPoint(0.0F, 20.0F, 26.0F);
		rearWheels.cubeList.add(new ModelBox(rearWheels, 144, 32, 11.0F, -4.0F, -4.0F, 3, 8, 8, 0.0F, false));
		rearWheels.cubeList.add(new ModelBox(rearWheels, 40, 143, -14.0F, -4.0F, -4.0F, 3, 8, 8, 0.0F, false));

		engine = new ModelRenderer(this);
		engine.setRotationPoint(0.0F, 24.0F, 0.0F);
		engine.cubeList.add(new ModelBox(engine, 129, 111, -7.0F, -4.0F, 30.0F, 2, 2, 12, 0.0F, false));

		seats = new ModelRenderer(this);
		seats.setRotationPoint(0.0F, 24.0F, 0.0F);
		seats.cubeList.add(new ModelBox(seats, 129, 99, -12.0F, -7.0F, -7.0F, 10, 3, 9, 0.0F, false));
		seats.cubeList.add(new ModelBox(seats, 72, 127, 2.0F, -7.0F, -7.0F, 10, 3, 9, 0.0F, false));
		seats.cubeList.add(new ModelBox(seats, 134, 65, 2.0F, -16.0F, 0.0F, 10, 9, 3, 0.0F, false));
		seats.cubeList.add(new ModelBox(seats, 107, 136, -12.0F, -16.0F, 0.0F, 10, 9, 3, 0.0F, false));
		seats.cubeList.add(new ModelBox(seats, 89, 53, -12.0F, -7.0F, 6.0F, 24, 3, 9, 0.0F, false));
		seats.cubeList.add(new ModelBox(seats, 58, 115, -12.0F, -16.0F, 13.0F, 24, 9, 3, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (entity instanceof EntityCar) {
			Minecraft mc = Minecraft.getMinecraft();
			
			EntityCar car = (EntityCar) entity;
			bone.render(f5);
			bone2.render(f5);
			bone3.render(f5);
			frontLeftDoor.render(f5);
			frontRightDoor.render(f5);
			backLeftDoor.render(f5);
			backRightDoor.render(f5);
			bone13.render(f5);
			if (car.getWheelsFront()) {
				frontWheels.render(f5);
			}
			if (car.getWheelsBack()) {
				rearWheels.render(f5);
			}
			if (car.getEngine()) {
				engine.render(f5);
			}
			if (car.getSeat()) {
				seats.render(f5);
			}
			if(car.getHandles()) {
				getSteering_wheel().render(f5);
			}
			
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.translate(-0.3, 0.75, 0.8075);
			GlStateManager.scale(f5/10, f5/10, f5/10);
			mc.fontRenderer.drawString(MathUtils.roundToNDecimal(MathUtils.getSpeedKilometersPerHour(entity),1) + " km/h", 0, 0, 0x000000);

			GlStateManager.translate(0, 15, 0);
			mc.fontRenderer.drawString((int)Math.round((car.getHealth()/car.getMaxHealth())*100) + "%", 0, 0, 0x000000);
			GlStateManager.translate(0, -15, 0);
			//mc.fontRenderer.drawString(car.getFuel()+"", 0, 0, 0x000000);

			GlStateManager.translate(0,10,45);
			GlStateManager.scale(0.5,0.5,0.5);
			GlStateManager.pushMatrix();
			mc.renderEngine.bindTexture(GuiPlayerUI.UI_TEX);
			float fuel = car.getFuel();
			mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 22,
					(int) Math.floor(fuel / (10f * (EntityMinibike.MAX_FUEL / 780f))), 6);
			mc.ingameGUI.drawTexturedModalRect(- 1,- 1, 0, 0, 81, 8);
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public ModelRenderer getFrontWheels() {
		return frontWheels;
	}

	public ModelRenderer getRearWheels() {
		return rearWheels;
	}

	public ModelRenderer getSteering_wheel() {
		return steering_wheel;
	}

}