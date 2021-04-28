package com.nuparu.sevendaystomine.network.packets;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.item.IReloadable;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HonkHandler implements IMessageHandler<HonkMessage, HonkMessage> {

	@Override
	public HonkMessage onMessage(HonkMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		Entity riding = player.getRidingEntity();
		if(riding != null && riding instanceof EntityCar) {
			if(riding.getPassengers().indexOf(player) == 0) {
				System.out.println("Beep beep beep");
				player.world.playSound(null,player.posX, player.posY, player.posZ, SoundHelper.HONK, SoundCategory.PLAYERS, player.world.rand.nextFloat()*0.2f+1.4f, player.world.rand.nextFloat()*0.05f+0.8f);
			}
		}

		return null;
	}

	

}
