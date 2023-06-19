package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WallAttachedBlockBase extends WaterloggableHorizontalBlockBase {

    public WallAttachedBlockBase(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        Direction direction = p_196260_1_.getValue(FACING);
        BlockPos blockpos = p_196260_3_.relative(direction.getOpposite());
        BlockState blockstate = p_196260_2_.getBlockState(blockpos);
        return this.canSurviveOn(p_196260_2_, blockpos, blockstate, direction);
    }

    private boolean canSurviveOn(BlockGetter p_235552_1_, BlockPos p_235552_2_, BlockState state, Direction direction) {
        return state.isFaceSturdy(p_235552_1_, p_235552_2_, direction.getOpposite());
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Block block,
                                @NotNull BlockPos pos2, boolean p_220069_6_) {
        if (!world.isClientSide) {
            if (!state.canSurvive(world, pos)) {
                world.destroyBlock(pos, true);
            }
        }
    }
}