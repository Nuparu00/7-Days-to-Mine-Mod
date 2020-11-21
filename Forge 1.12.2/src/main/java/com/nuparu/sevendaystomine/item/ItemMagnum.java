package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemMagnum extends ItemGun {

	public ItemMagnum() {
		super();
		this.setMaxAmmo(6);
		this.setFullDamage(60F);
		this.setSpeed(18f);
		this.setRecoil(2.76f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(2500);
		this.setDelay(15);
		setFOVFactor(1.25f);
		this.setType(EnumGun.PISTOL);
		this.setLength(EnumLength.SHORT);
		this.setWield(EnumWield.DUAL);
		this.setAimPosition(-0.222, 0,-0.25);
	}

	public Item getReloadItem(ItemStack stack) {
		return ModItems.MAGNUM_BULLET;
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.PISTOL_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.PISTOL_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}

}
