package com.nuparu.sevendaystomine.item;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends ItemQualityScrapable {

	public static final int MAX_VOLTAGE = 10000;

	public ItemBattery(EnumMaterial mat, int weight) {
		super(mat, weight);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setQualityForPlayer(stack, player);
			}
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound nbt = stack.getTagCompound();
			nbt.setInteger("voltage", MAX_VOLTAGE);
			items.add(stack);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		return (nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_INT));
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack The current ItemStack
	 * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged /
	 *         empty bar)
	 */
	public double getDurabilityForDisplay(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_INT)) {
			return 1-((double) nbt.getInteger("voltage") / (double) MAX_VOLTAGE);
		}
		return 0d;
	}
}
