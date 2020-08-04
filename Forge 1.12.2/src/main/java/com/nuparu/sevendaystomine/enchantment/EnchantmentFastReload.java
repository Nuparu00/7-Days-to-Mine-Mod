package com.nuparu.sevendaystomine.enchantment;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.ItemGun;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentFastReload extends Enchantment {

	protected EnchantmentFastReload() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setRegistryName(SevenDaysToMine.MODID, "fast_reload");
		setName("fast_reload");
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemGun;
	}
}