package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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

public class BoardsBlock extends BlockBase implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty CROSS = BooleanProperty.create("cross");

    public final VoxelShape NORTH = Block.box(0, 0, 14, 16, 16, 16);
    public final VoxelShape SOUTH = Block.box(0, 0, 0, 16, 16, 2);
    public final VoxelShape WEST = Block.box(14, 0, 0F, 16, 16, 16);
    public final VoxelShape EAST = Block.box(0, 0, 0, 2, 16, 16);
    public final VoxelShape DOWN = Block.box(0, 14, 0, 16, 16, 16);
    public final VoxelShape UP = Block.box(0, 0, 0, 16, 2, 16);

    public final VoxelShape SUPPORT_SHAPE = Block.box(1, 1, 1, 14, 14, 14);


    public BoardsBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(CROSS, Boolean.FALSE));
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return ModCreativeModeTabs.TAB_MATERIALS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            case UP -> UP;
            case DOWN -> DOWN;
        };

    }

    @Deprecated
    public @NotNull VoxelShape getBlockSupportShape(@NotNull BlockState p_60581_, @NotNull BlockGetter p_60582_, @NotNull BlockPos p_60583_) {
        return SUPPORT_SHAPE;
    }

    public @NotNull FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_53304_) {
        Direction direction = p_53304_.getClickedFace();
        FluidState fluidstate = p_53304_.getLevel().getFluidState(p_53304_.getClickedPos());
        return super.getStateForPlacement(p_53304_).setValue(FACING,direction).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(CROSS,false);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_53323_, @NotNull Direction p_53324_, @NotNull BlockState p_53325_, @NotNull LevelAccessor p_53326_, @NotNull BlockPos p_53327_, @NotNull BlockPos p_53328_) {
        if (p_53323_.getValue(WATERLOGGED)) {
            p_53326_.scheduleTick(p_53327_, Fluids.WATER, Fluids.WATER.getTickDelay(p_53326_));
        }

        return super.updateShape(p_53323_, p_53324_, p_53325_, p_53326_, p_53327_, p_53328_);
    }

    public @NotNull BlockState rotate(BlockState p_154354_, Rotation p_154355_) {
        return p_154354_.setValue(FACING, p_154355_.rotate(p_154354_.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState p_154351_, Mirror p_154352_) {
        return p_154351_.setValue(FACING, p_154352_.mirror(p_154351_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING, WATERLOGGED, CROSS);
    }

}