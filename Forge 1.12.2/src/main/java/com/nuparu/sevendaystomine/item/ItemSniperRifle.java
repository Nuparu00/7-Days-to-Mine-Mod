package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemSniperRifle extends ItemGun {

	public ItemSniperRifle() {
		super();
		this.setMaxAmmo(12);
		this.setFullDamage(300f);
		this.setSpeed(38f);
		this.setRecoil(4f);
		this.setCounterDef(0);
		this.setCross(15);
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
	public float getFOVFactor(ItemStack stack) {
		return 10f;
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
