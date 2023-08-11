package nuparu.sevendaystomine.world.item;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import org.jetbrains.annotations.NotNull;

public class MurkyWaterBottleItem extends PotionItem implements CreativeModeTabProvider {
    public MurkyWaterBottleItem(Properties p_42979_) {
        super(p_42979_);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack p_43003_) {
        return this.getDescriptionId();
    }

    @Override
    public ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.FOOD.getId();
    }

}
