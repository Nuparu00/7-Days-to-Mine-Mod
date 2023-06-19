package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ComputerBlock extends HorizontalBlockBase{

    private static final VoxelShape NORTH = Block.box(5, 0, 1, 11, 16F, 14);
    private static final VoxelShape SOUTH = Block.box(5, 0, 2, 11, 16F, 15);
    private static final VoxelShape WEST = Block.box(1, 0, 5, 14, 16F, 11);
    private static final VoxelShape EAST = Block.box(2, 0, 5, 15, 16F, 11);


    public ComputerBlock(BlockBehaviour.Properties p_54120_) {
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
