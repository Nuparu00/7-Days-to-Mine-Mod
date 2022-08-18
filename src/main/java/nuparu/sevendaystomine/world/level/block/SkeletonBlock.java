package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkeletonBlock extends WaterloggableHorizontalBlockBase{

    private static final VoxelShape SHAPE = Block.box(0F, 0.0F, 0F, 16F, 4F, 16);


    public SkeletonBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return SHAPE;
    }
}
