package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.potions.Potions;

public class ItemFirstAidKit extends Item {
	
	public ItemFirstAidKit() {
		setMaxDamage(0);
		maxStackSize = 8;
		setCreativeTab(SevenDaysToMine.TAB_MEDICINE);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);

	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.BOW;
	}
	
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
				entityplayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 1));
				PotionEffect brokeLegEffect = entityplayer.getActivePotionEffect(Potions.brokenLeg);
				if(brokeLegEffect != null) {
					entityplayer.removePotionEffect(Potions.brokenLeg);
					PotionEffect effect = new PotionEffect(Potions.splintedLeg,brokeLegEffect.getDuration(), brokeLegEffect.getAmplifier(), brokeLegEffect.getIsAmbient(), brokeLegEffect.doesShowParticles());
					effect.setCurativeItems(new ArrayList<ItemStack>());
					entityplayer.addPotionEffect(effect);
				}
			}
		}
	}
}
