package nuparu.sevendaystomine.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.init.ModItems;

public class CannedFoodItem extends Item {
    public boolean liquid;

    public CannedFoodItem(Properties properties) {
        this(properties,false);
    }

    public CannedFoodItem(Properties properties, boolean liquid) {
        super(properties.tab(ModCreativeModeTabs.TAB_FOOD).craftRemainder(ModItems.EMPTY_CAN.get()));
        this.liquid = liquid;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity living) {
        ItemStack original = stack.copy();
        ItemStack result = super.finishUsingItem(stack, worldIn, living);
        if (living instanceof Player player) {
            if ((original.getDamageValue() + 1 == original.getMaxDamage()) && this.hasCraftingRemainingItem(original)) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
                }

                if (player != null) {
                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }

                if (player == null || !player.getAbilities().instabuild) {
                    if (stack.isEmpty()) {
                        return new ItemStack(ModItems.EMPTY_CAN.get());
                    }

                    if (player != null) {
                        player.getInventory().add(new ItemStack(ModItems.EMPTY_CAN.get()));
                    }
                }

                living.gameEvent(GameEvent.DRINK);
                return stack;
            }
        }
        return result;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return liquid ? UseAnim.DRINK : UseAnim.EAT;
    }
}
