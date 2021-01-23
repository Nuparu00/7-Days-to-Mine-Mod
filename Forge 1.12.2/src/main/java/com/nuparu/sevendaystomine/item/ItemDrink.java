package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDrink extends ItemFood {
    private int thirst;
    private int stamina;
    
	public ItemDrink(int amount, int thirst, int stamina) {
		super(amount, false);
		setAlwaysEdible();
		this.thirst = thirst;
		this.stamina = stamina;
	}

	public ItemDrink(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, false);
		setAlwaysEdible();
		this.thirst = thirst;
		this.stamina = stamina;
	}
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		if(worldIn.isRemote) return;
		
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		
		extendedPlayer.addThirst(thirst);
		extendedPlayer.addStamina(stamina);
		player.removePotionEffect(Potions.thirst);
		
		if(getContainerItem() != null) {
			ItemStack itemStack = new ItemStack(getContainerItem());
			if (!player.addItemStackToInventory(itemStack))
            {
                player.dropItem(itemStack, false);
            }
		}
    }
	
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
}
