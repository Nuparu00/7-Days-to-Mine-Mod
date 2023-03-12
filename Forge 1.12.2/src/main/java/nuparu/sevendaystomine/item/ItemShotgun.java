package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModItems;

public class ItemShotgun extends ItemGun {

	public ItemShotgun() {
		super();
		this.setMaxAmmo(4);
		this.setProjectiles(10);
		this.setFullDamage(12);
		this.setSpeed(1);
		this.setRecoil(4);
		this.setCounterDef(0);
		this.setCross(30);
		this.setReloadTime(1500);
		this.setDelay(10);
		setFOVFactor(1.2f);
		this.setType(EnumGun.SHOTGUN);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.41, 0.1, 0);
	}
	
	public Item getReloadItem(ItemStack stack) {
		return ModItems.SHOTGUN_SHELL;
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

	@Override
	public Vec3d getMuzzleFlashPositionMain() {
		return new Vec3d(-0.05, 0.35, -1.8);
	}
	@Override
	public Vec3d getMuzzleFlashPositionSide() {
		return new Vec3d(-0.05, 0.35, -1.8);
	}
	@Override
	public Vec3d getMuzzleFlashAimPosition() {
		return new Vec3d(0.15, 0.35, -1.4);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}
	
}
