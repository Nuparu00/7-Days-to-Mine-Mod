package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import nuparu.sevendaystomine.world.item.block.BlockItemBase;
import org.jetbrains.annotations.Nullable;

public class BlockBase extends Block implements IBlockBase {
    public BlockBase(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties();
        return new BlockItemBase(this, properties);
    }
}
