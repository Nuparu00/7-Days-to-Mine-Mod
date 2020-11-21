package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemPistol extends ItemGun {

	public ItemPistol() {
		super();
		this.setMaxAmmo(15);
		this.setFullDamage(35F);
		this.setSpeed(14f);
		this.setRecoil(0.61f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(1900);
		this.setDelay(6);
		this.setFOVFactor(1.25f);
		this.setType(EnumGun.PISTOL);
		this.setLength(EnumLength.SHORT);
		this.setWield(EnumWield.DUAL);
		this.setAimPosition(-0.2, 0.1,-0.25);
	}

	public Item getReloadItem(ItemStack stack) {
		return ModItems.NINE_MM_BULLET;
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
