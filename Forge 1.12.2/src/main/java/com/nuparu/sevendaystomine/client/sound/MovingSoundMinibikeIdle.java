package com.nuparu.sevendaystomine.client.sound;

import com.nuparu.sevendaystomine.SevenDaysToMine;
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
public class MovingSoundMinibikeIdle extends MovingSound {
	private final EntityMinibike minibike;
	private float distance = 0.0F;

	public MovingSoundMinibikeIdle(EntityMinibike minibike) {
		super(SoundHelper.MINIBIKE_IDLE, SoundCategory.NEUTRAL);
		this.minibike = minibike;
		this.repeat = false;
		this.repeatDelay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {

		if (minibike.getControllingPassenger() == null || !minibike.canBeDriven()) {
			this.donePlaying = true;
			return;
		}
		this.pitch = (float)(1+MathUtils.getSpeedKilometersPerHour(minibike)/100d);
		this.xPosF = (float) this.minibike.posX;
		this.yPosF = (float) this.minibike.posY;
		this.zPosF = (float) this.minibike.posZ;
		this.distance = 0.0F;
		this.volume = 2.0F;

	}
}