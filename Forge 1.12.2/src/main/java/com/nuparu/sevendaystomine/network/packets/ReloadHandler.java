package com.nuparu.sevendaystomine.network.packets;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.nuparu.sevendaystomine.item.ItemGun;

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
	private TimerTask task = new MyTask(this, 1500);

	EntityPlayer player = null;
	World world = null;
	ItemStack stack = null;
	ItemStack stackBullet = null;
	Item bulletItem = null;

	@Override
	public ReloadMessage onMessage(ReloadMessage message, MessageContext ctx) {
		player = ctx.getServerHandler().player;
		world = player.world;
		stack = player.getHeldItemMainhand();

		if (stack == null || stack.isEmpty())
			return null;

		Item item = stack.getItem();
		if (item instanceof ItemGun) {
			ItemGun gun = (ItemGun) item;
			stackBullet = getBullet(player.inventory, stack);
			if (!stackBullet.isEmpty()) {
				bulletItem = gun.getBullet();
				SoundEvent reloadSound = gun.getReloadSound();
				// player.playSound(reloadSound, 1.0F, 1.0F / 1 * 0.4F + 1.2F + 1 * 0.5F);
				world.playSound(null, new BlockPos(player), reloadSound, SoundCategory.PLAYERS, 1F, 1F);
				/*
				 * if (stack.getTagCompound().getBoolean("Reloading") == false) {
				 * stack.getTagCompound().setBoolean("Reloading", true);
				 */
				stack.getTagCompound().setLong("NextFire",
						world.getTotalWorldTime() + (long) Math.ceil((gun.getReloadTime() / 1000d) * 20));
				task = new MyTask(this, gun.getReloadTime());
				timer.schedule(task, gun.getReloadTime(), 1000);
				// }
			}
		}
		return null;
	}

	public void reload() {
		if (stackBullet != null && stack.getTagCompound().hasKey("Capacity") && stack.getTagCompound().hasKey("Ammo")) {

			stack.getTagCompound().setBoolean("Reloading", false);
			int toReload = stack.getTagCompound().getInteger("Capacity") - stack.getTagCompound().getInteger("Ammo");
			if (stackBullet.getCount() > toReload) {
				stackBullet.shrink(
						stack.getTagCompound().getInteger("Capacity") - stack.getTagCompound().getInteger("Ammo"));
				stack.getTagCompound().setInteger("Ammo", stack.getTagCompound().getInteger("Ammo") + toReload);
			} else if (stackBullet.getCount() == toReload) {
				stack.getTagCompound().setInteger("Ammo", stack.getTagCompound().getInteger("Ammo") + toReload);
				player.inventory.clearMatchingItems(stackBullet.getItem(), -1, toReload, null);
			} else {
				stack.getTagCompound().setInteger("Ammo",
						stack.getTagCompound().getInteger("Ammo") + stackBullet.getCount());
				player.inventory.clearMatchingItems(stackBullet.getItem(), -1, toReload, null);
			}
		}
	}

	public ItemStack getBullet(InventoryPlayer inventory, ItemStack gun) {
		ItemStack itemstack = ItemStack.EMPTY;
		if (gun != null && gun.getItem() != null && gun.getItem() instanceof ItemGun) {
			Item bullet = ((ItemGun) gun.getItem()).getBullet();
			for (ItemStack s : inventory.mainInventory) {
				if (s != null && s.getItem() == bullet) {
					itemstack = s;
					break;
				}
			}
		}

		return itemstack;
	}

	private class MyTask extends TimerTask {

		private int MAX_SECONDS = 1500;
		private int seconds = 0;
		private ReloadHandler handler;

		private MyTask(ReloadHandler h, int sec) {
			this.handler = h;
		}

		@Override
		public void run() {

			handler.reload();
			cancel();
		}
	}

}
