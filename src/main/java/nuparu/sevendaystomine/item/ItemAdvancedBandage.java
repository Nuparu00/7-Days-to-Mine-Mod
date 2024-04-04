package nuparu.sevendaystomine.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemAdvancedBandage extends ItemBandage {
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur <= this.getMaxItemUseDuration(stack) * 0.1f) {
				if (!entityplayer.capabilities.isCreativeMode) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						entityplayer.inventory.deleteStack(stack);
					}
				}
				entityplayer.removePotionEffect(Potions.bleeding);
				entityplayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
			}
		}
	}
}
