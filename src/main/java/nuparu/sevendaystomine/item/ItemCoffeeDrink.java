package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemCoffeeDrink extends ItemDrink {

	int amplifier;
	int effectTime;
	public ItemCoffeeDrink(int amount, int thirst, int stamina, int amplifier, int effectTime) {
		super(amount, thirst, stamina);
		this.amplifier = amplifier;
		this.effectTime = effectTime;
	}

	public ItemCoffeeDrink(int amount, float saturation, int thirst, int stamina, int amplifier, int effectTime) {
		super(amount, saturation, thirst, stamina);
		this.amplifier = amplifier;
		this.effectTime = effectTime;
	}
	
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		PotionEffect effect_new = new PotionEffect(Potions.caffeineBuzz, effectTime, amplifier, false, false);
		effect_new.setCurativeItems(new ArrayList<ItemStack>());
		player.addPotionEffect(effect_new);
		
	}

}
