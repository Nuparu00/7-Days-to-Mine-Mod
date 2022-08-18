package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;

import javax.annotation.Nullable;

public interface IBlockBase {

    @Nullable
    BlockItem createBlockItem();

    default CreativeModeTab getItemGroup() {
        return ModCreativeModeTabs.TAB_BLOCKS;
    }
}
