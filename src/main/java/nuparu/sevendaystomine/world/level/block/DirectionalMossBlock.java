package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.world.level.block.entity.SpikesBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DirectionalMossBlock extends BlockBase implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final VoxelShape DOWN = Block.box(0, 15, 0, 16, 16, 16);
    public static final VoxelShape UP = Block.box(0, 0, 0, 16, 1, 16);
    public static final VoxelShape NORTH = Block.box(0, 0, 15, 16, 16, 16);
    public static final VoxelShape SOUTH = Block.box(0, 0, 0.0, 16, 16, 1);
    public static final VoxelShape WEST = Block.box(15, 0, 0, 16, 16, 16);
    public static final VoxelShape EAST = Block.box(0.0, 0, 0, 1, 16, 16);


    public DirectionalMossBlock(BlockBehaviour.Properties p_49795_) {
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        Direction facing = context.getClickedFace();
        BlockState blockstate = super.getStateForPlacement(context).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(FACING, facing);

        if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
            return blockstate;
        }
        return null;
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
    public boolean canSurvive(@NotNull BlockState p_196260_1_, @NotNull LevelReader p_196260_2_, @NotNull BlockPos p_196260_3_) {
        return canAttach(p_196260_2_, p_196260_3_, p_196260_1_.getValue(FACING).getOpposite());
    }

    public static boolean canAttach(LevelReader p_220185_0_, BlockPos p_220185_1_, Direction p_220185_2_) {
        BlockPos blockpos = p_220185_1_.relative(p_220185_2_);
        return p_220185_0_.getBlockState(blockpos).isFaceSturdy(p_220185_0_, blockpos, p_220185_2_.getOpposite());
    }

    @Override
    public void neighborChanged(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos, @NotNull Block p_220069_4_,
                                @NotNull BlockPos neighbor, boolean p_220069_6_) {

        if (!level.isClientSide && !canSurvive(blockState, level, pos)) {
                level.destroyBlock(pos, true);
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
