package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import nuparu.sevendaystomine.world.level.block.IBlockBase;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FlowerPotBlockBase extends FlowerPotBlock implements IBlockBase {


    public FlowerPotBlockBase(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> p_53528_, Properties properties) {
        super(emptyPot, p_53528_, properties);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        return null;
    }
}
