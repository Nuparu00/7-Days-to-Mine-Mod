package nuparu.sevendaystomine.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.init.ModItems;
import org.jetbrains.annotations.NotNull;

public class CannedFoodItem extends ItemBase {
    public final boolean liquid;

    public CannedFoodItem(Properties properties) {
        this(properties,false);
    }

    public CannedFoodItem(Properties properties, boolean liquid) {
        super(properties.craftRemainder(ModItems.EMPTY_CAN.get()));
        this.liquid = liquid;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity living) {
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
    public int getUseDuration(@NotNull ItemStack stack) {
        return 32;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return liquid ? UseAnim.DRINK : UseAnim.EAT;
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
