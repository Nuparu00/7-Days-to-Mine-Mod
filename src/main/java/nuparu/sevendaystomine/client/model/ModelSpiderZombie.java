package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSpiderZombie extends ModelBase {
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer rightarm;
	ModelRenderer leftarm;
	ModelRenderer rightlegup;
	ModelRenderer leftlegup;
	ModelRenderer rightlegdown;
	ModelRenderer leftlegdown;
	ModelRenderer headwear;

	public ModelSpiderZombie() {
		this(0);
	}
	
	public ModelSpiderZombie(float modelSize) {
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8, modelSize);
		head.setRotationPoint(0F, 9F, -5F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8, 12, 4, modelSize);
		body.setRotationPoint(0F, 9F, -5F);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 1.396263F, 0F, 0F);
		rightarm = new ModelRenderer(this, 40, 16);
		rightarm.addBox(-3F, -2F, -2F, 4, 15, 4, modelSize);
		rightarm.setRotationPoint(-5F, 11F, -4F);
		rightarm.setTextureSize(64, 64);
		rightarm.mirror = true;
		setRotation(rightarm, 0F, 0F, 0F);
		leftarm = new ModelRenderer(this, 40, 16);
		leftarm.addBox(0F, -2F, -2F, 4, 15, 4, modelSize);
		leftarm.setRotationPoint(4F, 11F, -4F);
		leftarm.setTextureSize(64, 64);
		setRotation(leftarm, 0F, 0F, 0F);
		rightlegup = new ModelRenderer(this, 0, 16);
		rightlegup.addBox(-2F, 0F, -2F, 4, 9, 4, modelSize);
		rightlegup.setRotationPoint(-2F, 12F, 6F);
		rightlegup.setTextureSize(64, 64);
		rightlegup.mirror = true;
		setRotation(rightlegup, -0.7853982F, 0F, 0F);
		leftlegup = new ModelRenderer(this, 0, 16);
		leftlegup.addBox(-2F, 0F, -2F, 4, 9, 4, modelSize);
		leftlegup.setRotationPoint(2F, 12F, 6F);
		leftlegup.setTextureSize(64, 64);
		leftlegup.mirror = true;
		setRotation(leftlegup, -0.7853982F, 0F, 0F);
		rightlegdown = new ModelRenderer(this, 0, 32);
		rightlegdown.addBox(-2F, 5F, 2F, 4, 4, 6);
		rightlegdown.setRotationPoint(-2F, 12F, 6F);
		rightlegdown.setTextureSize(64, 64);
		rightlegdown.mirror = true;
		setRotation(rightlegdown, 0F, 0F, 0F);
		leftlegdown = new ModelRenderer(this, 0, 32);
		leftlegdown.addBox(-2F, 5F, 2F, 4, 4, 6);
		leftlegdown.setRotationPoint(2F, 12F, 6F);
		leftlegdown.setTextureSize(64, 64);
		leftlegdown.mirror = true;
		setRotation(leftlegdown, 0F, 0F, 0F);
		headwear = new ModelRenderer(this, 32, 0);
		headwear.addBox(-4F, -8F, -4F, 8, 8, 8, modelSize + 0.5f);
		headwear.setRotationPoint(0F, 9F, -5F);
		headwear.setTextureSize(64, 64);
		headwear.mirror = true;
		setRotation(headwear, 0F, 0F, 0F);

	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		head.render(f5);
		body.render(f5);
		rightarm.render(f5);
		leftarm.render(f5);
		rightlegup.render(f5);
		leftlegup.render(f5);
		rightlegdown.render(f5);
		leftlegdown.render(f5);
		headwear.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)

	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.leftlegup.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1 - 0.75f;
		this.rightlegup.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 - 0.75f;
		this.rightarm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2.0F * f1 * 0.75F;
		this.leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		this.headwear.rotateAngleY = this.head.rotateAngleY;
		this.headwear.rotateAngleX = this.head.rotateAngleX;
		this.leftlegdown.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1 - 0.75f;
		this.rightlegdown.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 - 0.75f;
	}
}
