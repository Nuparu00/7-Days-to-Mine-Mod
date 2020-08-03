package com.nuparu.sevendaystomine.enchantment;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.ItemGun;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentMarksman extends Enchantment {

	protected EnchantmentMarksman() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setRegistryName(SevenDaysToMine.MODID, "marksman");
		setName("marksman");
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemGun;
	}
}
