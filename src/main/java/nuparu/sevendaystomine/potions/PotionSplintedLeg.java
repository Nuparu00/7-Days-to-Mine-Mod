package nuparu.sevendaystomine.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionSplintedLeg extends PotionBase {

	public PotionSplintedLeg(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(4, 1);
		setRegistryName(SevenDaysToMine.MODID, "splinted_leg");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"7a5c093e-7a5d-11ea-bc55-0242ac130003", -0.1D, 2);
	}
}
