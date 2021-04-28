package com.nuparu.sevendaystomine.client.sound;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.entity.EntityMinibike;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovingSoundCarIdle extends MovingSound {
	private final EntityCar car;
	private float distance = 0.0F;

	public MovingSoundCarIdle(EntityCar car) {
		super(SoundHelper.MINIBIKE_IDLE, SoundCategory.NEUTRAL);
		this.car = car;
		this.repeat = false;
		this.repeatDelay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {

		if (car.getControllingPassenger() == null || !car.canBeDriven()) {
			this.donePlaying = true;
			return;
		}
		this.pitch = (float)(1+MathUtils.getSpeedKilometersPerHour(car)/100d);

		this.xPosF = (float) this.car.posX;
		this.yPosF = (float) this.car.posY;
		this.zPosF = (float) this.car.posZ;
		this.distance = 0.0F;
		this.volume = 2F;

	}
}