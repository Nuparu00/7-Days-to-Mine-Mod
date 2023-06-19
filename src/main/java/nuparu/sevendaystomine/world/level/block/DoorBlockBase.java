package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DoorBlock;
import org.jetbrains.annotations.Nullable;

public class DoorBlockBase extends DoorBlock implements IBlockBase {
    public DoorBlockBase(Properties p_52737_) {
        super(p_52737_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}
