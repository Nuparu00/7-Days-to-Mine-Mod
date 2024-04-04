package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSleepingBag extends ModelBase {
	// fields
	ModelRenderer UpperA;
	ModelRenderer UpperB;
	ModelRenderer LowerA;
	ModelRenderer LowerB;

	public ModelSleepingBag() {
		textureWidth = 64;
		textureHeight = 64;

		UpperA = new ModelRenderer(this, 0, 0);
		UpperA.addBox(0F, 0F, 0F, 14, 1, 15);
		UpperA.setRotationPoint(-7F, 23F, -8F);
		UpperA.setTextureSize(64, 64);
		UpperA.mirror = true;
		setRotation(UpperA, 0F, 0F, 0F);
		UpperB = new ModelRenderer(this, 0, 32);
		UpperB.addBox(0F, 0F, 0F, 12, 1, 7);
		UpperB.setRotationPoint(-6F, 22F, -8F);
		UpperB.setTextureSize(64, 64);
		UpperB.mirror = true;
		setRotation(UpperB, 0F, 0F, 0F);
		LowerA = new ModelRenderer(this, 0, 16);
		LowerA.addBox(0F, 0F, 0F, 14, 1, 15);
		LowerA.setRotationPoint(-7F, 23F, -7F);
		LowerA.setTextureSize(64, 64);
		LowerA.mirror = true;
		setRotation(LowerA, 0F, 0F, 0F);
		LowerB = new ModelRenderer(this, 0, 40);
		LowerB.addBox(0F, 0F, 0F, 12, 1, 14);
		LowerB.setRotationPoint(-6F, 22F, -6F);
		LowerB.setTextureSize(64, 64);
		LowerB.mirror = true;
		setRotation(LowerB, 0F, 0F, 0F);
	}

	public int getModelVersion() {
		return 51;
	}

	public void render() {
		this.LowerA.render(0.0625F);
		this.LowerB.render(0.0625F);
		this.UpperA.render(0.0625F);
		this.UpperB.render(0.0625F);
	}

	public void preparePiece(boolean head) {
		this.LowerA.showModel = !head;
		this.LowerB.showModel = !head;
		this.UpperA.showModel = head;
		this.UpperB.showModel = head;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
