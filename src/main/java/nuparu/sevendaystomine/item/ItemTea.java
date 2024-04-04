package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemTea extends ItemDrink {

	public ItemTea(int amount, int thirst, int stamina) {
		super(amount, thirst, stamina);
	}

	public ItemTea(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, thirst, stamina);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		player.removePotionEffect(Potions.dysentery);
	}

}
