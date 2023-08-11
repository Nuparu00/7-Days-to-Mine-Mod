package nuparu.sevendaystomine.world.item.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import nuparu.sevendaystomine.world.item.CreativeModeTabProviderBlockItem;

public class BlockItemBase extends BlockItem implements CreativeModeTabProviderBlockItem {
    public BlockItemBase(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
}
