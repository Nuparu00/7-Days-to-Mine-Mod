package nuparu.sevendaystomine.block.repair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Repair {
	Block block;
	private ItemStack itemStack;
	private float percentage;

	public Repair(Block block, ItemStack itemStack, float percentage) {
		this.block = block;
		this.setItemStack(itemStack);
		this.setPercentage(percentage);
	}

	public Repair(Block block, Item item, float percentage) {
		this(block, new ItemStack(item), percentage);
	}

	/**
	 * @return the itemStack
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * @param itemStack the itemStack to set
	 */
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	/**
	 * @return the percentage
	 */
	public float getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
}