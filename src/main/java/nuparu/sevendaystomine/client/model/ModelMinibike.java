package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityMinibike;

@SideOnly(Side.CLIENT)
public class ModelMinibike extends ModelBase {

	public ModelRenderer Seat;
	public ModelRenderer Battery;
	public ModelRenderer Chassis1;
	public ModelRenderer Chassis2;
	public ModelRenderer Chassis12;
	public ModelRenderer Chassis11;
	public ModelRenderer Chassis3;
	public ModelRenderer Chassis10;
	public ModelRenderer Chassis4;
	public ModelRenderer Chassis9;
	public ModelRenderer Chassis5;
	public ModelRenderer Chassis8;
	public ModelRenderer Chassis6;
	public ModelRenderer Chassis7;
	public ModelRenderer Handle1;
	public ModelRenderer Handle2;
	public ModelRenderer Engine1;
	public ModelRenderer Engine2;
	public ModelRenderer Engine3;
	public ModelRenderer Frontwheel;
	public ModelRenderer Backwheel;
	public ModelRenderer Chest;

	public ModelMinibike() {
		textureWidth = 128;
		textureHeight = 64;

		Seat = new ModelRenderer(this, 20, 50);
		Seat.addBox(0F, 0F, 0F, 4, 2, 9);
		Seat.setRotationPoint(-2F, 11F, -2F);
		Seat.setTextureSize(128, 64);
		Seat.mirror = true;
		setRotation(Seat, 0F, 0F, 0F);
		Battery = new ModelRenderer(this, 0, 50);
		Battery.addBox(0F, 0F, 0F, 3, 2, 4);
		Battery.setRotationPoint(-1.5F, 14F, -6F);
		Battery.setTextureSize(128, 64);
		Battery.mirror = true;
		setRotation(Battery, 0F, 0F, 0F);
		Chassis1 = new ModelRenderer(this, 0, 0);
		Chassis1.addBox(0F, 0F, 0F, 1, 16, 1);
		Chassis1.setRotationPoint(1.5F, 6F, -7.5F);
		Chassis1.setTextureSize(128, 64);
		Chassis1.mirror = true;
		setRotation(Chassis1, -0.1745329F, 0F, 0F);
		Chassis2 = new ModelRenderer(this, 0, 0);
		Chassis2.addBox(0F, 0F, 0F, 1, 16, 1);
		Chassis2.setRotationPoint(-2.5F, 6F, -7.5F);
		Chassis2.setTextureSize(128, 64);
		Chassis2.mirror = true;
		setRotation(Chassis2, -0.1745329F, 0F, 0F);
		Chassis12 = new ModelRenderer(this, 40, 0);
		Chassis12.addBox(0F, 0F, 0F, 6, 1, 1);
		Chassis12.setRotationPoint(-2.5F, 21F, 6F);
		Chassis12.setTextureSize(128, 64);
		Chassis12.mirror = true;
		setRotation(Chassis12, 0F, 0F, 0F);
		Chassis11 = new ModelRenderer(this, 0, 20);
		Chassis11.addBox(-1F, 0F, 0F, 1, 10, 1);
		Chassis11.setRotationPoint(0.5F, 14F, -8F);
		Chassis11.setTextureSize(128, 64);
		Chassis11.mirror = true;
		setRotation(Chassis11, 0.6283185F, -0.5235988F, 0F);
		Chassis3 = new ModelRenderer(this, 0, 34);
		Chassis3.addBox(0F, 0F, 0F, 3, 3, 3);
		Chassis3.setRotationPoint(-1.5F, 13F, -10F);
		Chassis3.setTextureSize(128, 64);
		Chassis3.mirror = true;
		setRotation(Chassis3, 0F, 0F, 0F);
		Chassis10 = new ModelRenderer(this, 0, 20);
		Chassis10.addBox(0F, 0F, 0F, 1, 10, 1);
		Chassis10.setRotationPoint(-0.5F, 14F, -8F);
		Chassis10.setTextureSize(128, 64);
		Chassis10.mirror = true;
		setRotation(Chassis10, 0.6283185F, 0.5235988F, 0F);
		Chassis4 = new ModelRenderer(this, 70, 0);
		Chassis4.addBox(0F, 0F, 0F, 1, 1, 16);
		Chassis4.setRotationPoint(-0.5F, 13F, -8F);
		Chassis4.setTextureSize(128, 64);
		Chassis4.mirror = true;
		setRotation(Chassis4, 0F, 0.1745329F, 0F);
		Chassis9 = new ModelRenderer(this, 34, 3);
		Chassis9.addBox(0F, 0F, 0F, 1, 1, 12);
		Chassis9.setRotationPoint(-3.5F, 21F, -3F);
		Chassis9.setTextureSize(128, 64);
		Chassis9.mirror = true;
		setRotation(Chassis9, 0F, 0F, 0F);
		Chassis5 = new ModelRenderer(this, 70, 0);
		Chassis5.addBox(0F, 0F, 0F, 1, 1, 16);
		Chassis5.setRotationPoint(-0.5F, 13F, -8F);
		Chassis5.setTextureSize(128, 64);
		Chassis5.mirror = true;
		setRotation(Chassis5, 0F, -0.1745329F, 0F);
		Chassis8 = new ModelRenderer(this, 34, 3);
		Chassis8.addBox(0F, 0F, 0F, 1, 1, 12);
		Chassis8.setRotationPoint(2.533333F, 21F, -3F);
		Chassis8.setTextureSize(128, 64);
		Chassis8.mirror = true;
		setRotation(Chassis8, 0F, 0F, 0F);
		Chassis6 = new ModelRenderer(this, 5, 20);
		Chassis6.addBox(0F, 0F, 15F, 1, 9, 1);
		Chassis6.setRotationPoint(-0.5F, 15.5F, -8F);
		Chassis6.setTextureSize(128, 64);
		Chassis6.mirror = true;
		setRotation(Chassis6, 0.1570796F, 0.1745329F, 0F);
		Chassis7 = new ModelRenderer(this, 5, 20);
		Chassis7.addBox(0F, 0F, 15F, 1, 9, 1);
		Chassis7.setRotationPoint(-0.5F, 15.5F, -8F);
		Chassis7.setTextureSize(128, 64);
		Chassis7.mirror = true;
		setRotation(Chassis7, 0.1570796F, -0.1745329F, 0F);
		Handle1 = new ModelRenderer(this, 10, 0);
		Handle1.addBox(0F, 0F, 0F, 4, 1, 1);
		Handle1.setRotationPoint(2.5F, 6F, -7.5F);
		Handle1.setTextureSize(128, 64);
		Handle1.mirror = true;
		setRotation(Handle1, -0.1745329F, 0F, 0F);
		Handle2 = new ModelRenderer(this, 10, 0);
		Handle2.addBox(0F, 0F, 0F, 4, 1, 1);
		Handle2.setRotationPoint(-6.5F, 6F, -7.5F);
		Handle2.setTextureSize(128, 64);
		Handle2.mirror = true;
		setRotation(Handle2, -0.1745329F, 0F, 0F);
		Engine1 = new ModelRenderer(this, 30, 30);
		Engine1.addBox(0F, 0F, 0F, 1, 5, 5);
		Engine1.setRotationPoint(2F, 17F, -3F);
		Engine1.setTextureSize(128, 64);
		Engine1.mirror = true;
		setRotation(Engine1, 0F, 0F, 0F);
		Engine2 = new ModelRenderer(this, 50, 30);
		Engine2.addBox(0F, 0F, 0F, 4, 3, 6);
		Engine2.setRotationPoint(-2F, 18F, -3F);
		Engine2.setTextureSize(128, 64);
		Engine2.mirror = true;
		setRotation(Engine2, 0F, 0F, 0F);
		Engine3 = new ModelRenderer(this, 80, 30);
		Engine3.addBox(0F, 0F, 0F, 1, 2, 2);
		Engine3.setRotationPoint(-3F, 18.4F, 0F);
		Engine3.setTextureSize(128, 64);
		Engine3.mirror = true;
		setRotation(Engine3, 0F, 0F, 0F);
		Frontwheel = new ModelRenderer(this, 80, 50);
		Frontwheel.addBox(-1.5F, -2.5F, -2.5F, 3, 5, 5);
		Frontwheel.setRotationPoint(0F, 21.5F, -9.5F);
		Frontwheel.setTextureSize(128, 64);
		Frontwheel.mirror = true;
		setRotation(Frontwheel, 0F, 0F, 0F);
		Backwheel = new ModelRenderer(this, 80, 50);
		Backwheel.addBox(-1.5F, -2.5F, -2.5F, 3, 5, 5);
		Backwheel.setRotationPoint(0F, 21.5F, 6.5F);
		Backwheel.setTextureSize(128, 64);
		Backwheel.mirror = true;
		setRotation(Backwheel, 0F, 0F, 0F);
		Chest = new ModelRenderer(this, 100, 30);
		Chest.addBox(-4F, -6F, 7F, 6, 6, 6);
		Chest.setRotationPoint(1F, 13F, -3F);
		Chest.setTextureSize(128, 64);
		Chest.mirror = true;
		setRotation(Chest, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (entity instanceof EntityMinibike) {
			EntityMinibike minibike = (EntityMinibike) entity;

			if (minibike.getHandles()) {
				Handle1.render(f5);
				Handle2.render(f5);
			}

			if (minibike.getWheels()) {
				Frontwheel.render(f5);
				Backwheel.render(f5);
			}

			if (minibike.getSeat()) {
				Seat.render(f5);
			}
			if (minibike.getBattery()) {
				Battery.render(f5);
			}
			if (minibike.getEngine()) {
				Engine1.render(f5);
				Engine2.render(f5);
				Engine3.render(f5);
			}
			if (minibike.getChest()) {
				Chest.render(f5);
			}

			Chassis1.render(f5);
			Chassis2.render(f5);
			Chassis12.render(f5);
			Chassis11.render(f5);
			Chassis3.render(f5);
			Chassis10.render(f5);
			Chassis4.render(f5);
			Chassis9.render(f5);
			Chassis5.render(f5);
			Chassis8.render(f5);
			Chassis6.render(f5);
			Chassis7.render(f5);
		}

	}

	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)

	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);

	}

}