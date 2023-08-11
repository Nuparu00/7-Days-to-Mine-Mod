package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LadderBlock;
import nuparu.sevendaystomine.world.item.block.BlockItemBase;
import org.jetbrains.annotations.Nullable;

public class MetalLadderBlock extends LadderBlock implements IBlockBase {
    public MetalLadderBlock(Properties p_54345_) {
        super(p_54345_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties();
        return new BlockItemBase(this, properties);
    }
}
