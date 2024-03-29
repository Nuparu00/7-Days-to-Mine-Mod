package nuparu.sevendaystomine.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.init.ModEffects;
import nuparu.sevendaystomine.util.PlayerUtils;
import org.jetbrains.annotations.NotNull;

public class AntibioticsItem extends ItemBase {
	public AntibioticsItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if(!(living instanceof Player player))return stack;
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		int time = iep.getInfectionTime();
		int stage = PlayerUtils.getInfectionStage(time);
		if (player instanceof ServerPlayer && (stage >= PlayerUtils.getNumberOfstages()-1)) {
			//ModTriggers.CURE.trigger((ServerPlayerEntity) player, o -> true);
		}
		iep.setInfectionTime(-1);
		player.removeEffect(ModEffects.INFECTION.get());
		player.removeEffect(ModEffects.FUNGAL_INFECTION.get());
		player.awardStat(Stats.ITEM_USED.get(this));
		return stack;
	}

	@Override
	public ResourceLocation creativeModeTab(){
		return ModCreativeModeTabs.MEDICINE.getId();
	}
}
