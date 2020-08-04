package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public class ItemAK47 extends ItemGun {

	public ItemAK47() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(256f);
		this.setSpeed(30f);
		this.setRecoil(3f);
		this.setCounterDef(0);
		this.setCross(22);
		this.setReloadTime(1500);
		this.setDelay(2);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
	}
	
	@Override
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.AK47_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.AK47_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}

}
