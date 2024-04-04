package nuparu.sevendaystomine.advancements;

import net.minecraft.advancements.ICriterionTrigger;

public class ModTriggers {
	public static final UpgradeBlockTrigger BLOCK_UPGRADE = new UpgradeBlockTrigger("upgrade_block");
	public static final BloodmoonSurvivalTrigger BLOODMOON_SURVIVAL = new BloodmoonSurvivalTrigger(
			"bloodmoon_survival");
	public static final CustomTrigger AIRDROP_INTERACT = new CustomTrigger("airdrop_interact");
	public static final CustomTrigger GUN_INTERACT = new CustomTrigger("gun_trigger");
	public static final CustomTrigger CURE = new CustomTrigger("cure");
	public static final CustomTrigger FLARE = new CustomTrigger("flare");
	public static final CustomTrigger MOSCO = new CustomTrigger("mosco");
	public static final CustomTrigger BLOOD_BOND = new CustomTrigger("blood_bond");
	public static final CustomTrigger ROAD_TRIP = new CustomTrigger("road_trip");
	public static final CustomTrigger KNOW_IT_ALL = new CustomTrigger("know_it_all");
	public static final CustomTrigger SAFE_UNLOCK = new CustomTrigger("safe_unlock");

	@SuppressWarnings("rawtypes")
	public static final ICriterionTrigger[] TRIGGER_ARRAY = new ICriterionTrigger[] { BLOCK_UPGRADE, BLOODMOON_SURVIVAL,
			AIRDROP_INTERACT, GUN_INTERACT, CURE, FLARE,MOSCO,BLOOD_BOND, ROAD_TRIP,KNOW_IT_ALL,SAFE_UNLOCK};
}
