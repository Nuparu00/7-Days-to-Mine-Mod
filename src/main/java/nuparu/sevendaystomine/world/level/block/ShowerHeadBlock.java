package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ShowerHeadBlock extends WaterloggableHorizontalBlockBase {

    public VoxelShape NORTH = Block.box(5, 8, 6, 13, 14, 16);
    public VoxelShape SOUTH = Block.box(5, 8, 0.0F, 13, 14, 10);
    public VoxelShape WEST = Block.box(6, 8, 5, 16, 14, 13);
    public VoxelShape EAST = Block.box(0.0F, 8, 5, 10, 14, 13);

    public ShowerHeadBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        switch (state.getValue(FACING)) {
            default:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }

    }
}