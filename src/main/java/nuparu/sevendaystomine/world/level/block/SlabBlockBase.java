package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SlabBlock;
import nuparu.sevendaystomine.world.item.block.BlockItemBase;
import org.jetbrains.annotations.Nullable;

public class SlabBlockBase extends SlabBlock implements IBlockBase {

    public SlabBlockBase(Properties p_56359_) {
        super(p_56359_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties();
        return new BlockItemBase(this, properties);
    }
}
