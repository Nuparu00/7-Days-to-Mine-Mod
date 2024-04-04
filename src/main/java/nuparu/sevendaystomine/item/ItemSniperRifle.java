package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModItems;

public class ItemSniperRifle extends ItemGun {

	public ItemSniperRifle() {
		super();
		this.setMaxAmmo(12);
		this.setFullDamage(100f);
		this.setSpeed(25f);
		this.setRecoil(4f);
		this.setCounterDef(0);
		this.setCross(15);
		this.setReloadTime(1500);
		this.setDelay(20);
		this.setFOVFactor(10f);
		this.setScoped(true);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
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
	@Nullable
	public Vec3d getMuzzleFlashAimPosition() {
		return null;
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}
}
