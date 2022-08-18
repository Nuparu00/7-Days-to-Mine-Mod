package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BrokenRedstoneLampBlock extends BlockBase{
    public BrokenRedstoneLampBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherBlock, boolean p_55671_) {
        if (!level.isClientSide) {
            if (level.hasNeighborSignal(pos)) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 0.1f, true,
                        Explosion.BlockInteraction.DESTROY);
            }

        }
    }
}
