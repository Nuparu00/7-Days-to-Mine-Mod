package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemMP5 extends ItemGun {

	public ItemMP5() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(40f);
		this.setSpeed(18f);
		this.setRecoil(2.3f);
		this.setCounterDef(0);
		this.setCross(24);
		this.setReloadTime(2000);
		this.setDelay(4);
		setFOVFactor(1.3f);
		this.setType(EnumGun.SUBMACHINE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(0.08, 0, -0.5);
	}
	
	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.TEN_MM_BULLET;
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.AK47_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.MP5_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}
	
	@Override
	public float getShotSoundVolume() {
		return 1F;
	}
	
	@Override
	public float getShotSoundPitch() {
		return 3.0F / (itemRand.nextFloat() * 0.4F + 1.2F) * 0.5F;
	}

}
