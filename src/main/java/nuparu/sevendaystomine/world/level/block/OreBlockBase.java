package nuparu.sevendaystomine.world.level.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

public class OreBlockBase extends DropExperienceBlock implements IBlockBase {

    public OreBlockBase(BlockBehaviour.Properties p_55140_) {
        super(p_55140_);
    }

    public OreBlockBase(Properties p_153992_, UniformInt p_153993_) {
        super(p_153992_, p_153993_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}
