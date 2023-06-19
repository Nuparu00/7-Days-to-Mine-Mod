package nuparu.sevendaystomine.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.BowItem.getPowerForTime;
import static nuparu.sevendaystomine.util.ItemUtils.canHaveQuality;

@Mixin(BowItem.class)
public abstract class MixinBowItem {
    @Shadow public abstract int getUseDuration(ItemStack p_40680_);


    @Shadow public abstract AbstractArrow customArrow(AbstractArrow arrow);

    @Inject(method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V", at = @At("HEAD"), cancellable = true)
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft, CallbackInfo ci) {
        BowItem instance =(BowItem)(Object)this;
        if (canHaveQuality(instance) && (Object)stack instanceof IQualityStack qualityStack && qualityStack.getQuality() > 0) {
            if (entity instanceof Player player) {
                boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
                ItemStack itemstack = player.getProjectile(stack);

                int i = getUseDuration(stack) - timeLeft;
                i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
                if (i < 0) return;

                if (!itemstack.isEmpty() || flag) {
                    if (itemstack.isEmpty()) {
                        itemstack = new ItemStack(Items.ARROW);
                    }

                    float f = getPowerForTime(i);
                    if (!((double)f < 0.1D)) {
                        boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player));
                        if (!level.isClientSide) {
                            ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                            AbstractArrow abstractarrow = arrowitem.createArrow(level, itemstack, player);
                            abstractarrow = customArrow(abstractarrow);
                            abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F , 1.0F);
                            if (f == 1.0F) {
                                abstractarrow.setCritArrow(true);
                            }

                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * (1 + qualityStack.getRelativeQualityScaled()));

                            int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                            if (j > 0) {
                                abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                            }

                            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                            if (k > 0) {
                                abstractarrow.setKnockback(k);
                            }

                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                                abstractarrow.setSecondsOnFire(100);
                            }

                            stack.hurtAndBreak(1, player, (p_40665_) -> p_40665_.broadcastBreakEvent(player.getUsedItemHand()));
                            if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                                abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(abstractarrow);
                        }

                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        if (!flag1 && !player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                player.getInventory().removeItem(itemstack);
                            }
                        }

                        player.awardStat(Stats.ITEM_USED.get(instance));
                    }
                }
            }
            ci.cancel();
        }
    }
}
