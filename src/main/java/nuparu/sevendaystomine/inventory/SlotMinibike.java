package nuparu.sevendaystomine.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMinibike extends SlotItemHandler {

	protected Item validItem;

	public SlotMinibike(IItemHandler inventoryIn, int index, int xPosition, int yPosition, Item validItem) {
		super(inventoryIn, index, xPosition, yPosition);
		this.validItem = validItem;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == validItem;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {

	}
}
