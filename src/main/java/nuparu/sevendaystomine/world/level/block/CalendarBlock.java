package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.level.block.entity.CalendarBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalendarBlock extends HorizontalBlockBase implements EntityBlock {
    private static final VoxelShape NORTH = Block.box(5, 7, 15, 11, 16, 16);
    private static final VoxelShape SOUTH = Block.box(5, 7, 0.0F, 11, 16, 1);
    private static final VoxelShape WEST = Block.box(15, 7, 5, 16, 16, 11);
    private static final VoxelShape EAST = Block.box(0.0F, 7, 5, 1, 16, 11);

    public CalendarBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_53304_) {
        return super.getStateForPlacement(p_53304_).setValue(FACING, p_53304_.getHorizontalDirection().getOpposite());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CalendarBlockEntity(pos,state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH, UP, DOWN -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING);
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
