package com.nuparu.sevendaystomine.potions;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionInfection extends PotionBase {

	public PotionInfection(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(2, 0);
		setRegistryName(SevenDaysToMine.MODID, "infection");
		setPotionName("effect." + getRegistryName().toString());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "96a8df9e-bf44-11e7-abc4-cec278b6b50a", -0.1D, 1);
	}

}
