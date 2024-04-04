package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.potions.Potions;

public class ItemCannedSoup extends ItemCannedFood {

	int thirst;
	int stamina;

	public ItemCannedSoup(int amount, float saturation, boolean isWolfFood, int maxBites, int thirst, int stamina) {
		super(amount, saturation, isWolfFood, maxBites);
		this.setAlwaysEdible();
		this.thirst = thirst;
		this.stamina = stamina;
	}

	public ItemCannedSoup(int amount, boolean isWolfFood, int maxBites, int thirst, int stamina) {
		super(amount, isWolfFood, maxBites);
		this.setAlwaysEdible();
		this.thirst = thirst;
		this.stamina = stamina;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		if (ModConfig.players.thirstSystem) {
			extendedPlayer.addThirst(thirst);
		}
		if (ModConfig.players.staminaSystem) {
			extendedPlayer.addThirst(thirst);
			extendedPlayer.addStamina(stamina);
		}
		player.removePotionEffect(Potions.thirst);
	}

}
