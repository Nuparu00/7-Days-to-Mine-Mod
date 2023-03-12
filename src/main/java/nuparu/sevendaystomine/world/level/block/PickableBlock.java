package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import nuparu.sevendaystomine.util.MathUtils;
import org.jetbrains.annotations.NotNull;

public class PickableBlock extends BlockBase implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public PickableBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }


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


    @Override
    public boolean canSurvive(@NotNull BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        BlockPos blockpos = p_196260_3_.below();
        BlockState blockstate = p_196260_2_.getBlockState(blockpos);
        return this.canSurviveOn(p_196260_2_, blockpos, blockstate);
    }

    public boolean canSurviveOn(LevelReader world, BlockPos pos, BlockState state) {
        return state.isFaceSturdy(world, pos, Direction.UP) || state.is(Blocks.HOPPER);
    }

    @Override
    public void neighborChanged(@NotNull BlockState p_220069_1_, Level p_220069_2_, @NotNull BlockPos p_220069_3_, @NotNull Block p_220069_4_,
                                @NotNull BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!p_220069_2_.isClientSide) {
            if (!p_220069_1_.canSurvive(p_220069_2_, p_220069_3_)) {
                p_220069_2_.destroyBlock(p_220069_3_, true);
            }

        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult rayTraceResult) {
        if (worldIn.isClientSide())
            return InteractionResult.SUCCESS;

        Block.dropResources(state, worldIn, pos);
        worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS,
                MathUtils.getFloatInRange(0.9f, 1.1f), MathUtils.getFloatInRange(0.9f, 1.1f));

        return InteractionResult.SUCCESS;
    }
}
