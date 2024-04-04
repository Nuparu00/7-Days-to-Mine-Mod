package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModItems;

public class ItemPistol extends ItemGun {

	public ItemPistol() {
		super();
		this.setMaxAmmo(15);
		this.setFullDamage(30F);
		this.setSpeed(10f);
		this.setRecoil(0.61f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(1900);
		this.setDelay(6);
		this.setFOVFactor(1.25f);
		this.setType(EnumGun.PISTOL);
		this.setLength(EnumLength.SHORT);
		this.setWield(EnumWield.DUAL);
		this.setAimPosition(-0.195, 0.1,-0.25);
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
	
	@Override
	public Vec3d getMuzzleFlashPositionMain() {
		return new Vec3d(0.03, 0.4, -0.2);
	}
	@Override
	public Vec3d getMuzzleFlashPositionSide() {
		return new Vec3d(-0.56, 0.4, -0.2);
	}
	@Override
	public Vec3d getMuzzleFlashAimPosition() {
		return new Vec3d(-0.05, 0.4, -0.15);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 0.5;
	}

}
