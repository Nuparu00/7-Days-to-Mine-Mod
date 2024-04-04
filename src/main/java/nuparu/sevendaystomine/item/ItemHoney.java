package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.PlayerUtils;

public class ItemHoney extends ItemDrink {

	public ItemHoney() {
		super(5, 0, 300, 150);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		int time = iep.getInfectionTime();
		if (player instanceof EntityPlayerMP && PlayerUtils.getInfectionStage(time) == 0) {
			player.removePotionEffect(Potions.infection);
			iep.setInfectionTime(-1);
		}
	}
}
