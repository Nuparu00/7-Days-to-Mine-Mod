package nuparu.sevendaystomine.world.level.block;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.world.level.block.entity.CombustionGeneratorBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CombustionGeneratorBlock extends AbstractFurnaceBlock implements IBlockBase, EntityBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape X_BASE = Block.box(2.0D, 0.0D, 0.0D, 14.0D, 12.0D, 16.0D);
    private static final VoxelShape Z_BASE = Block.box(0.0D, 0.0D, 2.0D, 16.0D, 12.0D, 14.0D);

    private static final VoxelShape Z_TOP = Block.box(0.0D, 13.0D, 3.0D, 16.0D, 15.0D, 13.0D);
    private static final VoxelShape X_TOP = Block.box(3.0D, 13.0D, 0.0D, 13.0D, 15.0D, 16.0D);

    private static final VoxelShape X_AXIS_AABB = Shapes.or(X_BASE, X_TOP);
    private static final VoxelShape Z_AXIS_AABB = Shapes.or(Z_BASE, Z_TOP);

    public CombustionGeneratorBlock(BlockBehaviour.Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE).setValue(WATERLOGGED, false));
    }

    @Override
    protected void openContainer(@NotNull Level p_48690_, @NotNull BlockPos p_48691_, @NotNull Player p_48692_) {
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CombustionGeneratorBlockEntity(pos, state);
    }

    public @NotNull FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_53304_) {
        FluidState fluidstate = p_53304_.getLevel().getFluidState(p_53304_.getClickedPos());
        return super.getStateForPlacement(p_53304_).setValue(FACING, p_53304_.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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
        p_53334_.add(FACING, LIT, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        Direction direction = p_220053_1_.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }


    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);

    }

    @Override
    public void setPlacedBy(@NotNull Level p_48694_, @NotNull BlockPos p_48695_, @NotNull BlockState p_48696_, @NotNull LivingEntity p_48697_, ItemStack p_48698_) {
        if (p_48698_.hasCustomHoverName()) {
            BlockEntity blockentity = p_48694_.getBlockEntity(p_48695_);
            if (blockentity instanceof CombustionGeneratorBlockEntity generatorBlockEntity) {
                generatorBlockEntity.setDisplayName(p_48698_.getHoverName());
            }
        }

    }

    @Override
    public void onRemove(BlockState p_48713_, @NotNull Level p_48714_, @NotNull BlockPos p_48715_, BlockState p_48716_, boolean p_48717_) {
        if (!p_48713_.is(p_48716_.getBlock())) {
            BlockEntity blockentity = p_48714_.getBlockEntity(p_48715_);
            if (blockentity instanceof CombustionGeneratorBlockEntity generatorBlockEntity) {
                if (p_48714_ instanceof ServerLevel) {
                    //Containers.dropContents(p_48714_, p_48715_, (ForgeBlockEntity)blockentity);
                    generatorBlockEntity.dropAllContents(p_48714_, p_48715_);
                }

                p_48714_.updateNeighbourForOutputSignal(p_48715_, this);
            }

            super.onRemove(p_48713_, p_48714_, p_48715_, p_48716_, p_48717_);
        }
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (level1, blockPos, blockState, t) -> {
            if (t instanceof CombustionGeneratorBlockEntity tile) {
                tile.tick();
            }
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player,
                                          @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult) {
        if (worldIn.isClientSide())
            return InteractionResult.SUCCESS;

        MenuProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
        if (namedContainerProvider != null) {
            CombustionGeneratorBlockEntity blockEntity = (CombustionGeneratorBlockEntity) namedContainerProvider;
            blockEntity.unpackLootTable(player);
            if(player.isCrouching()){
                blockEntity.switchOn();
                return InteractionResult.SUCCESS;
            }
            if (!(player instanceof ServerPlayer serverPlayerEntity))
                return InteractionResult.FAIL;
            NetworkHooks.openScreen(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, @NotNull Level p_180655_2_, @NotNull BlockPos p_180655_3_, @NotNull RandomSource p_180655_4_) {
        if (p_180655_1_.getValue(LIT)) {
            double d0 = (double) p_180655_3_.getX() + 0.5D;
            double d1 = p_180655_3_.getY() + 0.125;
            double d2 = (double) p_180655_3_.getZ() + 0.5D;
            if (p_180655_4_.nextDouble() < 0.1D) {
                p_180655_2_.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F,
                        1.0F, false);
            }

            Direction direction = p_180655_1_.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = p_180655_4_.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.4D : d4;
            double d6 = p_180655_4_.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.4D : d4;
            p_180655_2_.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            p_180655_2_.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return ModCreativeModeTabs.TAB_ELECTRICITY;
    }
}

