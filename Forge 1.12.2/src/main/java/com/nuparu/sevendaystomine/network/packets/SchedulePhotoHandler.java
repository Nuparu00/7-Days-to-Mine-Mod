package com.nuparu.sevendaystomine.network.packets;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.item.IReloadable;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SchedulePhotoHandler implements IMessageHandler<SchedulePhotoMessage, SchedulePhotoMessage> {

	@Override
	public SchedulePhotoMessage onMessage(SchedulePhotoMessage message, MessageContext ctx) {
		SevenDaysToMine.proxy.schedulePhoto();
		return null;
	}

}
