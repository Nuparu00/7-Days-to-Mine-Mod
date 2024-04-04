package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.item.ItemClothing;

@SideOnly(Side.CLIENT)
public class LayerClothing implements LayerRenderer<EntityLivingBase> {
	private final RenderPlayer playerRenderer;

	ModelPlayer armorModel;
	private float alpha = 1.0F;
	private float colorR = 1.0F;
	private float colorG = 1.0F;
	private float colorB = 1.0F;

	public LayerClothing(RenderPlayer defaultModel) {
		this.playerRenderer = defaultModel;
	}

	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
				scale, EntityEquipmentSlot.CHEST);
		this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
				scale, EntityEquipmentSlot.LEGS);
		this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
				scale, EntityEquipmentSlot.FEET);
		this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
				scale, EntityEquipmentSlot.HEAD);
	}

	private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale,
			EntityEquipmentSlot slotIn) {
		ItemStack stack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

		if (!stack.isEmpty() && stack.getItem() instanceof ItemClothing) {
			ItemClothing item = (ItemClothing) stack.getItem();
			armorModel = item.getModel(null, stack);
			ModelPlayer defaultModel = (ModelPlayer) this.playerRenderer.getMainModel();
			armorModel.setModelAttributes(defaultModel);
			armorModel.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
			this.playerRenderer.bindTexture(item.texture);
			int c = item.getColor(stack);
			GlStateManager.pushMatrix();
			if (c != -1) // Allow this for anything, not only cloth.
			{
				float r = (float) (c >> 16 & 255) / 255.0F;
				float g = (float) (c >> 8 & 255) / 255.0F;
				float b = (float) (c & 255) / 255.0F;
				GlStateManager.color(this.colorR * r, this.colorG * g, this.colorB * b, this.alpha);
				armorModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scale + item.scale);
			} else { // Non-colored
				GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
				armorModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scale + item.scale);
			}
			if (item.hasOverlay) {
				this.playerRenderer.bindTexture(item.overlay);
				GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
				armorModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scale + item.scale);

			}
			GlStateManager.color(1,1,1,1);
			GlStateManager.popMatrix();
		}

	}

	protected void setInvisible(ModelBiped model) {
		model.setVisible(false);
	}

	protected void chooseArmor(ModelBiped model, int slot) {
		this.setInvisible(model);

		switch (slot) {
		case 1:
			model.bipedRightLeg.showModel = true;
			model.bipedLeftLeg.showModel = true;
			break;
		case 2:
			model.bipedBody.showModel = true;
			model.bipedRightLeg.showModel = true;
			model.bipedLeftLeg.showModel = true;
			break;
		case 3:
			model.bipedBody.showModel = true;
			model.bipedRightArm.showModel = true;
			model.bipedLeftArm.showModel = true;
			break;
		case 4:
			model.bipedHead.showModel = true;
			model.bipedHeadwear.showModel = true;
		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}
