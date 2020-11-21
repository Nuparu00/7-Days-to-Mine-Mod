package com.nuparu.sevendaystomine.network.packets;

import java.util.Timer;
import java.util.TimerTask;

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

public class ReloadHandler implements IMessageHandler<ReloadMessage, ReloadMessage> {

	private Timer timer = new Timer();
	private TimerTask task = new MyTask(this);

	EntityPlayer player = null;
	World world = null;
	ItemStack stack = null;
	ItemStack stackBullet = null;
	Item bulletItem = null;
	IReloadable reloadable = null;

	@Override
	public ReloadMessage onMessage(ReloadMessage message, MessageContext ctx) {
		player = ctx.getServerHandler().player;
		world = player.world;
		stack = player.getHeldItemMainhand();
		if (stack == null || stack.isEmpty())
			return null;

		Item item = stack.getItem();
		if (item instanceof IReloadable) {
			reloadable = (IReloadable) item;
			stackBullet = getReloadItem(player.inventory, reloadable.getReloadItem(stack));
			if (!stackBullet.isEmpty()) {
				bulletItem = reloadable.getReloadItem(stack);
				SoundEvent reloadSound = reloadable.getReloadSound();
				world.playSound(null, new BlockPos(player), reloadSound, SoundCategory.PLAYERS, 1F, 1F);
				int reloadTime = reloadable.getReloadTime(stack);
				reloadable.onReloadStart(world, player, stack, reloadTime);
				task = new MyTask(this);
				timer.schedule(task, reloadTime, 1000);
			}
		}
		return null;
	}

	public void reload() {
		reloadable.onReloadEnd(world, player, stack, stackBullet);
	}

	public ItemStack getReloadItem(InventoryPlayer inventory, Item reloadItem) {
		ItemStack itemstack = ItemStack.EMPTY;
		for (ItemStack s : inventory.mainInventory) {
			if (s != null && s.getItem() == reloadItem) {
				itemstack = s;
				break;
			}
		}

		return itemstack;
	}

	private class MyTask extends TimerTask {

		private ReloadHandler handler;

		private MyTask(ReloadHandler h) {
			this.handler = h;
		}

		@Override
		public void run() {

			handler.reload();
			cancel();
		}
	}

}
