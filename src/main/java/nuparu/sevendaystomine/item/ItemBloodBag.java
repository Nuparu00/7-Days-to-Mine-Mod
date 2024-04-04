package nuparu.sevendaystomine.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;

public class ItemBloodBag extends Item {
	
	public ItemBloodBag() {
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
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur >= this.getMaxItemUseDuration(stack) * 0.05f) {
				if (!entityplayer.capabilities.isCreativeMode) {
					NBTTagCompound nbt = stack.getTagCompound();
					if(nbt != null && nbt.hasKey("donor", Constants.NBT.TAG_STRING)) {
						String uuid = nbt.getString("donor");
						if(!worldIn.isRemote && !uuid.equals(entityplayer.getUniqueID().toString())) {
							ModTriggers.BLOOD_BOND.trigger((EntityPlayerMP) entityplayer);
						}
					}
					stack.shrink(1);
					if (stack.isEmpty()) {
						entityplayer.inventory.deleteStack(stack);
					}
				}
				entityplayer.heal(4);
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
