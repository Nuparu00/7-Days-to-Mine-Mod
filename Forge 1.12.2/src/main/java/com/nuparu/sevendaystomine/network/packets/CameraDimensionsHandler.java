package com.nuparu.sevendaystomine.network.packets;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.IReloadable;
import com.nuparu.sevendaystomine.item.ItemAnalogCamera;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CameraDimensionsHandler implements IMessageHandler<CameraDimensionsMessage, CameraDimensionsMessage> {


	@Override
	public CameraDimensionsMessage onMessage(CameraDimensionsMessage message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		ItemStack stack = player.getHeldItemMainhand();
		if(stack.isEmpty()) return null;
		
		if(!(stack.getItem() instanceof ItemAnalogCamera)) return null;
		
		ItemAnalogCamera.setWidth(MathHelper.clamp(message.deltaWidth+ItemAnalogCamera.getWidth(stack, player),0.25,1), stack, player);
		ItemAnalogCamera.setHeight(MathHelper.clamp(message.deltaHeight+ItemAnalogCamera.getHeight(stack, player),0.25,1), stack, player);
		ItemAnalogCamera.setZoom(MathUtils.roundToNDecimal(MathHelper.clamp(message.deltaZoom+ItemAnalogCamera.getZoom(stack, player),1,4),1), stack, player);
		
		return null;
	}
}
