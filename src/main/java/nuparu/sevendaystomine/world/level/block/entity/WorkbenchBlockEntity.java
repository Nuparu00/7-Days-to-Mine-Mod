package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.inventory.block.ContainerGrill;
import nuparu.sevendaystomine.world.inventory.block.ContainerWorkbench;
import nuparu.sevendaystomine.world.inventory.block.ContainerWorkbenchUncrafting;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class WorkbenchBlockEntity extends ItemHandlerBlockEntity<ItemHandlerNameable> implements Container {
    private static final int INVENTORY_SIZE = 1;
    public static final Component DEFAULT_NAME = Component.translatable("container.workbench");

    public WorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WORKBENCH.get(), pos, state);
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP.get();
        }
    };
    }

    @Override
    public Component getDisplayName() {
        return getInventory().getDisplayName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player) {
        return playerInventory.player.isCrouching() ? ContainerWorkbenchUncrafting.createContainerServerSide(windowID, playerInventory, this) : ContainerWorkbench.createContainerServerSide(windowID, playerInventory, this);
    }

    @Override
    public void onContainerOpened(Player player) {

    }

    @Override
    public void onContainerClosed(Player player) {

    }

    @Override
    public boolean isUsableByPlayer(Player player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public int getContainerSize() {
        return getInventory().getSlots();
    }

    @Override
    public boolean isEmpty() {
        return getInventory().isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return getInventory().getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return null;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        //getInventory().setStackInSlot(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return isUsableByPlayer(player);
    }

    @Override
    public void clearContent() {
        getInventory().clear();
    }


    public void dropAllContents(Level world, BlockPos blockPos) {
        Containers.dropContents(world, blockPos, this.getDrops());
    }
}
