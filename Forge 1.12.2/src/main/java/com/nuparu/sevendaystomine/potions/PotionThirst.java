package com.nuparu.sevendaystomine.potions;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.util.DamageSources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;

public class PotionThirst extends PotionBase {

	public PotionThirst(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(3, 0);
		setRegistryName(SevenDaysToMine.MODID, "thirst");
		setPotionName("effect." + getRegistryName().getResourcePath());
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
		if (entityLivingBaseIn instanceof EntityPlayer) {

			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((EntityPlayer) entityLivingBaseIn);
			if (entityLivingBaseIn.world.rand.nextInt(10) == 0) {
				extendedPlayer.consumeStamina(2);
			}
		}
		if (entityLivingBaseIn.world.getDifficulty() != EnumDifficulty.PEACEFUL)
			return;
		if (entityLivingBaseIn.world.rand.nextInt(15) == 0) {
			entityLivingBaseIn.attackEntityFrom(DamageSources.thirst, 2);
		}

	}

}
