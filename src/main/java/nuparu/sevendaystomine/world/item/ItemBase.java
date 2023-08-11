package nuparu.sevendaystomine.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;

public class ItemBase extends Item implements CreativeModeTabProvider{
    public ItemBase(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.INGREDIENTS.getId();
    }
}
