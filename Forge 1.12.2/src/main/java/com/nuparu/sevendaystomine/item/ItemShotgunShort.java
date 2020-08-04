package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public class ItemShotgunShort extends ItemGun {

	public ItemShotgunShort() {
		super();
		this.setMaxAmmo(4);
		this.setProjectiles(10);
		this.setFullDamage(20);
		this.setSpeed(8);
		this.setRecoil(4.2f);
		this.setCounterDef(0);
		this.setCross(35);
		this.setReloadTime(1500);
		this.setDelay(2);
		this.setType(EnumGun.SHOTGUN);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
	}
	
	public Item getBullet() {
		return ModItems.SEVEN_MM_BULLET;
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.AK47_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.SHOTGUN_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}
}
