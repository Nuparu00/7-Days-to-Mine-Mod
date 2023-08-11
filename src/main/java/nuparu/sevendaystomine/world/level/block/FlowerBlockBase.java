package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FlowerBlock;
import nuparu.sevendaystomine.world.item.block.BlockItemBase;
import org.jetbrains.annotations.Nullable;

public class FlowerBlockBase extends FlowerBlock implements IBlockBase {

    public FlowerBlockBase(MobEffect p_53512_, int p_53513_, Properties p_53514_) {
        super(p_53512_, p_53513_, p_53514_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties();
        return new BlockItemBase(this, properties);
    }
}
