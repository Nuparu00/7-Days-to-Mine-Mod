package nuparu.sevendaystomine.world.item;

import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;

import javax.annotation.Nullable;

public interface CreativeModeTabProvider {


    @Nullable
    default ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.BUILDING_BLOCKS.getId();
    }
}
