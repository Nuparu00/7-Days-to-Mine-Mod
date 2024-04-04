package nuparu.sevendaystomine.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PotionDysentery extends PotionBase {

	public PotionDysentery(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(4, 0);
		setRegistryName(SevenDaysToMine.MODID, "dysentery");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "9c44164e-bf44-11e7-abc4-cec278b6b50a",
				-0.1D, 2);
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {

		if (entityLivingBaseIn instanceof EntityPlayer) {

			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((EntityPlayer) entityLivingBaseIn);
			if (entityLivingBaseIn.world.rand.nextInt(10) == 0) {
				extendedPlayer.consumeStamina(5);
			}
		}

	}

}
