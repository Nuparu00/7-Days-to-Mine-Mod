package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CatwalkBlock extends BlockBase implements SimpleWaterloggedBlock {

    public static final  EnumProperty<CatwalkType> TYPE = EnumProperty.create("type", CatwalkType.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape BOTTOM_AABB = Block.box(0, 0, 0, 16, 0.0625*16, 16);
    protected static final VoxelShape TOP_AABB = Block.box(0, 0.9375*16, 0, 16, 16, 16);

    public CatwalkBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, CatwalkType.BOTTOM).setValue(WATERLOGGED,
                Boolean.FALSE));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_56390_, @NotNull BlockGetter p_56391_, @NotNull BlockPos p_56392_, @NotNull CollisionContext p_56393_) {
        CatwalkType slabtype = p_56390_.getValue(TYPE);
        if (slabtype == CatwalkType.TOP) {
            return TOP_AABB;
        }
        return BOTTOM_AABB;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_56361_) {
        BlockPos blockpos = p_56361_.getClickedPos();

            FluidState fluidstate = p_56361_.getLevel().getFluidState(blockpos);
            BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, CatwalkType.BOTTOM).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            Direction direction = p_56361_.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(p_56361_.getClickLocation().y - (double)blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, CatwalkType.TOP);

    }

    @Override
    public boolean canBeReplaced(BlockState p_56373_, BlockPlaceContext p_56374_) {
        ItemStack itemstack = p_56374_.getItemInHand();
        CatwalkType slabtype = p_56373_.getValue(TYPE);
        if (itemstack.is(this.asItem())) {
            if (p_56374_.replacingClickedOnBlock()) {
                boolean flag = p_56374_.getClickLocation().y - (double)p_56374_.getClickedPos().getY() > 0.5D;
                Direction direction = p_56374_.getClickedFace();
                if (slabtype == CatwalkType.BOTTOM) {
                    return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
                } else {
                    return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState p_56397_) {
        return p_56397_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_56397_);
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor p_56368_, @NotNull BlockPos p_56369_, @NotNull BlockState p_56370_, @NotNull FluidState p_56371_) {
        return SimpleWaterloggedBlock.super.placeLiquid(p_56368_, p_56369_, p_56370_, p_56371_);
    }

    @Override
    public boolean canPlaceLiquid(@NotNull BlockGetter p_56363_, @NotNull BlockPos p_56364_, @NotNull BlockState p_56365_, @NotNull Fluid p_56366_) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(p_56363_, p_56364_, p_56365_, p_56366_);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_56381_, @NotNull Direction p_56382_, @NotNull BlockState p_56383_, @NotNull LevelAccessor p_56384_, @NotNull BlockPos p_56385_, @NotNull BlockPos p_56386_) {
        if (p_56381_.getValue(WATERLOGGED)) {
            p_56384_.scheduleTick(p_56385_, Fluids.WATER, Fluids.WATER.getTickDelay(p_56384_));
        }

        return super.updateShape(p_56381_, p_56382_, p_56383_, p_56384_, p_56385_, p_56386_);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState p_56376_, @NotNull BlockGetter p_56377_, @NotNull BlockPos p_56378_, @NotNull PathComputationType p_56379_) {
        if (p_56379_ == PathComputationType.WATER) {
            return p_56377_.getFluidState(p_56378_).is(FluidTags.WATER);
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56388_) {
        p_56388_.add(TYPE, WATERLOGGED);
    }

    public enum CatwalkType implements StringRepresentable {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        CatwalkType(String p_61775_) {
            this.name = p_61775_;
        }

        public String toString() {
            return this.name;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}