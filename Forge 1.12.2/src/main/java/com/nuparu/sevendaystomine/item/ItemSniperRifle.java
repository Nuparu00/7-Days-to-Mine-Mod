package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSniperRifle extends ItemGun {

	public ItemSniperRifle() {
		super("ak47.shot", "pistol.dryshot", "ak47.reload", 30, 256F, 30F, 3F, 0, 22, 1500, 2, EnumGun.RIFLE, EnumLength.LONG, EnumWield.TWO_HAND);
	}
	
	@Override
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}
	
	@Override
	public float getFOVFactor(ItemStack stack) {
		return 10f;
	}

}
