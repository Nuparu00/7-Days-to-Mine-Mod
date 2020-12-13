package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.util.ColorDetector;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLeatherArmor extends ItemArmorBase{

	public ItemLeatherArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String name) {
		super(materialIn, renderIndexIn, equipmentSlotIn, name);
        this.setMaxDamage(SevenDaysToMine.LEATHER_ARMOR.getDurability(equipmentSlotIn));
        ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, SevenDaysToMine.LEATHER_ARMOR.getDamageReductionAmount(equipmentSlotIn), "field_77879_b");
        ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, SevenDaysToMine.LEATHER_ARMOR.getToughness(), "field_189415_e");
    	}

	@Override
	public boolean hasColor(ItemStack stack) {

		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return nbttagcompound != null && nbttagcompound.hasKey("display", 10)
				? nbttagcompound.getCompoundTag("display").hasKey("color", 3)
				: false;
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	public int getColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
				return nbttagcompound1.getInteger("color");
			}
		}

		return 10511680;

	}

	public void removeColor(ItemStack stack) {

		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color")) {
				nbttagcompound1.removeTag("color");
			}
		}
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	public void setColor(ItemStack stack, int color) {

		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound == null) {
			nbttagcompound = new NBTTagCompound();
			stack.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display", 10)) {
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", color);
	}

}
