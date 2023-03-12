package nuparu.sevendaystomine.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModEffects;

public class AdvancedBandageItem extends BandageItem {

	public AdvancedBandageItem(Properties properties) {
		super(properties);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player player) {
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur >= 20) {
				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.getInventory().removeItem(stack);
					}
				}
				player.removeEffect(ModEffects.BLEEDING.get());
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		if(super.interactLivingEntity(stack, playerIn, target, hand) == InteractionResult.SUCCESS){
			target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
