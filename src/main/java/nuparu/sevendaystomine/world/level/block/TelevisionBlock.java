package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class TelevisionBlock extends HorizontalBlockBase{

    private static final VoxelShape NORTH = Block.box(0, 0, 4, 16, 12, 11);
    private static final VoxelShape SOUTH = Block.box(0, 0, 5, 16, 12, 12);
    private static final VoxelShape WEST = Block.box(5, 0, 0, 12, 12, 16);
    private static final VoxelShape EAST = Block.box(4, 0, 0, 11, 12, 16);


    public TelevisionBlock(Properties p_54120_) {
        super(p_54120_, true);
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