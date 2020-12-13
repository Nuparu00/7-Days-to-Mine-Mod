package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

public class ItemGunPart extends ItemQualityScrapable {

	public ItemGunPart(String name, EnumMaterial mat) {
		super(mat);
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
		setRegistryName(name);
		setUnlocalizedName(name);
	}

	public ItemGunPart(String name, EnumMaterial mat, int weight) {
		super(mat, weight);
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
		setRegistryName(name);
		setUnlocalizedName(name);
	}
}
