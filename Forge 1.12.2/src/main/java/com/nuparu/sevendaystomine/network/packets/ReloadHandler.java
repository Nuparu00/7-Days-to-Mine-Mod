package com.nuparu.sevendaystomine.network.packets;

import java.util.Timer;
import java.util.TimerTask;

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
	ItemGun gun = null;

	@Override
	public ReloadMessage onMessage(ReloadMessage message, MessageContext ctx) {
		player = ctx.getServerHandler().player;
		world = player.world;
		stack = player.getHeldItemMainhand();

		if (stack == null || stack.isEmpty())
			return null;

		Item item = stack.getItem();
		if (item instanceof ItemGun) {
			gun = (ItemGun) item;
			stackBullet = getBullet(player.inventory, stack);
			if (!stackBullet.isEmpty()) {
				bulletItem = gun.getBullet();
				SoundEvent reloadSound = gun.getReloadSound();
				world.playSound(null, new BlockPos(player), reloadSound, SoundCategory.PLAYERS, 1F, 1F);

				int reloadTime = gun.getReloadTime(stack);
				stack.getTagCompound().setLong("NextFire",
						world.getTotalWorldTime() + (long) Math.ceil((reloadTime / 1000d) * 20));
				task = new MyTask(this);
				timer.schedule(task, reloadTime, 1000);
			}
		}
		return null;
	}

	public void reload() {
		if (stackBullet != null && stack.getTagCompound().hasKey("Capacity") && stack.getTagCompound().hasKey("Ammo")) {

			stack.getTagCompound().setBoolean("Reloading", false);
			int toReload = (int) Math.ceil(
					(double) (stack.getTagCompound().getInteger("Capacity") - stack.getTagCompound().getInteger("Ammo"))
							/ (double) gun.getShotsPerAmmo());
			int reload = Math.min(toReload, Utils.getItemCount(player.inventory, bulletItem));

			stack.getTagCompound().setInteger("Ammo", stack.getTagCompound().getInteger("Ammo") + reload * gun.getShotsPerAmmo());
			player.inventory.clearMatchingItems(stackBullet.getItem(), -1, reload, null);
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
