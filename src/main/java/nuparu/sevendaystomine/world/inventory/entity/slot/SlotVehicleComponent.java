package nuparu.sevendaystomine.world.inventory.entity.slot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotVehicleComponent extends SlotItemHandler {

    protected Item validItem;

    public SlotVehicleComponent(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item validItem) {
        super(itemHandler, index, xPosition, yPosition);
        this.validItem = validItem;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() == validItem;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }
}
