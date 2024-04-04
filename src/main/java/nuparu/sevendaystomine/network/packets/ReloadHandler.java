package nuparu.sevendaystomine.network.packets;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
import nuparu.sevendaystomine.item.IReloadable;

public class ReloadHandler implements IMessageHandler<ReloadMessage, ReloadMessage> {

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(16);

	EntityPlayer player = null;
	World world = null;
	ItemStack mainStack = null;
	ItemStack secStack = null;

	ItemStack mainBullet = null;
	ItemStack secBullet = null;

	IReloadable reloadableMain = null;
	IReloadable reloadableSec = null;

	@Override
	public ReloadMessage onMessage(ReloadMessage message, MessageContext ctx) {
		player = ctx.getServerHandler().player;
		world = player.world;
		mainStack = player.getHeldItemMainhand();
		secStack = player.getHeldItemOffhand();

		Item main = mainStack.getItem();
		int reloadTime = 0;

		if (main != null && main instanceof IReloadable) {
			reloadableMain = (IReloadable) main;
			mainBullet = getReloadItem(player.inventory, reloadableMain.getReloadItem(mainStack));
			if (!mainBullet.isEmpty()) {
				SoundEvent reloadSound = reloadableMain.getReloadSound();
				world.playSound(null, new BlockPos(player), reloadSound, SoundCategory.PLAYERS, 1F, 1F);
				reloadTime = reloadableMain.getReloadTime(mainStack);
				reloadableMain.onReloadStart(world, player, mainStack, reloadTime);
			}
		}
		Item sec = secStack.getItem();
		if (sec != null && sec instanceof IReloadable) {
			reloadableSec = (IReloadable) sec;
			secBullet = getReloadItem(player.inventory, reloadableSec.getReloadItem(secStack));
			if (!secBullet.isEmpty()) {
				SoundEvent reloadSound = reloadableSec.getReloadSound();
				world.playSound(null, new BlockPos(player), reloadSound, SoundCategory.PLAYERS, 1F, 1F);
				reloadTime = Math.max(reloadTime, reloadableSec.getReloadTime(secStack));
				reloadableSec.onReloadStart(world, player, secStack, reloadTime);

			}
		}

		ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				reload();
			}
		}, reloadTime, TimeUnit.MILLISECONDS);
		return null;
	}

	public void reload() {
		if (reloadableMain != null) {
			reloadableMain.onReloadEnd(world, player, mainStack, mainBullet);
		}
		if (reloadableSec != null) {
			reloadableSec.onReloadEnd(world, player, secStack, secBullet);
		}
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

}
