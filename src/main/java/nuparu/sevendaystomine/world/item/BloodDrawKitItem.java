package nuparu.sevendaystomine.world.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import nuparu.sevendaystomine.init.ModDamageSources;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.entity.EntityUtils;
import org.jetbrains.annotations.NotNull;

public class BloodDrawKitItem extends Item {

	public BloodDrawKitItem(Item.Properties properties) {
		super(properties);
	}


	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(itemstack);

	}

	@Override
	public void releaseUsing(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity entityLiving, int timeLeft) {

		if (entityLiving instanceof Player player && !world.isClientSide) {
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur >= this.getUseDuration(stack) * 0.15f) {
				ItemStack bloodBag = new ItemStack(ModItems.BLOOD_BAG.get());
				LivingEntity toHurt = entityLiving;

				HitResult entityRay = EntityUtils.raytraceEntities(entityLiving, 6, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
				if (entityRay != null) {
					if (entityRay instanceof EntityHitResult && ((EntityHitResult) entityRay).getEntity() instanceof LivingEntity clickedLiving) {
						if(clickedLiving instanceof Player){
							if(!player.isCreative() && !player.isSpectator()){
								toHurt = clickedLiving;
							}
						}
						else if (clickedLiving instanceof Mob){
							toHurt = clickedLiving;
						}
					}
				}

                bloodBag.getOrCreateTag().putString("donor", player.getUUID().toString());

                if (!player.getInventory().add(bloodBag)) {
					player.drop(bloodBag, false);
				}
				toHurt.hurt(ModDamageSources.bleeding, 4);
				if (player instanceof ServerPlayer) {
					stack.hurtAndBreak(1, player, (p_43076_) -> p_43076_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
				}
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public int getUseDuration(@NotNull ItemStack itemStack) {
		return 200;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
		return UseAnim.BOW;
	}
}
