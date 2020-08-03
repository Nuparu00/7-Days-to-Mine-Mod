package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.item.Item;

public class ItemCircuit extends Item {

	public ItemCircuit() {
		this.setMaxStackSize(1);
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	}
}
