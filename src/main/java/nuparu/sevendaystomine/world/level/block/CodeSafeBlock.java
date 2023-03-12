package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.OpenGuiClientMessage;
import nuparu.sevendaystomine.network.messages.SyncTileEntityMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.level.block.entity.CodeSafeBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeSafeBlock extends BlockBase implements SimpleWaterloggedBlock, EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");


    public CodeSafeBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(LOCKED, Boolean.TRUE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_53304_) {
        FluidState fluidstate = p_53304_.getLevel().getFluidState(p_53304_.getClickedPos());
        return super.getStateForPlacement(p_53304_).setValue(FACING, p_53304_.getNearestLookingDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }


    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player,
                                          @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult) {
        if (worldIn.isClientSide())
            return InteractionResult.SUCCESS;
        MenuProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
        ItemHandlerBlockEntity tileEntity = (ItemHandlerBlockEntity) namedContainerProvider;
        tileEntity.unpackLootTable(player);
        if (!(player instanceof ServerPlayer serverPlayerEntity))
            return InteractionResult.FAIL;
        if (namedContainerProvider != null) {

            CompoundTag nbt = tileEntity.saveWithoutMetadata();
            nbt.remove("CorrectCode");
            PacketManager.sendTo(PacketManager.syncTileEntity, new SyncTileEntityMessage(nbt, pos), serverPlayerEntity);
            if (player.isCrouching() || ((CodeSafeBlockEntity) namedContainerProvider).isLocked()) {
                PacketManager.sendTo(PacketManager.openGuiClient, new OpenGuiClientMessage(0, pos.getX(), pos.getY(), pos.getZ()), (ServerPlayer) player);
                return InteractionResult.SUCCESS;
            }

            NetworkHooks.openScreen(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (level1, blockPos, blockState, t) -> {
            if (t instanceof CodeSafeBlockEntity tile) {
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
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }


    @Override
    public void setPlacedBy(Level worldIn, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof CodeSafeBlockEntity safe) {

            CompoundTag nbt = stack.getOrCreateTag();
            if (nbt.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
                CompoundTag tileNBT = nbt.getCompound("BlockEntityTag");
                tileNBT.putInt("x", pos.getX());
                tileNBT.putInt("y", pos.getY());
                tileNBT.putInt("z", pos.getZ());
                safe.load(tileNBT);
            }
            if (stack.hasCustomHoverName()) {
                String displayName = stack.getHoverName().getString();
                if (Utils.isNumeric(displayName)) {
                    double d = Double.parseDouble(displayName);
                    if ((d % 1) == 0 && d >= 0 && d < 1000) {
                        safe.setCorrectCode((int) d);
                        int selectedCode = 0;
                        while ((int) d == selectedCode) {
                            RandomSource random = worldIn.random;
                            selectedCode = random.nextInt(1000);
                        }
                        safe.setSelectedCode(selectedCode, null);
                        safe.setInit(true);
                    }
                } else {
                    safe.setDisplayName(Component.literal(displayName));
                }
            }
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_53323_, @NotNull Direction p_53324_, @NotNull BlockState p_53325_, @NotNull LevelAccessor p_53326_, @NotNull BlockPos p_53327_, @NotNull BlockPos p_53328_) {
        if (p_53323_.getValue(WATERLOGGED)) {
            p_53326_.scheduleTick(p_53327_, Fluids.WATER, Fluids.WATER.getTickDelay(p_53326_));
        }

        return super.updateShape(p_53323_, p_53324_, p_53325_, p_53326_, p_53327_, p_53328_);
    }

    public @NotNull BlockState rotate(BlockState p_154354_, Rotation p_154355_) {
        return p_154354_.setValue(FACING, p_154355_.rotate(p_154354_.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState p_154351_, Mirror p_154352_) {
        return p_154351_.setValue(FACING, p_154352_.mirror(p_154351_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING, WATERLOGGED, LOCKED);
    }

    public static void setState(boolean locked, Level worldIn, BlockPos pos) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        BlockState BlockState = worldIn.getBlockState(pos);

        worldIn.setBlock(pos, ModBlocks.CODE_SAFE.get().defaultBlockState()
                .setValue(FACING, BlockState.getValue(FACING)).setValue(LOCKED, locked), 3);
        worldIn.sendBlockUpdated(pos, BlockState, worldIn.getBlockState(pos), 2);

        if (tileentity != null) {
            worldIn.setBlockEntity(tileentity);
        }
    }
/*
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack p_56193_, @javax.annotation.Nullable BlockGetter p_56194_, List<Component> p_56195_, TooltipFlag p_56196_) {
        super.appendHoverText(p_56193_, p_56194_, p_56195_, p_56196_);
        CompoundTag compoundtag = BlockItem.getBlockEntityData(p_56193_);
        if (compoundtag != null) {
            if (compoundtag.contains("LootTable", 8)) {
                p_56195_.add(Component.literal("???????"));
            }

            if (compoundtag.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundtag, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableComponent mutablecomponent = itemstack.getHoverName().copy();
                            mutablecomponent.append(" x").append(String.valueOf(itemstack.getCount()));
                            p_56195_.add(mutablecomponent);
                        }
                    }
                }

                if (j - i > 0) {
                    p_56195_.add(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC));
                }
            }
        }

    }*/

    @Override
    public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos blockPos, BlockState newState, boolean isMoving) {

        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileentity = world.getBlockEntity(blockPos);

            if (!world.isClientSide()) {
                if (tileentity instanceof CodeSafeBlockEntity codeSafe) {
                    {
                        if (codeSafe.isLocked()) {
                            ItemStack itemstack = new ItemStack(Item.byBlock(this));
                            CompoundTag compoundTag = new CompoundTag();
                            compoundTag.put("BlockEntityTag", codeSafe.saveWithoutMetadata());
                            itemstack.setTag(compoundTag);
                            Containers.dropItemStack(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemstack);
                        } else {
                            Containers.dropContents(world, blockPos, codeSafe.getDrops());
                        }

                    }
                }
            }
        }

        super.onRemove(state, world, blockPos, newState, isMoving);
    }


    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return state.getBlock() instanceof CodeSafeBlock && !state.getValue(LOCKED);
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level p_56224_, @NotNull BlockPos p_56225_) {
        return hasAnalogOutputSignal(state) ? AbstractContainerMenu.getRedstoneSignalFromContainer((Container) p_56224_.getBlockEntity(p_56225_)) : 0;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter p_56202_, @NotNull BlockPos p_56203_, @NotNull BlockState p_56204_) {
        ItemStack itemstack = super.getCloneItemStack(p_56202_, p_56203_, p_56204_);
        p_56202_.getBlockEntity(p_56203_, BlockEntityType.SHULKER_BOX).ifPresent((p_187446_) -> p_187446_.saveToItem(itemstack));
        return itemstack;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CodeSafeBlockEntity(pos, state);
    }
}