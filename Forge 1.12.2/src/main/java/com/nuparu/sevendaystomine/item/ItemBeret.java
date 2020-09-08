package com.nuparu.sevendaystomine.item;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.model.ModelBeret;
import com.nuparu.sevendaystomine.client.model.ModelChristmasHat;
import com.nuparu.sevendaystomine.client.model.ModelNightVisionDevice;
import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBeret extends ItemArmor {

	public ItemBeret(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (!stack.isEmpty() && stack.getItem() instanceof ItemBeret) {
			return SevenDaysToMine.MODID + ":textures/models/armor/beret_green.png";
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
			ModelBiped original) {
		ModelBeret model = new ModelBeret();
		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBeret) {
			model.isChild = original.isChild;
			model.isSneak = original.isSneak;
			model.isRiding = original.isRiding;
			model.rightArmPose = original.rightArmPose;
			model.leftArmPose = original.leftArmPose;
		}

		return model;
	}

}
