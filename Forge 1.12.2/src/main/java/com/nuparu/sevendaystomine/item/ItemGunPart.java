package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

public class ItemGunPart extends ItemQuality {

	public ItemGunPart(String name) {
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
		setRegistryName(name);
		setUnlocalizedName(name);
	}
}
