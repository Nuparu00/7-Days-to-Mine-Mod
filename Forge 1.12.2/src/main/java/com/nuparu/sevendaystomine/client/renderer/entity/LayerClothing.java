package com.nuparu.sevendaystomine.client.renderer.entity;

import com.nuparu.sevendaystomine.item.ItemClothing;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerClothing implements LayerRenderer<EntityPlayer> {
	private final RenderPlayer playerRenderer;

	ModelPlayer armorModel;
	private float alpha = 1.0F;
	private float colorR = 1.0F;
	private float colorG = 1.0F;
	private float colorB = 1.0F;

	public LayerClothing(RenderPlayer defaultModel) {
		this.playerRenderer = defaultModel;
	}

	public void doRenderLayer(EntityPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		renderArmorPiece(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 0);
		renderArmorPiece(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 1);
		renderArmorPiece(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 2);
		renderArmorPiece(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 3);
	}

	public void renderArmorPiece(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, int slot) {
		EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		ItemStack stack = player.inventory.armorItemInSlot(slot);
		if (!stack.isEmpty() && stack.getItem() instanceof ItemClothing) {
			ItemClothing item = (ItemClothing) stack.getItem();
			armorModel = item.getModel(player,stack);
			ModelPlayer defaultModel = (ModelPlayer) this.playerRenderer.getMainModel();
			armorModel.setModelAttributes(defaultModel);
			armorModel.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);
			this.playerRenderer.bindTexture(item.texture);
			int c = item.getColor(stack);
			if (c != -1) // Allow this for anything, not only cloth.
			{
				float r = (float) (c >> 16 & 255) / 255.0F;
				float g = (float) (c >> 8 & 255) / 255.0F;
				float b = (float) (c & 255) / 255.0F;
				GlStateManager.color(this.colorR * r, this.colorG * g, this.colorB * b, this.alpha);
				armorModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scale + item.scale);
			} else { // Non-colored
				GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
				armorModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scale + item.scale);
			}
			if (item.hasOverlay) {
				this.playerRenderer.bindTexture(item.overlay);
				GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
				armorModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scale + item.scale);

			}
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
