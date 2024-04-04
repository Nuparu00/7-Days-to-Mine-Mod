package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModItems;

public class ItemAK47 extends ItemGun {

	public ItemAK47() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(80f);
		this.setSpeed(24f);
		this.setRecoil(3f);
		this.setCounterDef(0);
		this.setCross(24);
		this.setReloadTime(1500);
		this.setDelay(6);
		this.setFOVFactor(1.35f);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.43, 0.12, 0);
	}
	
	@Override
	public Item getReloadItem(ItemStack stack) {
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
	
	@Override
	public Vec3d getMuzzleFlashPositionMain() {
		return new Vec3d(0.08, 0.28, -1.8);
	}
	@Override
	public Vec3d getMuzzleFlashPositionSide() {
		return new Vec3d(-0.04, 0.42, -1.8);
	}
	@Override
	public Vec3d getMuzzleFlashAimPosition() {
		return new Vec3d(0.18, 0.25, -1.8);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}

}
