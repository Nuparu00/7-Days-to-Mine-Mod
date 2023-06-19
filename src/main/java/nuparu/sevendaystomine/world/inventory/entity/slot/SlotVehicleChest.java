package nuparu.sevendaystomine.world.inventory.entity.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.world.inventory.entity.ContainerMinibike;
import org.jetbrains.annotations.NotNull;

public class SlotVehicleChest extends SlotItemHandler {

    final ContainerMinibike containerMinibike;

    public SlotVehicleChest(IItemHandler itemHandler, int index, int xPosition, int yPosition, ContainerMinibike containerMinibike) {
        super(itemHandler, index, xPosition, yPosition);
        this.containerMinibike = containerMinibike;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() == Blocks.CHEST.asItem();
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.hasItem()) {
            if (this.getItem().getItem() == Blocks.CHEST.asItem()) {
                if (!containerMinibike.addedChest) {
                    containerMinibike.bindChest();
                }
            }
        } else {
            if (containerMinibike.addedChest) {
                containerMinibike.unbindChest();
            }
        }
    }
}