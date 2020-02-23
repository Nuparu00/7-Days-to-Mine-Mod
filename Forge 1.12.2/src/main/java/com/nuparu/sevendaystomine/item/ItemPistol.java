package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class ItemPistol extends ItemGun {

	public ItemPistol() {
		super("pistol.shot", "pistol.dryshot", "pistol.reload", 15, 48.15162342F, 14F, 0.61F, 0, 20, 1900, 10, EnumGun.PISTOL, EnumLength.SHORT, EnumWield.DUAL);
	}

	public Item getBullet() {
		return ModItems.NINE_MM_BULLET;
	}

}
