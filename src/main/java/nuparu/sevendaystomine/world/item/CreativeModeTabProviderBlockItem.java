package nuparu.sevendaystomine.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

public interface CreativeModeTabProviderBlockItem extends CreativeModeTabProvider{

    @Override
    default ResourceLocation creativeModeTab(){
        return this instanceof BlockItem blockItem && blockItem.getBlock() instanceof CreativeModeTabProvider creativeModeTabProvider ? creativeModeTabProvider.creativeModeTab() : null;
    }
}
