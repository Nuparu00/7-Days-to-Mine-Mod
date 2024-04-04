package nuparu.sevendaystomine.util.item;

import net.minecraft.item.ItemStack;

public class InventoryCache {
	public ItemStack[] inventory;
	public ItemStack currentItem;
	public ItemStack backpack;
	public int index;

	public InventoryCache(ItemStack[] inventory, ItemStack currentItem, ItemStack backpack, int index) {
		this.inventory = inventory;
		this.currentItem = currentItem;
		this.index = index;
		this.backpack = backpack;
	}
}
