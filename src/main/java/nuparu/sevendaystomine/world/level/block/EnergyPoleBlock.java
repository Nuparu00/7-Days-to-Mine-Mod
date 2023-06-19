package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import org.jetbrains.annotations.NotNull;

public class EnergyPoleBlock extends BlockBase implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape NORTH = Block.box(4, 4, 8, 12, 12, 16);
    private static final VoxelShape SOUTH = Block.box(4, 4, 0, 12, 12, 8);
    private static final VoxelShape WEST = Block.box(8, 4, 4, 16, 12, 12);
    private static final VoxelShape EAST = Block.box(0, 4, 4, 8, 12, 12);
    private static final VoxelShape UP = Block.box(6, 0.0, 6, 10, 16, 10);
    private static final VoxelShape DOWN = Block.box(4, 8, 4, 12, 16, 12);


    public EnergyPoleBlock(BlockBehaviour.Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)){
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    public @NotNull FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_53304_) {
        FluidState fluidstate = p_53304_.getLevel().getFluidState(p_53304_.getClickedPos());
        return super.getStateForPlacement(p_53304_).setValue(FACING, p_53304_.getClickedFace()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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
        p_53334_.add(FACING, WATERLOGGED);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return ModCreativeModeTabs.TAB_ELECTRICITY;
    }

    @Override
    public void neighborChanged(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos, @NotNull Block p_220069_4_,
                                @NotNull BlockPos neighbor, boolean p_220069_6_) {
        if (!level.isClientSide) {
            Direction facing = blockState.getValue(FACING);
            BlockPos offsetPos = pos.relative(facing.getOpposite());
            BlockState state = level.getBlockState(offsetPos);
            if (!state.isFaceSturdy(level, offsetPos, facing)) {
                level.destroyBlock(pos, true);
            }
        }
    }


    @Override
    public BlockState rotate(BlockState p_54125_, Rotation p_54126_) {
        return p_54125_.setValue(FACING, p_54126_.rotate(p_54125_.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_54122_, Mirror p_54123_) {
        return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
    }
}
