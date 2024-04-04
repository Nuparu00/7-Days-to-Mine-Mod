package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemAlcoholDrink extends ItemDrink {

	public ItemAlcoholDrink(int amount, int thirst, int stamina) {
		super(amount, thirst, stamina);
	}

	public ItemAlcoholDrink(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, thirst, stamina);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		if(worldIn.isRemote) return;
		if (!player.isPotionActive(Potions.alcoholBuzz) && !player.isPotionActive(Potions.drunk) && !player.isPotionActive(Potions.alcoholPoison)) {
			PotionEffect effect = new PotionEffect(Potions.alcoholBuzz, 6000, 4, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			player.addPotionEffect(effect);
		} else if (player.isPotionActive(Potions.alcoholBuzz) && itemRand.nextInt(3) != 0) {
			PotionEffect effect = player.getActivePotionEffect(Potions.alcoholBuzz) != null
					? player.getActivePotionEffect(Potions.alcoholBuzz)
					: player.getActivePotionEffect(Potions.drunk);
			if (worldIn.rand.nextInt(Math.max(6000 - (effect.getDuration() * effect.getAmplifier()), 1)) == 0) {
				PotionEffect effect_new = new PotionEffect(Potions.drunk, 6000, 4, false, false);
				effect_new.setCurativeItems(new ArrayList<ItemStack>());
				player.addPotionEffect(effect_new);
				player.removePotionEffect(Potions.alcoholBuzz);
			}
		} else if (player.isPotionActive(Potions.drunk) || player.isPotionActive(Potions.alcoholPoison) && itemRand.nextInt(5) != 0) {
			PotionEffect effect = player.getActivePotionEffect(Potions.drunk);
			if(effect != null) {
			if (worldIn.rand.nextInt(Math.max(6000 - (effect.getDuration() * effect.getAmplifier()), 1)) == 0) {
				PotionEffect effect_new = new PotionEffect(Potions.alcoholPoison, 6000, 4, false, false);
				effect_new.setCurativeItems(new ArrayList<ItemStack>());
				player.addPotionEffect(effect_new);
				player.removePotionEffect(Potions.drunk);
			}
			}
			else {
				PotionEffect effect_new = new PotionEffect(Potions.alcoholPoison, 6000, 4, false, false);
				effect_new.setCurativeItems(new ArrayList<ItemStack>());
				player.addPotionEffect(effect_new);
			}
		}
	}

}
