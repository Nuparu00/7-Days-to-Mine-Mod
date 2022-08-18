package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.entity.item.MountableBlockEntity;

import java.util.stream.IntStream;

public class SofaBlock extends HorizontalBlockBase implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    private static final VoxelShape SOUTH = Block.box(0F, 0.0F, 0F, 1F*16, 0.375F*16, 0.9375F*16);
    private static final VoxelShape NORTH = Block.box(0F, 0.0F, 0.0625F*16, 1F*16, 0.375F*16, 1F*16);
    private static final VoxelShape WEST = Block.box(0.0625F*16, 0.0F, 0F, 1F*16, 0.375F*16, 1F*16);
    private static final VoxelShape EAST = Block.box(0F, 0.0F, 0F, 0.9375F*16, 0.375F*16, 1F*16);

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_AABB = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape OCTET_NNN = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
    protected static final VoxelShape OCTET_NNP = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape OCTET_NPN = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape OCTET_NPP = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape OCTET_PNN = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape OCTET_PNP = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape OCTET_PPN = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape OCTET_PPP = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape[] TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
    protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP,
            OCTET_PPP);
    private static final int[] SHAPE_BY_STATE = new int[] { 12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4,
            1, 2, 8 };

    private static VoxelShape[] makeShapes(VoxelShape p_199779_0_, VoxelShape p_199779_1_, VoxelShape p_199779_2_,
                                           VoxelShape p_199779_3_, VoxelShape p_199779_4_) {
        return IntStream.range(0, 16).mapToObj((p_199780_5_) -> makeStairShape(p_199780_5_, p_199779_0_, p_199779_1_, p_199779_2_, p_199779_3_, p_199779_4_)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int p_199781_0_, VoxelShape p_199781_1_, VoxelShape p_199781_2_,
                                             VoxelShape p_199781_3_, VoxelShape p_199781_4_, VoxelShape p_199781_5_) {
        VoxelShape voxelshape = p_199781_1_;
        if ((p_199781_0_ & 1) != 0) {
            voxelshape = Shapes.or(p_199781_1_, p_199781_2_);
        }

        if ((p_199781_0_ & 2) != 0) {
            voxelshape = Shapes.or(voxelshape, p_199781_3_);
        }

        if ((p_199781_0_ & 4) != 0) {
            voxelshape = Shapes.or(voxelshape, p_199781_4_);
        }

        if ((p_199781_0_ & 8) != 0) {
            voxelshape = Shapes.or(voxelshape, p_199781_5_);
        }

        return voxelshape;
    }
    public SofaBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return (BOTTOM_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(state)]];

    }
    private int getShapeIndex(BlockState state) {
        return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos,
                                 Player playerIn, InteractionHand hand, BlockHitResult result) {
        if (!playerIn.isCrouching()) {
            MountableBlockEntity.mountBlock(worldIn, pos, playerIn,0.15f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));

        return blockState.setValue(SHAPE, getStairsShape(blockState, context.getLevel(), context.getClickedPos()));
    }

    private static StairsShape getStairsShape(BlockState p_208064_0_, LevelAccessor p_208064_1_, BlockPos p_208064_2_) {
        Direction direction = p_208064_0_.getValue(FACING);
        BlockState blockstate = p_208064_1_.getBlockState(p_208064_2_.relative(direction));
        if (isSofa(blockstate)) {
            Direction direction1 = blockstate.getValue(FACING);
            if (direction1.getAxis() != p_208064_0_.getValue(FACING).getAxis()
                    && canTakeShape(p_208064_0_, p_208064_1_, p_208064_2_, direction1.getOpposite())) {
                if (direction1 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState blockstate1 = p_208064_1_.getBlockState(p_208064_2_.relative(direction.getOpposite()));
        if (isSofa(blockstate1)) {
            Direction direction2 = blockstate1.getValue(FACING);
            if (direction2.getAxis() != p_208064_0_.getValue(FACING).getAxis()
                    && canTakeShape(p_208064_0_, p_208064_1_, p_208064_2_, direction2)) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState p_185704_0_, LevelAccessor p_185704_1_, BlockPos p_185704_2_,
                                        Direction p_185704_3_) {
        BlockState blockstate = p_185704_1_.getBlockState(p_185704_2_.relative(p_185704_3_));
        return !isSofa(blockstate) || blockstate.getValue(FACING) != p_185704_0_.getValue(FACING);
    }

    public static boolean isSofa(BlockState p_185709_0_) {
        return p_185709_0_.getBlock() instanceof SofaBlock;
    }

    @Override
    public BlockState updateShape(BlockState p_53323_, Direction p_53324_, BlockState p_53325_, LevelAccessor p_53326_, BlockPos p_53327_, BlockPos p_53328_) {
        if (p_53323_.getValue(WATERLOGGED)) {
            p_53326_.scheduleTick(p_53327_, Fluids.WATER, Fluids.WATER.getTickDelay(p_53326_));
        }

        return p_53324_.getAxis().isHorizontal()
                ? p_53323_.setValue(SHAPE, getStairsShape(p_53323_, p_53326_, p_53327_))
                : super.updateShape(p_53323_, p_53324_, p_53325_, p_53326_, p_53327_, p_53328_);
    }


    @Override
    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        Direction direction = p_185471_1_.getValue(FACING);
        StairsShape stairsshape = p_185471_1_.getValue(SHAPE);
        switch (p_185471_2_) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    switch (stairsshape) {
                        case INNER_LEFT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    switch (stairsshape) {
                        case INNER_LEFT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT:
                            return p_185471_1_.rotate(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.mirror(p_185471_1_, p_185471_2_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING, SHAPE, WATERLOGGED);
    }
}
