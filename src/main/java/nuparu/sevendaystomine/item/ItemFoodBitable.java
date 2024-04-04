package nuparu.sevendaystomine.item;

import net.minecraft.item.ItemFood;
import nuparu.sevendaystomine.init.ModItems;

public class ItemFoodBitable extends ItemFood {

	public ItemFoodBitable(int amount, boolean isWolfFood, int maxBites) {
		super(amount, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}

	public ItemFoodBitable(int amount, float saturation, boolean isWolfFood, int maxBites) {
		super(amount, saturation, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}
}
