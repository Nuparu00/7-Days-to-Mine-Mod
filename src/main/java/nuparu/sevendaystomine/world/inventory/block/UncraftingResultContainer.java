package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class UncraftingResultContainer implements Container, RecipeHolder {
    private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(25, ItemStack.EMPTY);
    @Nullable
    private Recipe<?> recipeUsed;

    public int getContainerSize() {
        return 25;
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.itemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public @NotNull ItemStack getItem(int slot) {
        return this.itemStacks.get(slot);
    }

    public @NotNull ItemStack removeItem(int slot, int p_40150_) {
        return ContainerHelper.takeItem(this.itemStacks, slot);
    }

    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.itemStacks, slot);
    }

    public void setItem(int slot, @NotNull ItemStack p_40153_) {
        this.itemStacks.set(slot, p_40153_);
    }

    public void setChanged() {
    }

    public boolean stillValid(@NotNull Player p_40155_) {
        return true;
    }

    public void clearContent() {
        this.itemStacks.clear();
    }

    public void setRecipeUsed(@Nullable Recipe<?> p_40157_) {
        this.recipeUsed = p_40157_;
    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}
