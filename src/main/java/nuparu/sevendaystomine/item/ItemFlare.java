package nuparu.sevendaystomine.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.entity.EntityFlare;

public class ItemFlare extends Item {
	
	public ItemFlare(){
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		int i = this.getMaxItemUseDuration(stack) - timeLeft;

		if (i < 0)
			return;

		float f = getVelocity(i);
		if (f < 0.1f)
			return;
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}

			worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
					SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!worldIn.isRemote) {
				ModTriggers.FLARE.trigger((EntityPlayerMP) player);
				EntityFlare flare = new EntityFlare(worldIn, player);
				flare.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.5f * f, 0.5f);
				worldIn.spawnEntity(flare);
			}
			player.addStat(StatList.getObjectUseStats(this));
		}
	}

	public static float getVelocity(int charge) {
		float f = (float) charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;

		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}
}
