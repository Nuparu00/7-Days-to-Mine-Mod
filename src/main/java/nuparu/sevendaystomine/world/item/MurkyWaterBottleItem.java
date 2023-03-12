package nuparu.sevendaystomine.world.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.jetbrains.annotations.NotNull;

public class MurkyWaterBottleItem extends PotionItem {
    public MurkyWaterBottleItem(Properties p_42979_) {
        super(p_42979_);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack p_43003_) {
        return this.getDescriptionId();
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab p_42981_, @NotNull NonNullList<ItemStack> p_42982_) {
        if (this.allowedIn(p_42981_)) {
            p_42982_.add(new ItemStack(this));
        }
    }

}
