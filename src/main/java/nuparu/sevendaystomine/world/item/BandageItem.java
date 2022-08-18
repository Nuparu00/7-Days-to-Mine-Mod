package nuparu.sevendaystomine.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModEffects;

public class BandageItem extends Item {

	public BandageItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(itemstack);

	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player player = (Player) entityLiving;
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur >= 20) {
				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.getInventory().removeItem(stack);
					}
				}
				player.removeEffect(ModEffects.BLEEDING.get());
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand)
    {
		if(target.getEffect(ModEffects.BLEEDING.get()) != null) {
			target.removeEffect(ModEffects.BLEEDING.get());
			stack.shrink(1);
			player.awardStat(Stats.ITEM_USED.get(this));
	        return InteractionResult.SUCCESS;
		}
        return InteractionResult.PASS;
    }

}
