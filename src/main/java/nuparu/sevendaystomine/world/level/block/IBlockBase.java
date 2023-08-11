package nuparu.sevendaystomine.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.world.item.CreativeModeTabProvider;

import javax.annotation.Nullable;

public interface IBlockBase extends CreativeModeTabProvider {

    @Nullable
    BlockItem createBlockItem();

    @Nullable
    default ResourceLocation creativeModeTab(){
        ResourceLocation res = ModCreativeModeTabs.BUILDING_BLOCKS.getId();
        return res;
    }
}
