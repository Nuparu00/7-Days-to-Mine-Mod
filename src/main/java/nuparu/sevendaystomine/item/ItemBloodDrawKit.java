package nuparu.sevendaystomine.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.Utils;

public class ItemBloodDrawKit extends Item {

	public ItemBloodDrawKit() {
		setMaxDamage(16);
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
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur >= this.getMaxItemUseDuration(stack) * 0.05f) {
				ItemStack bloodBag = new ItemStack(ModItems.BLOOD_BAG);

				EntityLivingBase toHurt = entityPlayer;

				RayTraceResult entityRay = Utils.raytraceEntities(entityLiving, 2);
				if (entityRay != null) {
					if (entityRay.entityHit != null && entityRay.entityHit instanceof EntityLivingBase) {
						EntityLivingBase clickedLiving = (EntityLivingBase) entityRay.entityHit;
						if (!(clickedLiving instanceof EntityMob) && !(clickedLiving instanceof EntityPlayer)) {
							toHurt = clickedLiving;
						} else if ((clickedLiving instanceof EntityPlayer)
								&& !((EntityPlayer) clickedLiving).isCreative()
								&& !((EntityPlayer) clickedLiving).isSpectator()) {
							toHurt = clickedLiving;
						}
					}
				}

				if (toHurt instanceof EntityPlayer) {
					if (bloodBag.getTagCompound() == null) {
						bloodBag.setTagCompound(new NBTTagCompound());
					}
					bloodBag.getTagCompound().setString("donor", toHurt.getUniqueID().toString());
				}

				if (!world.isRemote && toHurt != entityPlayer) {
					ModTriggers.MOSCO.trigger((EntityPlayerMP) entityPlayer);
				}

				if (!entityPlayer.inventory.addItemStackToInventory(bloodBag)) {
					entityPlayer.dropItem(bloodBag, false);
				}
				toHurt.attackEntityFrom(DamageSources.bleeding, 4);
				if (entityPlayer instanceof EntityPlayerMP) {
					stack.attemptDamageItem(1, world.rand, (EntityPlayerMP) entityLiving);
					if(stack.getItemDamage() >= this.getMaxDamage()) {
						stack.setCount(0);
					}
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 200;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.BOW;
	}
}
