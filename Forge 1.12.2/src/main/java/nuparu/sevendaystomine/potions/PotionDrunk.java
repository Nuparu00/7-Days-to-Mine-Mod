package nuparu.sevendaystomine.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionDrunk extends PotionBase {

	public PotionDrunk(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(6, 0);
		setRegistryName(SevenDaysToMine.MODID, "drunk");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
				"9c442288-bf44-11e7-abc4-cec278b6b50a", -0.1D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "9c442f44-bf44-11e7-abc4-cec278b6b50a",
				-0.1D, 2);
	}
}
