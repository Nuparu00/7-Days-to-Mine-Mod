package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.PlayerUtils;

public class ItemAntibiotics extends ItemFood {
	public ItemAntibiotics() {
		super(0, false);
		this.setAlwaysEdible();
		this.setCreativeTab(SevenDaysToMine.TAB_MEDICINE);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		int time = iep.getInfectionTime();
		int stage = PlayerUtils.getInfectionStage(time);
		if (player instanceof EntityPlayerMP && (stage >= PlayerUtils.getNumberOfstages()-1)) {
			ModTriggers.CURE.trigger((EntityPlayerMP) player);
		}
		player.removePotionEffect(Potions.infection);
		iep.setInfectionTime(-1);
	}
}
