package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FallingBlock;
import org.jetbrains.annotations.Nullable;

public class FallingBlockBase extends FallingBlock implements IBlockBase {

    public FallingBlockBase(Properties p_53205_) {
        super(p_53205_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}
