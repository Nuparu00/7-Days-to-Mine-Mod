package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RebarFrameWoodBlock extends BlockBase implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    VoxelShape occlusionShape;

    public RebarFrameWoodBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
        occlusionShape = Shapes.or(Block.box(0,0,0,1,1,16),
                Block.box(5,0,0,6,1,16),
                Block.box(10,0,0,11,1,16),
                Block.box(15,0,0,16,1,16),
                Block.box(0,15,0,1,16,16),
                Block.box(5,15,0,6,16,16),
                Block.box(10,15,0,11,16,16),
                Block.box(15,15,0,16,16,16),

                Block.box(0,0,0,16,1,1),
                Block.box(0,0,5,16,1,6),
                Block.box(0,0,10,16,1,11),
                Block.box(0,0,15,16,1,16),
                Block.box(0,15,0,16,16,1),
                Block.box(0,15,5,16,16,6),
                Block.box(0,15,10,16,16,11),
                Block.box(0,15,15,16,16,16),

                Block.box(0,0,0,16,16,1),
                Block.box(0,0,0,1,16,16),
                Block.box(0,0,15,0,16,16),
                Block.box(15,0,0,16,16,16)
        );
    }

    @Override
    @NotNull
    public VoxelShape getOcclusionShape(@NotNull BlockState p_53338_, @NotNull BlockGetter p_53339_, @NotNull BlockPos p_53340_) {
        return occlusionShape;
    }

    @Override
    @NotNull
    public VoxelShape getVisualShape(@NotNull BlockState p_53311_, @NotNull BlockGetter p_53312_, @NotNull BlockPos p_53313_, @NotNull CollisionContext p_53314_) {
        return occlusionShape;
    }


    @Override
    public @NotNull FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_53304_) {
        FluidState fluidstate = p_53304_.getLevel().getFluidState(p_53304_.getClickedPos());
        return super.getStateForPlacement(p_53304_).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }


    @Override
    public @NotNull BlockState updateShape(BlockState p_53323_, @NotNull Direction p_53324_, @NotNull BlockState p_53325_, @NotNull LevelAccessor p_53326_, @NotNull BlockPos p_53327_, @NotNull BlockPos p_53328_) {
        if (p_53323_.getValue(WATERLOGGED)) {
            p_53326_.scheduleTick(p_53327_, Fluids.WATER, Fluids.WATER.getTickDelay(p_53326_));
        }

        return super.updateShape(p_53323_, p_53324_, p_53325_, p_53326_, p_53327_, p_53328_);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(WATERLOGGED);
    }
}
