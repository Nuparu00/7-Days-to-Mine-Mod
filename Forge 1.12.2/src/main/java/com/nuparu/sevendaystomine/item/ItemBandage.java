package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBandage extends Item implements IScrapable {
	private EnumMaterial material = EnumMaterial.CLOTH;
	private int weight = 1;

	public ItemBandage() {
		setMaxDamage(0);
		maxStackSize = 16;
		setCreativeTab(SevenDaysToMine.TAB_MEDICINE);

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);

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
			}
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
		if(target.getActivePotionEffect(Potions.bleeding) != null) {
			target.removePotionEffect(Potions.bleeding);
			stack.shrink(1);
		}
        return false;
    }

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.BOW;
	}

	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	public EnumMaterial getMaterial() {
		return material;
	}

	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	public int getWeight() {
		return weight;
	}

	public boolean canBeScraped() {
		return true;
	}
}
