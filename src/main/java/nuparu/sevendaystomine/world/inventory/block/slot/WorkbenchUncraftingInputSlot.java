package nuparu.sevendaystomine.world.inventory.block.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.world.inventory.block.ContainerWorkbenchUncrafting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class WorkbenchUncraftingInputSlot extends Slot {
    ContainerWorkbenchUncrafting workbench;

    public WorkbenchUncraftingInputSlot(Container container, int index, int xPosition, int yPosition, ContainerWorkbenchUncrafting workbench) {
        super(container, index, xPosition, yPosition);
        this.workbench = workbench;
    }

    @Override
    public void onQuickCraft(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
        super.onQuickCraft(p_75220_1_, p_75220_2_);
        workbench.updateCraftingGrid();

    }

    @Override
    public void set(@NotNull ItemStack stack) {
        ItemStack old = this.getItem();
        super.set(stack);
        if(!ItemStack.matches(old,stack)) {
            workbench.updateCraftingGrid();
        }
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        ItemStack stack = super.remove(amount);
        if(amount > 0) {
            workbench.updateCraftingGrid();
        }
        return stack;
    }
}