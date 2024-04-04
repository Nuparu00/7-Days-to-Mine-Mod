package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;

public class ItemBackpack extends Item {

	public ItemBackpack() {
		this.setMaxStackSize(1);
		this.setCreativeTab(SevenDaysToMine.TAB_CLOTHING);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!playerIn.isSneaking()) {
			playerIn.openGui(SevenDaysToMine.instance, 18, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound compound) {
		return new ExtendedInventoryProvider(9,"backpack");
	}
}
