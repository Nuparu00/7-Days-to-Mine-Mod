package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlueprint extends ItemRecipeBook {

	public ItemBlueprint(ResourceLocation data, String recipe) {
		super(data, recipe);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(SevenDaysToMine.proxy.localize(this.getRegistryName().getResourcePath() + ".title"));
	}
}
