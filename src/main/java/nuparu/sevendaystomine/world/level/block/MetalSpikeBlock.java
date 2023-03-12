package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.level.block.entity.SpikesBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MetalSpikeBlock extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock, ISpikeBlock, IBlockBase{

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");

    public static final VoxelShape AABB_DOWN = Block.box(0, 15, 0, 16, 16, 16);
    public static final VoxelShape AABB_UP = Block.box(0, 0, 0, 16, 1, 16);
    public static final VoxelShape AABB_NORTH = Block.box(0, 0, 15, 16, 16, 16);
    public static final VoxelShape AABB_SOUTH = Block.box(0, 0, 0.0, 16, 16, 1);
    public static final VoxelShape AABB_WEST = Block.box(15, 0, 0, 16, 16, 16);
    public static final VoxelShape AABB_EAST = Block.box(0.0, 0, 0, 1, 16, 16);


    protected int maxHealth;
    protected int damage;

    public MetalSpikeBlock(BlockBehaviour.Properties p_49795_, int maxHealth, int damage) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, Boolean.FALSE).setValue(EXTENDED, Boolean.FALSE));
        this.maxHealth = maxHealth;
        this.damage = damage;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos,
                                        @NotNull CollisionContext context) {
        AttachFace face = state.getValue(FACE);
        Direction facing = state.getValue(FACING);

        switch (face) {
            case FLOOR:
                return AABB_UP;
            case CEILING:
                return AABB_DOWN;
            case WALL: {
                switch (facing) {
                    case WEST:
                        return AABB_WEST;
                    case NORTH:
                        return AABB_NORTH;
                    case SOUTH:
                        return AABB_SOUTH;
                    case EAST:
                        return AABB_EAST;
                }
            }
        }
        return super.getShape(state, reader, pos, context);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Entity entityIn) {
        if (!(entityIn instanceof LivingEntity)) {
            return;
        }
        if(!state.getValue(EXTENDED)) return;
        entityIn.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
        if (entityIn instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
        }
        entityIn.hurt(DamageSource.GENERIC, damage);
        if (worldIn.getBlockEntity(pos) instanceof SpikesBlockEntity blockEntity) {
            blockEntity.damage(1);
        }
    }

    public boolean canSurvive(@NotNull BlockState p_196260_1_, @NotNull LevelReader p_196260_2_, @NotNull BlockPos p_196260_3_) {
        return canAttach(p_196260_2_, p_196260_3_, getConnectedDirection(p_196260_1_).getOpposite());
    }

    public static boolean canAttach(LevelReader p_220185_0_, BlockPos p_220185_1_, Direction p_220185_2_) {
        BlockPos blockpos = p_220185_1_.relative(p_220185_2_);
        return p_220185_0_.getBlockState(blockpos).isFaceSturdy(p_220185_0_, blockpos, p_220185_2_.getOpposite());
    }

    private void checkIfExtend(Level world, BlockPos pos, BlockState state) {
        boolean powered = world.hasNeighborSignal(pos);
        boolean extended = state.getValue(EXTENDED);
        if(powered != extended){
            BlockEntity blockEntity =  world.getBlockEntity(pos);
            world.setBlockAndUpdate(pos,state.setValue(EXTENDED,powered));
            world.setBlockEntity(blockEntity);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.5f, 0.5f);

        }

    }

    @Override
    public void setPlacedBy(Level p_180633_1_, @NotNull BlockPos p_180633_2_, @NotNull BlockState p_180633_3_, LivingEntity p_180633_4_, @NotNull ItemStack p_180633_5_) {
        if (!p_180633_1_.isClientSide) {
            this.checkIfExtend(p_180633_1_, p_180633_2_, p_180633_3_);
        }

    }

    @Override
    public void neighborChanged(@NotNull BlockState p_220069_1_, Level p_220069_2_, @NotNull BlockPos p_220069_3_, @NotNull Block p_220069_4_, @NotNull BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!p_220069_2_.isClientSide) {
            this.checkIfExtend(p_220069_2_, p_220069_3_, p_220069_1_);
        }

    }

    @Override
    public void onPlace(BlockState p_220082_1_, @NotNull Level p_220082_2_, @NotNull BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
        if (!p_220082_4_.is(p_220082_1_.getBlock()) && !p_220082_2_.isClientSide && p_220082_2_.getBlockEntity(p_220082_3_) == null) {
            this.checkIfExtend(p_220082_2_, p_220082_3_, p_220082_1_);
        }

    }

    protected static @NotNull Direction getConnectedDirection(BlockState p_196365_0_) {
        switch (p_196365_0_.getValue(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return p_196365_0_.getValue(FACING);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING,
                        direction.getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_,
                                  Level p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }
        return getConnectedDirection(p_196271_1_).getOpposite() == p_196271_2_
                && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_,
                p_196271_6_);
    }

    @Override
    public void degradeBlock(BlockPos pos, Level level) {
        level.destroyBlock(pos,false);
    }

    @Override
    public int getMaxHealth(BlockPos pos, BlockState state, Level level) {
        return maxHealth;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SpikesBlockEntity(pos,state);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING, FACE, WATERLOGGED, EXTENDED);
    }
}
