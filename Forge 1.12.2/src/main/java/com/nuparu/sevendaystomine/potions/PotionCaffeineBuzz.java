package com.nuparu.sevendaystomine.potions;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionCaffeineBuzz extends PotionBase {

	public PotionCaffeineBuzz(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(5, 0);
		setRegistryName(SevenDaysToMine.MODID, "caffeinebuzz");
		setPotionName("effect." + getRegistryName().toString());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.2D, 1);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.33D, 1);
	}
}
