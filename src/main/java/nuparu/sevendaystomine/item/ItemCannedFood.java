package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItems;

public class ItemCannedFood extends ItemFood {

	public ItemCannedFood(int amount, boolean isWolfFood, int maxBites) {
		super(amount, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}

	public ItemCannedFood(int amount, float saturation, boolean isWolfFood, int maxBites) {
		super(amount, saturation, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		if(stack.getItemDamage()+1 == stack.getMaxDamage() && getContainerItem() != null) {
			ItemStack itemStack = new ItemStack(getContainerItem());
			if (!player.addItemStackToInventory(itemStack))
            {
                player.dropItem(itemStack, false);
            }
		}
    }

}
