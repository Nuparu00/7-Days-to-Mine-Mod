package nuparu.sevendaystomine.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.world.item.IReloadableItem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ReloadMessage {

	public ReloadMessage() {

	}

	public static void encode(ReloadMessage msg, FriendlyByteBuf buf) {
	}

	public static ReloadMessage decode(FriendlyByteBuf buf) {
		return new ReloadMessage();
	}

	public static class Handler {

		private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(16);

		public static void handle(ReloadMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				final ServerPlayer player = ctx.get().getSender();
				final Level world = player.level();
				final ItemStack mainStack = player.getMainHandItem();
				final ItemStack secStack = player.getOffhandItem();

				ItemStack mainBullet = null;
				ItemStack secBullet = null;

				IReloadableItem reloadableMain = null;
				IReloadableItem reloadableSec = null;

				Item main = mainStack.getItem();
				int reloadTime = 0;

				if (main instanceof IReloadableItem) {
					reloadableMain = (IReloadableItem) main;
					mainBullet = getReloadItem(player.getInventory(), reloadableMain.getReloadItem(mainStack));
					if (!mainBullet.isEmpty()) {
						SoundEvent reloadSound = reloadableMain.getReloadSound();
						world.playSound(null, player.blockPosition(), reloadSound, SoundSource.PLAYERS, 1F, 1F);
						reloadTime = reloadableMain.getReloadTime(mainStack);
						reloadableMain.onReloadStart(world, player, mainStack, reloadTime);
					}
				}
				Item sec = secStack.getItem();
				if (sec instanceof IReloadableItem) {
					reloadableSec = (IReloadableItem) sec;
					secBullet = getReloadItem(player.getInventory(), reloadableSec.getReloadItem(secStack));
					if (!secBullet.isEmpty()) {
						SoundEvent reloadSound = reloadableSec.getReloadSound();
						world.playSound(null, player.blockPosition(), reloadSound, SoundSource.PLAYERS, 1F, 1F);
						reloadTime = Math.max(reloadTime, reloadableSec.getReloadTime(secStack));
						reloadableSec.onReloadStart(world, player, secStack, reloadTime);

					}
				}

				final ItemStack mainBulletFinal = mainBullet;
				final ItemStack secBulletFinal = secBullet;
				final IReloadableItem reloadableMainFinal = reloadableMain;
				final IReloadableItem reloadableSecFinal = reloadableSec;

				ScheduledFuture<?> countdown = scheduler.schedule(() -> reload(reloadableMainFinal, reloadableSecFinal, world, player, mainStack, mainBulletFinal,
						secStack, secBulletFinal), reloadTime, TimeUnit.MILLISECONDS);
			});
		}

		public static ItemStack getReloadItem(Inventory inventory, Item reloadItem) {
			ItemStack itemstack = ItemStack.EMPTY;
			for (ItemStack s : inventory.items) {
				if (s != null && s.getItem() == reloadItem) {
					itemstack = s;
					break;
				}
			}

			return itemstack;
		}

		public static void reload(IReloadableItem reloadableMain, IReloadableItem reloadableSec, Level world,
				ServerPlayer player, ItemStack mainStack, ItemStack mainBullet, ItemStack secStack,
				ItemStack secBullet) {
			if (reloadableMain != null) {
				reloadableMain.onReloadEnd(world, player, mainStack, mainBullet);
			}
			if (reloadableSec != null) {
				reloadableSec.onReloadEnd(world, player, secStack, secBullet);
			}
		}
	}
}
