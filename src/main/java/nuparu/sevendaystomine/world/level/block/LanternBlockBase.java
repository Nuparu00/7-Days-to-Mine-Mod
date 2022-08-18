package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LanternBlock;
import org.jetbrains.annotations.Nullable;

public class LanternBlockBase extends LanternBlock implements IBlockBase{
    public LanternBlockBase(Properties p_153465_) {
        super(p_153465_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}
