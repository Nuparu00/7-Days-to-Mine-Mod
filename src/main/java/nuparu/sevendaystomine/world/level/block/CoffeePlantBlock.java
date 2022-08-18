package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

public class CoffeePlantBlock extends CropBlock {
    public CoffeePlantBlock(Properties p_51021_) {
        super(p_51021_);
    }

    @Override

    protected ItemLike getBaseSeedId() {
        return Items.WHEAT_SEEDS;
    }
}
