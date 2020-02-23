package com.nuparu.sevendaystomine.util.item;

import net.minecraft.item.ItemStack;

public class InventoryCache {
	public ItemStack[] inventory;
	public ItemStack currentItem;
	public int index;

	public InventoryCache(ItemStack[] inventory, ItemStack currentItem, int index) {
		this.inventory = inventory;
		this.currentItem = currentItem;
		this.index = index;
	}
}
