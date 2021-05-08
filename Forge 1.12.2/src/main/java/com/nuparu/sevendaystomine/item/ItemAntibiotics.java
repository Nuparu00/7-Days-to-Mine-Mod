package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.advancements.ModTriggers;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.ExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.PlayerUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
		if (player instanceof EntityPlayerMP && (stage == 2 || stage == 3)) {
			ModTriggers.CURE.trigger((EntityPlayerMP) player);
		}
		player.removePotionEffect(Potions.infection);
		iep.setInfectionTime(-1);
	}
}
