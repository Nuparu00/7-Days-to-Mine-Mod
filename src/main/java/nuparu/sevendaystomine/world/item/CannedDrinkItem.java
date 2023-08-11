package nuparu.sevendaystomine.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.init.ModItems;
import org.jetbrains.annotations.NotNull;

public class CannedDrinkItem extends ItemBase {
    public CannedDrinkItem(Properties properties) {
        super(properties);
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_42984_, @NotNull Level p_42985_, @NotNull LivingEntity p_42986_) {
        Player player = p_42986_ instanceof Player ? (Player)p_42986_ : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, p_42984_);
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                p_42984_.shrink(1);
            }
        }

        if (player == null || !player.getAbilities().instabuild) {
            if (p_42984_.isEmpty()) {
                return new ItemStack(ModItems.EMPTY_CAN.get());
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(ModItems.EMPTY_CAN.get()));
            }
        }

        p_42986_.gameEvent(GameEvent.DRINK);
        return p_42984_;
    }

    public int getUseDuration(@NotNull ItemStack p_43001_) {
        return 32;
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_42997_) {
        return UseAnim.DRINK;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_42993_, @NotNull Player p_42994_, @NotNull InteractionHand p_42995_) {
        return ItemUtils.startUsingInstantly(p_42993_, p_42994_, p_42995_);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return false;
    }

    @Override
    public ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.FOOD.getId();
    }
}
