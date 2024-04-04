package nuparu.sevendaystomine.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PotionCaffeineBuzz extends PotionBase {

	public PotionCaffeineBuzz(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(5, 0);
		setRegistryName(SevenDaysToMine.MODID, "caffeinebuzz");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.05D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.33D, 2);
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if(entity.world.rand.nextInt(3) == 0 && entity instanceof EntityPlayer) {
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((EntityPlayer)entity);
			if(extendedPlayer == null)return;
			extendedPlayer.addStamina(1*amplifier);
		}
	}
}
