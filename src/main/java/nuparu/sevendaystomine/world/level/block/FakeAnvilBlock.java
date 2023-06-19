package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FakeAnvilBlock extends FallingWaterloggableHorizontalBlockBase{

    private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape X_LEG1 = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
    private static final VoxelShape X_LEG2 = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
    private static final VoxelShape X_TOP = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
    private static final VoxelShape Z_LEG1 = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
    private static final VoxelShape Z_LEG2 = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
    private static final VoxelShape Z_TOP = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
    private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_LEG1, X_LEG2, X_TOP);
    private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_LEG1, Z_LEG2, Z_TOP);

    public FakeAnvilBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        return super.getStateForPlacement(p_48781_).setValue(FACING, p_48781_.getHorizontalDirection().getClockWise());
    }


    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Override
    protected void falling(FallingBlockEntity p_149829_1_) {
        p_149829_1_.setHurtsEntities(2.0F, 40);
    }

    @Override
    public void onLand(Level p_176502_1_, BlockPos p_176502_2_, BlockState p_176502_3_, BlockState p_176502_4_,
                       FallingBlockEntity p_176502_5_) {
        if (!p_176502_5_.isSilent()) {
            p_176502_1_.levelEvent(1031, p_176502_2_, 0);
        }

    }

    @Override
    public void onBrokenAfterFall(Level p_190974_1_, BlockPos p_190974_2_, FallingBlockEntity p_190974_3_) {
        if (!p_190974_3_.isSilent()) {
            p_190974_1_.levelEvent(1029, p_190974_2_, 0);
        }
    }

    @Override
    public boolean isPathfindable(BlockState p_48799_, BlockGetter p_48800_, BlockPos p_48801_, PathComputationType p_48802_) {
        return false;
    }

    @Override
    public int getDustColor(BlockState p_48827_, BlockGetter p_48828_, BlockPos p_48829_) {
        return p_48827_.getMapColor(p_48828_, p_48829_).col;
    }
}
