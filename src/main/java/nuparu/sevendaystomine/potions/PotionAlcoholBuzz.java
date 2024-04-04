package nuparu.sevendaystomine.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionAlcoholBuzz extends PotionBase {

	public PotionAlcoholBuzz(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(6, 0);
		setRegistryName(SevenDaysToMine.MODID, "alcoholbuzz");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.15D, 1);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.25D, 1);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.15D, 1);
	}
}