package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class ItemAK47 extends ItemGun {

	public ItemAK47() {
		super("ak47.shot", "pistol.dryshot", "ak47.reload", 30, 256F, 30F, 3F, 0, 22, 1500, 2, EnumGun.RIFLE, EnumLength.LONG, EnumWield.TWO_HAND);
	}
	
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}

}
