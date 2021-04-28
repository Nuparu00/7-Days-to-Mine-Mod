package com.nuparu.sevendaystomine.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.events.HumanResponseEvent;
import com.nuparu.sevendaystomine.events.RenderHolsteredItemEvent;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemClub;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.item.ItemQualitySword;
import com.nuparu.sevendaystomine.item.ItemQualityTool;
import com.nuparu.sevendaystomine.item.ItemScrewdriver;
import com.nuparu.sevendaystomine.item.ItemStoneAxe;
import com.nuparu.sevendaystomine.item.ItemStoneShovel;
import com.nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import com.nuparu.sevendaystomine.util.item.ItemCache;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGuns implements LayerRenderer<EntityPlayer> {
	private final RenderPlayer playerRenderer;

	public LayerGuns(RenderPlayer defaultModel) {
		this.playerRenderer = defaultModel;
	}

	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (player == null)
			return;
		if (ModConfig.players.renderPlayerInventory == true) {
			if (PlayerInventorySyncHelper.itemsCache != null
					&& PlayerInventorySyncHelper.itemsCache.containsKey(player.getName())) {
				ItemCache cache = PlayerInventorySyncHelper.itemsCache.get(player.getName());
				if (cache == null || cache.isEmpty())
					return;
				Minecraft mc = Minecraft.getMinecraft();
				if (!cache.longItem.isEmpty()) {
					Item item = cache.longItem.getItem();
					GlStateManager.pushMatrix();
					if (player.isSneaking()) {
						GlStateManager.rotate(28.6479f, 1, 0, 0);
						GlStateManager.translate(0, 0.25, -0.1);
					}
					if (playerRenderer.getMainModel().swingProgress > 0.0F) {
						EnumHandSide enumhandside = this.getMainHand(player);
						float f1 = this.playerRenderer.getMainModel().swingProgress;
						float angle = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

						if (enumhandside == EnumHandSide.LEFT) {
							angle *= -1.0f;
						}
						angle = (float) Math.toDegrees(angle);
						GlStateManager.rotate(angle, 0, 1, 0);
					}
					// if (Utils.isClassFromPackageOrChild(item.getClass(), "net.minecraft")) {
					if (item instanceof ItemGun) {
						GlStateManager.translate(-0.215, 0, 0.12);
						GlStateManager.rotate(-90, 1, 0, 0);
						GlStateManager.rotate(90, 0, 0, -1);
						GlStateManager.scale(0.215, 0.215, 0.215);
					} else if (item == ModItems.SLEDGEHAMMER) {
						GlStateManager.translate(0.1, 0.6, 0.2);
						GlStateManager.scale(0.75, 0.75, 0.75);
						//GlStateManager.rotate(-90, 0, 1,0 );
						GlStateManager.rotate(180, 0, 0,1 );
						GlStateManager.rotate(-45, 0, 0,1 );
					} else if (item instanceof ItemClub) {
						GlStateManager.translate(-0.215, 0, 0.16);
						GlStateManager.scale(0.625, 0.625, 0.625);
					} else if (item instanceof ItemQualitySword) {
						GlStateManager.translate(-0.215, 1, 0.16);
						GlStateManager.scale(0.75, 0.75, 0.75);
						GlStateManager.rotate(-90, 0, 1,0 );
						GlStateManager.rotate(180, 0, 0,1 );
					}
					else if (item instanceof ItemStoneShovel) {
						GlStateManager.translate(-0.225, 0.25, 0.16);
						GlStateManager.scale(0.875, 0.875, 0.875);
					}
					else if (item == ModItems.IRON_AXE) {
						GlStateManager.rotate(180, 1, 0, 0);
						GlStateManager.rotate(90, 0, 1, 0);
						GlStateManager.rotate(45, -1, 0, 0);
						GlStateManager.translate(0.16, -0.1, 0.2);
					}
					else if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemHoe) {
						GlStateManager.translate(0.75, 0.45, -0.2825);
						GlStateManager.rotate(90, 0, 0, 1);
					} 
					else {
						GlStateManager.translate(0, 0.2, 0.16);
					}
					Event event = new RenderHolsteredItemEvent(player, cache.longItem,
							RenderHolsteredItemEvent.EnumType.BACK);
					if (!MinecraftForge.EVENT_BUS.post(event)) {
						mc.getRenderItem().renderItem(cache.longItem, player, ItemCameraTransforms.TransformType.HEAD,
								false);
					}
					GlStateManager.popMatrix();
				}
				
				
				if (!cache.shortItem_R.isEmpty()) {
					Item item = cache.shortItem_R.getItem();
					GlStateManager.pushMatrix();
					if (player.isSneaking()) {
						GlStateManager.translate(0, 0.05, 0.25);
					}
					if (item instanceof ItemGun) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(-0.3, 0.8, 0);
						GlStateManager.rotate(-90, 1, 0, 0);
						GlStateManager.scale(0.3625, 0.3625, 0.3625);
					} else if (item == ModItems.BONE_SHIV) {
						GlStateManager.translate(0, 0.5, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(-0.255, 0.925, -0.025);
						GlStateManager.rotate(90, 0, 1, 0);
						GlStateManager.scale(0.2625, 0.2625, 0.2625);
					}
					else if (item instanceof ItemQualitySword) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(-0.255, 1, -0.025);
						GlStateManager.rotate(90, 0, 1, 0);
						GlStateManager.rotate(270, 0, 0, 1);
						GlStateManager.scale(0.3625, 0.3625, 0.3625);
					}
					else if (item instanceof ItemQualityTool) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(-0.255, 1, -0.025);
						GlStateManager.rotate(180, 1, 0, 0);
						GlStateManager.rotate(180, 0, 1, 0);
						GlStateManager.scale(0.3625, 0.3625, 0.3625);
					}
					else if (item instanceof ItemScrewdriver) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(-0.255, 0.925, -0.025);
						GlStateManager.rotate(180, 0, 1, 0);
						GlStateManager.scale(0.2625, 0.2625, 0.2625);
					}
					Event event = new RenderHolsteredItemEvent(player, cache.shortItem_R,
							RenderHolsteredItemEvent.EnumType.RIGHT_HIP);
					if (!MinecraftForge.EVENT_BUS.post(event)) {
						mc.getRenderItem().renderItem(cache.shortItem_R, player,
								ItemCameraTransforms.TransformType.HEAD, false);
					}
					GlStateManager.popMatrix();
				}
				
				
				if (!cache.shortItem_L.isEmpty()) {
					Item item = cache.shortItem_L.getItem();
					GlStateManager.pushMatrix();
					if (player.isSneaking()) {
						GlStateManager.translate(0, 0.05, 0.25);
					}
					if (item instanceof ItemGun) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(-0.255, 0.925, -0.025);
						GlStateManager.rotate(-90, 1, 0, 0);
						GlStateManager.scale(0.3625, 0.3625, 0.3625);
					}else if (item == ModItems.BONE_SHIV) {
						GlStateManager.translate(0, 0.5, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(0.255, 0.925, -0.025);
						GlStateManager.rotate(90, 0, 1, 0);
						GlStateManager.scale(0.2625, 0.2625, 0.2625);
					}
					else if (item instanceof ItemQualitySword) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(0.255, 1, -0.025);
						GlStateManager.rotate(90, 0, 1, 0);
						GlStateManager.rotate(270, 0, 0, 1);
						GlStateManager.scale(0.3625, 0.3625, 0.3625);
					}
					else if (item instanceof ItemQualityTool) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(0.255, 1, -0.025);
						GlStateManager.rotate(180, 1, 0, 0);
						GlStateManager.rotate(180, 0, 1, 0);
						GlStateManager.scale(0.3625, 0.3625, 0.3625);
					}
					else if (item instanceof ItemScrewdriver) {
						GlStateManager.translate(0, 0.7, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleX),
								1, 0, 0);
						GlStateManager.rotate(
								(float) Math.toDegrees(this.playerRenderer.getMainModel().bipedRightLeg.rotateAngleY),
								0, 1, 0);
						GlStateManager.translate(0, -0.7, 0);
						GlStateManager.translate(0.255, 0.925, -0.025);
						GlStateManager.rotate(180, 0, 1, 0);
						GlStateManager.scale(0.2625, 0.2625, 0.2625);
					}
					Event event = new RenderHolsteredItemEvent(player, cache.shortItem_L,
							RenderHolsteredItemEvent.EnumType.LEFT_HIP);
					if (!MinecraftForge.EVENT_BUS.post(event)) {
						mc.getRenderItem().renderItem(cache.shortItem_L, player,
								ItemCameraTransforms.TransformType.HEAD, false);
					}
					GlStateManager.popMatrix();
				}
			}
		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}

	protected EnumHandSide getMainHand(Entity entityIn) {
		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
			return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
		} else {
			return EnumHandSide.RIGHT;
		}
	}
}
