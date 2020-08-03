package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class ItemShotgun extends ItemGun {

	public ItemShotgun() {
		super("ak47.shot", "pistol.dryshot", "ak47.reload", 30, 80F, 30F, 3F, 0, 30, 1500, 2, EnumGun.SHOTGUN, EnumLength.LONG, EnumWield.TWO_HAND);
		this.setProjectiles(10);
	}
	
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}

}
