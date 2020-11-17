package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public class ItemM4 extends ItemGun {

	public ItemM4() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(100f);
		this.setSpeed(28f);
		this.setRecoil(2.5f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(1500);
		this.setDelay(13);
		setFOVFactor(1.37f);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.025, -0.025, -0.4);
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
