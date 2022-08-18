package nuparu.sevendaystomine.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModEffects;

public class BloodBagItem extends Item {
	
	public BloodBagItem(Properties properties) {
		super(properties);
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
			if (dur >= this.getUseDuration(stack) * 0.15f) {
				if (!player.isCreative()) {
					
					CompoundTag nbt = stack.getOrCreateTag();
					if(nbt.contains("donor", Tag.TAG_STRING)) {
						String uuid = nbt.getString("donor");
						if(!worldIn.isClientSide() && !uuid.equals(player.getUUID().toString())) {
							//ModTriggers.BLOOD_BOND.trigger((ServerPlayerEntity) player, o -> true);
						}
					}
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.getInventory().removeItem(stack);
					}
				}
				player.heal(4);
				if(player.hasEffect(ModEffects.ALCOHOL_POISON.get())){
					MobEffectInstance poisonEffect = player.getEffect(ModEffects.ALCOHOL_POISON.get());
					MobEffectInstance updatedEffect = new MobEffectInstance(ModEffects.ALCOHOL_POISON.get(), poisonEffect.getDuration()/3, poisonEffect.getAmplifier(), poisonEffect.isAmbient(), poisonEffect.isVisible());
					player.removeEffect(ModEffects.ALCOHOL_POISON.get());
					player.addEffect(updatedEffect);
				}
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack itemStack) {
		return 60;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.BOW;
	}

}
