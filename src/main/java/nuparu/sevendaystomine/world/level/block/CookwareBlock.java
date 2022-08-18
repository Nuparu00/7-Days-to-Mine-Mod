package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CookwareBlock extends HorizontalPickableBlock{
    public static final BooleanProperty CAMPFIRE = BooleanProperty.create("campfire");

    public final VoxelShape shape;
    public boolean placeableOnCampfires = true;
    public CookwareBlock(Properties p_49795_, VoxelShape shape, boolean placeableOnCampfires) {
        super(p_49795_);
        this.shape = shape;
        this.placeableOnCampfires = placeableOnCampfires;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(CAMPFIRE,false));
    }

    @Override
    public boolean canSurviveOn(LevelReader world, BlockPos pos, BlockState state) {
        return super.canSurviveOn(world, pos, state) || (placeableOnCampfires && state.getBlock() instanceof CampfireBlock);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(CAMPFIRE,context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() instanceof CampfireBlock);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING, WATERLOGGED, CAMPFIRE);
    }

    public boolean isBurning(Level level, BlockPos pos){
        BlockState state = level.getBlockState(pos.below());
        if (!(state.getBlock() instanceof CampfireBlock)) return false;
        return state.getValue(CampfireBlock.LIT);
    }
}
