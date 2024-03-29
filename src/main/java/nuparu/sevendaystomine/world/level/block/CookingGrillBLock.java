package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.level.block.entity.GrillBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CookingGrillBLock extends CookwareBlock implements EntityBlock {

    public static final VoxelShape SHAPE = Block.box(2,0,2,14,1,14);
    public CookingGrillBLock(Properties p_49795_) {
        super(p_49795_, SHAPE,true);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult rayTraceResult) {

        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == ModBlocks.BEAKER.get()) {
            if (!worldIn.isClientSide()) {
                worldIn.setBlockAndUpdate(pos, ModBlocks.BEAKER.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(CAMPFIRE, true));
                worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS,
                        MathUtils.getFloatInRange(0.9f, 1.1f), MathUtils.getFloatInRange(0.9f, 1.1f));
            }
            return InteractionResult.SUCCESS;
        }

        if (worldIn.isClientSide())
            return InteractionResult.SUCCESS;

        if(!worldIn.getBlockState(pos.above()).canBeReplaced()) return  InteractionResult.SUCCESS;
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
            if (blockentity instanceof GrillBlockEntity) {
                ((GrillBlockEntity)blockentity).setDisplayName(p_48698_.getHoverName());
            }
        }

    }

    @Override
    public void onRemove(BlockState p_48713_, @NotNull Level p_48714_, @NotNull BlockPos p_48715_, BlockState p_48716_, boolean p_48717_) {
        if (!p_48713_.is(p_48716_.getBlock())) {
            BlockEntity blockentity = p_48714_.getBlockEntity(p_48715_);
            if (blockentity instanceof GrillBlockEntity) {
                if (p_48714_ instanceof ServerLevel) {
                    //Containers.dropContents(p_48714_, p_48715_, (ForgeBlockEntity)blockentity);
                    ((GrillBlockEntity)blockentity).dropAllContents(p_48714_,p_48715_);
                    ((GrillBlockEntity)blockentity).getRecipesToAwardAndPopExperience(p_48714_, Vec3.atCenterOf(p_48715_));
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
            if (t instanceof GrillBlockEntity tile) {
                tile.tick();
            }
        };
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


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GrillBlockEntity(pos,state);
    }

}
