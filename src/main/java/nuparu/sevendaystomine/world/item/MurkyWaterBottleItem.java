package nuparu.sevendaystomine.world.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class MurkyWaterBottleItem extends PotionItem {
    public MurkyWaterBottleItem(Properties p_42979_) {
        super(p_42979_);
    }

    @Override
    public String getDescriptionId(ItemStack p_43003_) {
        return this.getDescriptionId();
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_42981_, NonNullList<ItemStack> p_42982_) {
        if (this.allowedIn(p_42981_)) {
            p_42982_.add(new ItemStack(this));
        }
    }

}
