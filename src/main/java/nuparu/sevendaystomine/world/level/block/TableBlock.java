package nuparu.sevendaystomine.world.level.block;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class TableBlock extends BlockBase implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public VoxelShape TOP = Block.box(0,14,0,16,16,16);
    public static final VoxelShape LEG_A = Block.box(2,0,12,4,14,14);
    public static final VoxelShape LEG_B = Block.box(12,0,12,14,14,14);
    public static final  VoxelShape LEG_C = Block.box(2,0,2,4,14,4);
    public static final  VoxelShape LEG_D = Block.box(12,0,2,14,14,4);


    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Util.make(Maps.newEnumMap(Direction.class), (p_203421_0_) -> {
                p_203421_0_.put(Direction.NORTH, NORTH);
                p_203421_0_.put(Direction.EAST, EAST);
                p_203421_0_.put(Direction.SOUTH, SOUTH);
                p_203421_0_.put(Direction.WEST, WEST);
            });

    public TableBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        boolean north = state.getValue(NORTH);
        boolean south = state.getValue(SOUTH);
        boolean west = state.getValue(WEST);
        boolean east = state.getValue(EAST);

        VoxelShape result = TOP;

        if(!south){
            if(!west){
                result = Shapes.or(result,LEG_A);
            }
            if(!east){
                result = Shapes.or(result,LEG_B);
            }
        }

        if(!north){
            if(!west){
                result = Shapes.or(result,LEG_C);
            }
            if(!east){
                result = Shapes.or(result,LEG_D);
            }
        }
        return result;
    }

    public boolean connectsTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
        Block block = p_220111_1_.getBlock();
        return block instanceof TableBlock;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level iblockreader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        return super.getStateForPlacement(context)
                .setValue(
                        NORTH,
                        this.connectsTo(blockstate,
                                blockstate.isFaceSturdy(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH))
                .setValue(EAST,
                        this.connectsTo(blockstate1,
                                blockstate1.isFaceSturdy(iblockreader, blockpos2, Direction.WEST), Direction.WEST))
                .setValue(SOUTH,
                        this.connectsTo(blockstate2,
                                blockstate2.isFaceSturdy(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH))
                .setValue(WEST, this.connectsTo(blockstate3,
                        blockstate3.isFaceSturdy(iblockreader, blockpos4, Direction.EAST), Direction.EAST)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_,
                                  LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }

        return p_196271_2_.getAxis().getPlane() == Direction.Plane.HORIZONTAL
                ? p_196271_1_.setValue(PROPERTY_BY_DIRECTION.get(p_196271_2_),
                this.connectsTo(p_196271_3_,
                        p_196271_3_.isFaceSturdy(p_196271_4_, p_196271_6_, p_196271_2_.getOpposite()),
                        p_196271_2_.getOpposite()))
                : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        switch(p_185499_2_) {
            case CLOCKWISE_180:
                return p_185499_1_.setValue(NORTH, p_185499_1_.getValue(SOUTH)).setValue(EAST, p_185499_1_.getValue(WEST)).setValue(SOUTH, p_185499_1_.getValue(NORTH)).setValue(WEST, p_185499_1_.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return p_185499_1_.setValue(NORTH, p_185499_1_.getValue(EAST)).setValue(EAST, p_185499_1_.getValue(SOUTH)).setValue(SOUTH, p_185499_1_.getValue(WEST)).setValue(WEST, p_185499_1_.getValue(NORTH));
            case CLOCKWISE_90:
                return p_185499_1_.setValue(NORTH, p_185499_1_.getValue(WEST)).setValue(EAST, p_185499_1_.getValue(NORTH)).setValue(SOUTH, p_185499_1_.getValue(EAST)).setValue(WEST, p_185499_1_.getValue(SOUTH));
            default:
                return p_185499_1_;
        }
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        switch(p_185471_2_) {
            case LEFT_RIGHT:
                return p_185471_1_.setValue(NORTH, p_185471_1_.getValue(SOUTH)).setValue(SOUTH, p_185471_1_.getValue(NORTH));
            case FRONT_BACK:
                return p_185471_1_.setValue(EAST, p_185471_1_.getValue(WEST)).setValue(WEST, p_185471_1_.getValue(EAST));
            default:
                return super.mirror(p_185471_1_, p_185471_2_);
        }
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56388_) {
        p_56388_.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }

}
