package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class ItemHuntingRifle extends ItemGun {

	public ItemHuntingRifle() {
		super("hunting_rifle.shot", "pistol.dryshot", "hunting_rifle.reload", 1, 120F, 32F, 3.2F, 0, 16, 500, 2, EnumGun.RIFLE,
				EnumLength.LONG, EnumWield.TWO_HAND);
	}

	@Override
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}
	
	@Override
	public float getShotSoundVolume() {
		return 1F;
	}
	
	@Override
	public float getShotSoundPitch() {
		return 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F;
	}

}
