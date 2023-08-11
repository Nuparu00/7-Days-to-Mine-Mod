package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UncraftingContainer implements Container, StackedContentsCompatible {
    private final NonNullList<ItemStack> items;
    private final int width;
    private final int height;
    public final AbstractContainerMenu menu;

    public UncraftingContainer(AbstractContainerMenu p_39325_, int p_39326_, int p_39327_) {
        this.items = NonNullList.withSize(p_39326_ * p_39327_, ItemStack.EMPTY);
        this.menu = p_39325_;
        this.width = p_39326_;
        this.height = p_39327_;
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public @NotNull ItemStack getItem(int p_39332_) {
        return p_39332_ >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(p_39332_);
    }

    public @NotNull ItemStack removeItemNoUpdate(int p_39344_) {
        return ContainerHelper.takeItem(this.items, p_39344_);
    }

    public @NotNull ItemStack removeItem(int p_39334_, int p_39335_) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, p_39334_, p_39335_);
        if (!itemstack.isEmpty()) {
            this.menu.slotsChanged(this);
        }

        return itemstack;
    }

    public void setItem(int p_39337_, @NotNull ItemStack p_39338_) {
        this.items.set(p_39337_, p_39338_);
        this.menu.slotsChanged(this);
    }

    public void setChanged() {
    }

    public boolean stillValid(@NotNull Player p_39340_) {
        return true;
    }

    public void clearContent() {
        this.items.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void fillStackedContents(@NotNull StackedContents p_39342_) {
        for(ItemStack itemstack : this.items) {
            p_39342_.accountSimpleStack(itemstack);
        }

    }
    public List<ItemStack> getItems() {
        return List.copyOf(this.items);
    }
}