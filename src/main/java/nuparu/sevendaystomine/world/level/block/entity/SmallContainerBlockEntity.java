package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.world.level.block.IContainerBlockWithSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallContainerBlockEntity extends ItemHandlerBlockEntity<ItemHandlerNameable> {
    protected static final int INVENTORY_SIZE = 9;
    protected int openCount;

    public SmallContainerBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.SMALL_CONTAINER.get(), pos, state);
    }

    public SmallContainerBlockEntity(BlockEntityType<? extends SmallContainerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(INVENTORY_SIZE, Component.translatable("container." + ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath()));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getInventory().getDisplayName();
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @NotNull Inventory playerInventory, @NotNull Player player) {
        return ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
    }

    @Override
    public void onContainerOpened(Player player) {
        if (level.isClientSide()) return;
        if (this.openCount == 0 && getBlockState().getBlock() instanceof IContainerBlockWithSounds blockWithSounds) {
            SoundEvent sound = blockWithSounds.getOpeningSound(level, player);
            if (sound != null) {
                this.level.playSound(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, sound, SoundSource.BLOCKS, MathUtils.getFloatInRange(0.45f, 0.55f), MathUtils.getFloatInRange(0.75f, 1.15f));
            }
        }
        this.openCount++;
    }

    @Override
    public void onContainerClosed(Player player) {
        if (level.isClientSide()) return;
        this.openCount--;
        if (this.openCount == 0 && getBlockState().getBlock() instanceof IContainerBlockWithSounds blockWithSounds) {
            SoundEvent sound = blockWithSounds.getClosingSound(level, player);
            if (sound != null) {
                this.level.playSound(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, sound, SoundSource.BLOCKS, MathUtils.getFloatInRange(0.45f, 0.55f), MathUtils.getFloatInRange(0.75f, 1.15f));
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(Player player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }
}
