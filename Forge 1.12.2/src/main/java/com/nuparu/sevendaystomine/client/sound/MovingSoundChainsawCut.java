package com.nuparu.sevendaystomine.client.sound;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.events.PlayerEventHandler;
import com.nuparu.sevendaystomine.init.ModItems;

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
public class MovingSoundChainsawCut extends MovingSound {
	private final EntityPlayer player;
	private float distance = 0.0F;

	public MovingSoundChainsawCut(EntityPlayer player) {
		super(SoundHelper.CHAINSAW_CUT, SoundCategory.NEUTRAL);
		this.player = player;
		this.repeat = false;
		this.repeatDelay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		ItemStack stack = this.player.getHeldItem(EnumHand.MAIN_HAND);
		NBTTagCompound nbt = stack.getTagCompound();
		if (this.player.isDead || System.currentTimeMillis()-PlayerEventHandler.getLastTimeHittingBlock() > 500 || stack.isEmpty() || (stack.getItem() != ModItems.CHAINSAW && stack.getItem() != ModItems.AUGER)) {
			this.donePlaying = true;
			PlayerEventHandler.nextChainsawCutSound = System.currentTimeMillis();
		}
		if (nbt != null && !nbt.hasKey("FuelMax") || nbt.getInteger("FuelMax") == 0) {
			return;
		}

		else {
			this.xPosF = (float) this.player.posX;
			this.yPosF = (float) this.player.posY;
			this.zPosF = (float) this.player.posZ;
			this.distance = 0.0F;
			this.volume = 1.0F;
		}
	}
}