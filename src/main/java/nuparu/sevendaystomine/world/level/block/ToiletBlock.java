package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.SmallContainerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToiletBlock extends WaterloggableHorizontalBlockBase implements EntityBlock, IContainerBlockWithSounds {


    private static final VoxelShape SOUTH = Shapes.or(Block.box(1, 15, -1, 15, 16, 5),Block.box(2, 6, 0, 14, 16, 4),Block.box(2, 0, 2, 14, 7, 16));
    private static final VoxelShape NORTH =  Shapes.or(Block.box(1, 15, 11, 15, 16, 17),Block.box(2, 6, 12, 14, 16, 16),Block.box(2, 0, 0, 14, 7, 14));
    private static final VoxelShape WEST =  Shapes.or(Block.box(11, 15, 1, 17, 16, 15),Block.box(12, 6, 2, 16, 16, 14),Block.box(0, 0, 2, 14, 7, 14));
    private static final VoxelShape EAST =  Shapes.or(Block.box(-1, 15, 1, 5, 16, 15),Block.box(0, 6, 2, 4, 16, 14),Block.box(2, 0, 2, 16, 7, 14));

    public ToiletBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SmallContainerBlockEntity(pos,state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        switch (state.getValue(FACING)) {
            default:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
    }
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player,
                                          @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult) {
        if (worldIn.isClientSide())
            return InteractionResult.SUCCESS;

        MenuProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
        if (namedContainerProvider != null) {
            ItemHandlerBlockEntity tileEntity = (ItemHandlerBlockEntity)namedContainerProvider;
            tileEntity.unpackLootTable(player);
            if (!(player instanceof ServerPlayer serverPlayerEntity))
                return InteractionResult.FAIL;
            NetworkHooks.openScreen(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(@NotNull Level p_48694_, @NotNull BlockPos p_48695_, @NotNull BlockState p_48696_, LivingEntity p_48697_, ItemStack p_48698_) {
        if (p_48698_.hasCustomHoverName()) {
            BlockEntity blockentity = p_48694_.getBlockEntity(p_48695_);
            if (blockentity instanceof SmallContainerBlockEntity) {
                ((SmallContainerBlockEntity)blockentity).setDisplayName(p_48698_.getHoverName());
            }
        }

    }

    @Override
    public void onRemove(BlockState p_48713_, @NotNull Level p_48714_, @NotNull BlockPos p_48715_, BlockState p_48716_, boolean p_48717_) {
        if (!p_48713_.is(p_48716_.getBlock())) {
            BlockEntity blockentity = p_48714_.getBlockEntity(p_48715_);
            if (blockentity instanceof SmallContainerBlockEntity) {
                if (p_48714_ instanceof ServerLevel) {
                    ((SmallContainerBlockEntity)blockentity).dropAllContents(p_48714_,p_48715_);
                }

                p_48714_.updateNeighbourForOutputSignal(p_48715_, this);
            }

            super.onRemove(p_48713_, p_48714_, p_48715_, p_48716_, p_48717_);
        }
    }

    @Override
    public boolean triggerEvent(@NotNull BlockState p_49226_, @NotNull Level p_49227_, @NotNull BlockPos p_49228_, int p_49229_, int p_49230_) {
        super.triggerEvent(p_49226_, p_49227_, p_49228_, p_49229_, p_49230_);
        BlockEntity blockentity = p_49227_.getBlockEntity(p_49228_);
        return blockentity != null && blockentity.triggerEvent(p_49229_, p_49230_);
    }

    @Override
    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(@NotNull BlockState p_49234_, Level p_49235_, @NotNull BlockPos p_49236_) {
        BlockEntity blockentity = p_49235_.getBlockEntity(p_49236_);
        return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
    }

    @Override
    @Nullable
    public SoundEvent getOpeningSound(Level level, Player player){
        return ModSounds.CABINET_OPEN.get();
    }

    @Override
    @Nullable
    public SoundEvent getClosingSound(Level level, Player player){
        return ModSounds.CABINET_CLOSE.get();
    }

}
