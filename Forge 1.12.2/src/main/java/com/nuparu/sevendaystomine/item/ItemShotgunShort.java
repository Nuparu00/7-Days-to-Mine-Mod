package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class ItemShotgunShort extends ItemGun {

	public ItemShotgunShort() {
		super("ak47.shot", "pistol.dryshot", "ak47.reload", 30, 80F, 30F, 3F, 0, 35, 1500, 2, EnumGun.SHOTGUN, EnumLength.LONG, EnumWield.TWO_HAND);
		this.setProjectiles(10);
	}
	
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}

}
