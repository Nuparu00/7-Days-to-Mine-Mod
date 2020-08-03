package com.nuparu.sevendaystomine.potions;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.DamageSources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.EnumDifficulty;

public class PotionAlcoholPoison extends PotionBase {

	public PotionAlcoholPoison(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(2, 1);
		setRegistryName(SevenDaysToMine.MODID, "alcoholpoison");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"0d9a52e1-adc0-4c8b-bdad-3fe38a5d2657", -0.1D, 1);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "1acf3189-a3be-48d6-a8cb-9a3181c5ad36",
				-0.1D, 1);

	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
		if (entityLivingBaseIn.world.getDifficulty() != EnumDifficulty.PEACEFUL)
			return;
		if (entityLivingBaseIn.world.rand.nextInt(10) == 0) {
			entityLivingBaseIn.attackEntityFrom(DamageSources.alcoholPoison, 1);

		}
	}

}
