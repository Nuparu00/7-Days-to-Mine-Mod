package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemQuality extends Item implements IQuality {

	public static final int MAX_QUALITY = 600;
	public static final double XP_PER_QUALITY_POINT = 2;

	public ItemQuality() {

	}

	@Override
	public int getQuality(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("Quality")) {
				return nbt.getInteger("Quality");
			}
		}
		return 0;
	}

	@Override
	public EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(getQuality(stack));
	}

	@Override
	public EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}

	@Override
	public void setQuality(ItemStack stack, int quality) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality", quality);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack itemstack) {
		EnumQuality quality = getQualityTierFromStack(itemstack);
		return quality.color + SevenDaysToMine.proxy.localize(this.getUnlocalizedName() + ".name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		int quality = getQuality(stack);
		EnumQuality tier = getQualityTierFromInt(quality);
		tooltip.add(tier.color + SevenDaysToMine.proxy.localize("stat.quality." + tier.name().toLowerCase() + ".name"));
		tooltip.add(tier.color + SevenDaysToMine.proxy.localize("stat.quality.name") + quality);
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
			items.add(stack);
		}
	}

	public static ItemStack setQualityForPlayer(ItemStack stack, EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality",
				(int) Math.min(Math.max(Math.floor(player.getScore() / XP_PER_QUALITY_POINT), 1), MAX_QUALITY));

		return stack;
	}

	public static ItemStack setQualityForStack(ItemStack stack, int quality) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality", quality);

		return stack;
	}

	public static boolean hasQualityTag(ItemStack stack) {
		return stack.getTagCompound() == null ? false : stack.getTagCompound().hasKey("Quality", Constants.NBT.TAG_INT);
	}

}
