package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBackpack extends ModelBiped {
	ModelRenderer BackpackBase;
	ModelRenderer PocketLeft;
	ModelRenderer PocketRight;
	ModelRenderer PocketFront;
	ModelRenderer LeftA;
	ModelRenderer LeftB;
	ModelRenderer LeftC;
	ModelRenderer LeftD;
	ModelRenderer LeftE;
	ModelRenderer RightA;
	ModelRenderer RightB;
	ModelRenderer RightC;
	ModelRenderer RightD;
	ModelRenderer RightE;
	ModelBiped playerModel;

	public ModelBackpack(ModelBiped playerModel) {
		super(0f);

		BackpackBase = new ModelRenderer(this, 46, 20);
		BackpackBase.addBox(-3F, 0F, 2F, 6, 9, 3);
		BackpackBase.setRotationPoint(0F, 0F, 0F);
		BackpackBase.setTextureSize(64, 32);
		BackpackBase.mirror = true;
		setRotation(BackpackBase, 0F, 0F, 0F);
		PocketLeft = new ModelRenderer(this, 0, 18);
		PocketLeft.addBox(3F, 4F, 2F, 1, 4, 3);
		PocketLeft.setRotationPoint(0F, 0F, 0F);
		PocketLeft.setTextureSize(64, 32);
		PocketLeft.mirror = true;
		setRotation(PocketLeft, 0F, 0F, 0F);
		PocketRight = new ModelRenderer(this, 0, 25);
		PocketRight.addBox(-4F, 4F, 2F, 1, 4, 3);
		PocketRight.setRotationPoint(0F, 0F, 0F);
		PocketRight.setTextureSize(64, 32);
		PocketRight.mirror = true;
		setRotation(PocketRight, 0F, 0F, 0F);
		PocketFront = new ModelRenderer(this, 14, 19);
		PocketFront.addBox(-3F, 4F, 5F, 6, 5, 1);
		PocketFront.setRotationPoint(0F, 0F, 0F);
		PocketFront.setTextureSize(64, 32);
		PocketFront.mirror = true;
		setRotation(PocketFront, 0F, 0F, 0F);
		LeftA = new ModelRenderer(this, 0, 4);
		LeftA.addBox(2.5F, -0.5F, 2F, 1, 3, 1);
		LeftA.setRotationPoint(0F, 0F, 0F);
		LeftA.setTextureSize(64, 32);
		LeftA.mirror = true;
		setRotation(LeftA, 0F, 0F, 0F);
		LeftB = new ModelRenderer(this, 20, 0);
		LeftB.addBox(2.5F, -0.5F, -3.5F, 1, 1, 5);
		LeftB.setRotationPoint(0F, 0F, 1F);
		LeftB.setTextureSize(64, 32);
		LeftB.mirror = true;
		setRotation(LeftB, 0F, 0F, 0F);
		LeftC = new ModelRenderer(this, 20, 6);
		LeftC.addBox(2.5F, 0.5F, -2.5F, 1, 3, 1);
		LeftC.setRotationPoint(0F, 0F, 0F);
		LeftC.setTextureSize(64, 32);
		LeftC.mirror = true;
		setRotation(LeftC, 0F, 0F, 0F);
		LeftD = new ModelRenderer(this, 20, 10);
		LeftD.addBox(3F, 3.5F, -2.5F, 1, 2, 1);
		LeftD.setRotationPoint(0F, 0F, 0F);
		LeftD.setTextureSize(64, 32);
		LeftD.mirror = true;
		setRotation(LeftD, 0F, 0F, 0F);
		LeftE = new ModelRenderer(this, 20, 13);
		LeftE.addBox(3.5F, 4.5F, -2F, 1, 1, 4);
		LeftE.setRotationPoint(0F, 0F, 0F);
		LeftE.setTextureSize(64, 32);
		LeftE.mirror = true;
		setRotation(LeftE, 0F, 0F, 0F);
		RightA = new ModelRenderer(this, 0, 0);
		RightA.addBox(-3.5F, -0.5F, 2F, 1, 3, 1);
		RightA.setRotationPoint(0F, 0F, 0F);
		RightA.setTextureSize(64, 32);
		RightA.mirror = true;
		setRotation(RightA, 0F, 0F, 0F);
		RightB = new ModelRenderer(this, 40, 0);
		RightB.addBox(-3.5F, -0.5F, -2.5F, 1, 1, 5);
		RightB.setRotationPoint(0F, 0F, 0F);
		RightB.setTextureSize(64, 32);
		RightB.mirror = true;
		setRotation(RightB, 0F, 0F, 0F);
		RightC = new ModelRenderer(this, 40, 6);
		RightC.addBox(-3.5F, 0.5F, -2.5F, 1, 3, 1);
		RightC.setRotationPoint(0F, 0F, 0F);
		RightC.setTextureSize(64, 32);
		RightC.mirror = true;
		setRotation(RightC, 0F, 0F, 0F);
		RightD = new ModelRenderer(this, 40, 10);
		RightD.addBox(-4F, 3.5F, -2.5F, 1, 2, 1);
		RightD.setRotationPoint(0F, 0F, 0F);
		RightD.setTextureSize(64, 32);
		RightD.mirror = true;
		setRotation(RightD, 0F, 0F, 0F);
		RightE = new ModelRenderer(this, 40, 13);
		RightE.addBox(-4.5F, 4.5F, -2F, 1, 1, 4);
		RightE.setRotationPoint(0F, 0F, 0F);
		RightE.setTextureSize(64, 32);
		RightE.mirror = true;
		setRotation(RightE, 0F, 0F, 0F);

		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedBody.showModel = false;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;

		this.BackpackBase.addChild(PocketLeft);
		this.BackpackBase.addChild(PocketRight);
		this.BackpackBase.addChild(PocketFront);
		this.BackpackBase.addChild(LeftA);
		this.BackpackBase.addChild(LeftB);
		this.BackpackBase.addChild(LeftC);
		this.BackpackBase.addChild(LeftD);
		this.BackpackBase.addChild(LeftE);
		this.BackpackBase.addChild(RightA);
		this.BackpackBase.addChild(RightB);
		this.BackpackBase.addChild(RightC);
		this.BackpackBase.addChild(RightD);
		this.BackpackBase.addChild(RightE);

		this.playerModel = playerModel;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		copyModelAngles(this.bipedBody, this.BackpackBase);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.isChild) {

			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.BackpackBase.render(scale);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.BackpackBase.render(scale);
		}

		GlStateManager.popMatrix();

	}

}
