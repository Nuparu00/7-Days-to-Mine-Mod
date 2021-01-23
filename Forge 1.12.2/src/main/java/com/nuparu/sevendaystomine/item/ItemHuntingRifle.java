package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemHuntingRifle extends ItemGun {

	public ItemHuntingRifle() {
		super();
		this.setMaxAmmo(1);
		this.setFullDamage(60f);
		this.setSpeed(24f);
		this.setRecoil(3.2f);
		this.setCounterDef(0);
		this.setCross(16);
		this.setReloadTime(500);
		this.setDelay(2);
		setFOVFactor(1.38f);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.43, 0.1, 0);
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
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

	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.HUNTING_RIFLE_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.HUNTING_RIFLE_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}

}
