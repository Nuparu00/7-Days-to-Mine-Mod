package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCircuit extends Item {

	public ItemCircuit() {
		this.setMaxStackSize(1);
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String data = "<null>";
		if(stack.getTagCompound() != null &&  stack.getTagCompound().hasKey("data")) {
			data = stack.getTagCompound().getString("data");
		}
		tooltip.add("Data: " + data);
	}
}
