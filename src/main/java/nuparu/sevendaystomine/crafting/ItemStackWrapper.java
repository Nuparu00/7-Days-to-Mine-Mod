package nuparu.sevendaystomine.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class ItemStackWrapper {
	private ItemStack stack;

	public ItemStackWrapper(ItemStack stack) {
		this.stack = stack;
	}

	public ItemStack getItemStack() {
		return stack;
	}
	
	public int getStackSize() {
		if(stack == null || stack.isEmpty()) return 0;
		return stack.getCount();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ItemStackWrapper)) return false;
		ItemStackWrapper wrapper = (ItemStackWrapper)obj;
		if ((stack.isEmpty() && wrapper.getItemStack().isEmpty()) || (ItemStack.areItemsEqual(stack, wrapper.getItemStack()))) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "ItemStackWrapper ["+stack.toString()+"]";
	}
	
	public static List<ItemStackWrapper> wrapList(List<ItemStack> itemStacks, boolean perfect){
		List<ItemStackWrapper> list = new ArrayList<ItemStackWrapper>();
		for(ItemStack stack : itemStacks) {
			if(stack.isEmpty()) continue;
			list.add(new ItemStackWrapper(stack));
		}
		return list;
	}
	
}