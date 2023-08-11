package nuparu.sevendaystomine.world.item;

import net.minecraft.resources.ResourceLocation;
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
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.init.ModEffects;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BandageItem extends ItemBase {

	public BandageItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(@NotNull ItemStack itemStack) {
		return 82000;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
		return UseAnim.BOW;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(itemstack);

	}

	@Override
	public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving, int timeLeft) {
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
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}
	
	@Override
	public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, LivingEntity target, @NotNull InteractionHand hand)
    {
		if(target.getEffect(ModEffects.BLEEDING.get()) != null) {
			target.removeEffect(ModEffects.BLEEDING.get());
			stack.shrink(1);
			player.awardStat(Stats.ITEM_USED.get(this));
	        return InteractionResult.SUCCESS;
		}
        return InteractionResult.PASS;
    }

	@Override
	public ResourceLocation creativeModeTab(){
		return ModCreativeModeTabs.MEDICINE.getId();
	}

}
