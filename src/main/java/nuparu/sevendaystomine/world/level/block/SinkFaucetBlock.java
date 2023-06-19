package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SinkFaucetBlock extends WallAttachedBlockBase {

    public final VoxelShape NORTH = Block.box(4, 6, 11.2, 12, 11, 16);
    public final VoxelShape SOUTH = Block.box(4, 6, 0.0F, 12, 11, 4.8);
    public final VoxelShape WEST = Block.box(11.2, 6, 4, 16, 11, 12);
    public final VoxelShape EAST = Block.box(0.0F, 6, 4, 4.8, 11, 12);

    public SinkFaucetBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };

    }
}