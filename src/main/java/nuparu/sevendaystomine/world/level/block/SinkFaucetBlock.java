package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SinkFaucetBlock extends WaterloggableHorizontalBlockBase {

    public VoxelShape NORTH = Block.box(4, 6, 11.2, 12, 11, 16);
    public VoxelShape SOUTH = Block.box(4, 6, 0.0F, 12, 11, 4.8);
    public VoxelShape WEST = Block.box(11.2, 6, 4, 16, 11, 12);
    public VoxelShape EAST = Block.box(0.0F, 6, 4, 4.8, 11, 12);

    public SinkFaucetBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
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