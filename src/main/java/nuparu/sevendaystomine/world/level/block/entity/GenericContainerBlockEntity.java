package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.world.level.block.IContainerBlockWithSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GenericContainerBlockEntity extends ItemHandlerBlockEntity<ItemHandlerNameable> {
    protected final int inventorySize;
    protected int openCount;

    public GenericContainerBlockEntity(BlockEntityType<? extends GenericContainerBlockEntity> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        this.inventorySize = inventorySize;
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(inventorySize, Component.translatable("container." + ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath()));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getInventory().getDisplayName();
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


    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int windowID, @NotNull Inventory playerInventory, @NotNull Player player);

}
