package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ISpikeBlock {
    void degradeBlock(BlockPos pos, Level level);

    int getMaxHealth(BlockPos pos, BlockState state, Level level);
}
