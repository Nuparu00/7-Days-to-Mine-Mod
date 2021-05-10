package nuparu.sevendaystomine.util.item;

import java.io.Serializable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import nuparu.sevendaystomine.item.EnumLength;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.item.ItemQualitySword;
import nuparu.sevendaystomine.item.ItemQualityTool;
import nuparu.sevendaystomine.item.ItemScrewdriver;

public class ItemCache implements Serializable {
	private static final long serialVersionUID = 1L;

	public transient ItemStack longItem = ItemStack.EMPTY;
	public transient ItemStack shortItem_L = ItemStack.EMPTY;
	public transient ItemStack shortItem_R = ItemStack.EMPTY;
	public transient ItemStack backpack = ItemStack.EMPTY;

	public void selectCorrectItems(ItemStack[] inv, ItemStack currentItem, int index) {
		if (inv != null) {
			for (int i = 0; i < inv.length; i++) {
				ItemStack stack = inv[i];
				if (!longItem.isEmpty() && !shortItem_L.isEmpty() && !shortItem_R.isEmpty() && !backpack.isEmpty()) {
					break;
				}
				if (i != index) {
					if (stack != null && !stack.isEmpty()) {
						Item item = stack.getItem();
						if (item instanceof ItemFuelTool) {
							continue;
						}

						if (item instanceof ItemQualityTool) {
							if (((ItemQualityTool) item).length == EnumLength.SHORT) {
								if (shortItem_R.isEmpty()) {
									shortItem_R = stack;
								} else if (shortItem_L.isEmpty()) {
									shortItem_L = stack;
								}
								continue;
							}
							if (longItem.isEmpty()) {
								longItem = stack;
							}
							continue;
						}
						if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemHoe
								|| item instanceof ItemBow) {
							if (longItem.isEmpty()) {
								longItem = stack;
							}
							continue;
						}
						if (item instanceof ItemQualitySword) {
							if (((ItemQualitySword) item).length == EnumLength.SHORT) {
								if (shortItem_R.isEmpty()) {
									shortItem_R = stack;
								} else if (shortItem_L.isEmpty()) {
									shortItem_L = stack;
								}
								continue;
							}
							if (longItem.isEmpty()) {
								longItem = stack;
							}
							continue;
						}
						if (item instanceof ItemGun) {

							if (((ItemGun) item).getLength() == EnumLength.LONG) {
								if (longItem.isEmpty()) {
									longItem = stack;
								}
								continue;
							}
							if (shortItem_R.isEmpty()) {
								shortItem_R = stack;
							} else if (shortItem_L.isEmpty()) {
								shortItem_L = stack;
							}
							continue;
						}
						if (item instanceof ItemScrewdriver) {
							if (shortItem_R.isEmpty()) {
								shortItem_R = stack;
							} else if (shortItem_L.isEmpty()) {
								shortItem_L = stack;
							}
							continue;
						}
					}
				}
			}
		}
	}

	public boolean isEmpty() {
		return longItem.isEmpty() && shortItem_R.isEmpty() && shortItem_L.isEmpty() && backpack.isEmpty();
	}

	public BufferedCache serialize() {
		BufferedCache cache = new BufferedCache();
		cache.writeLongItem(longItem);
		cache.writeShortItem_L(shortItem_L);
		cache.writeShortItem_R(shortItem_R);
		cache.writeBackpack(backpack);
		return cache;
	}
}