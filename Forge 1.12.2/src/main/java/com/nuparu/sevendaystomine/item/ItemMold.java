package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.item.Item;

public class ItemMold extends Item{

	public ItemMold() {
		setMaxStackSize(1);
		this.setMaxDamage(64);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
	}
}
