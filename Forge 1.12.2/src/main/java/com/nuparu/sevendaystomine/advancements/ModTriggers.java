package com.nuparu.sevendaystomine.advancements;

import net.minecraft.advancements.ICriterionTrigger;

public class ModTriggers {
	public static final UpgradeBlockTrigger BLOCK_UPGRADE = new UpgradeBlockTrigger("upgrade_block");
	public static final BloodmoonSurvivalTrigger BLOODMOON_SURVIVAL = new BloodmoonSurvivalTrigger("bloodmoon_survival");
	public static final CustomTrigger AIRDROP_INTERACT = new CustomTrigger("airdrop_interact");
	public static final CustomTrigger GUN_INTERACT = new CustomTrigger("gun_trigger");
	public static final CustomTrigger CURE = new CustomTrigger("cure");
	
	@SuppressWarnings("rawtypes")
	public static final ICriterionTrigger[] TRIGGER_ARRAY = new ICriterionTrigger[] { BLOCK_UPGRADE,BLOODMOON_SURVIVAL,AIRDROP_INTERACT,GUN_INTERACT,CURE };
}
