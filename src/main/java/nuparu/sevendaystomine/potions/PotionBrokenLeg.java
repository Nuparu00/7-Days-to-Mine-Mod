package nuparu.sevendaystomine.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionBrokenLeg extends PotionBase {

	public PotionBrokenLeg(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(1, 0);
		setRegistryName(SevenDaysToMine.MODID, "broken_leg");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"7a5c093e-7a5d-11ea-bc55-0242ac130003", -0.25D, 2);
	}
}
