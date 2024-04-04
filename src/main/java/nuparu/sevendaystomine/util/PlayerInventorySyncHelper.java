package nuparu.sevendaystomine.util;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncInventoryMessage;
import nuparu.sevendaystomine.util.item.InventoryCache;
import nuparu.sevendaystomine.util.item.ItemCache;

public class PlayerInventorySyncHelper {

	public static HashMap<String, ItemCache> itemsCache = new HashMap<String, ItemCache>();

	// Map of players and their inventories
	public static HashMap<String, InventoryCache> inventoryCache = new HashMap<String, InventoryCache>();

	@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
	public static class InventoryDetectionHandler {
		@SubscribeEvent
		public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
			if (!event.getEntityLiving().world.isRemote) {
				if (ModConfig.players.renderPlayerInventory == true) {
					if (event.getEntityLiving() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) event.getEntityLiving();
						ItemStack[] inventory = new ItemStack[player.inventory.mainInventory.size()];
						inventory = player.inventory.mainInventory.toArray(inventory);
						ItemStack currentItem = player.inventory.getCurrentItem();
						ItemStack backpack = ItemStack.EMPTY;
						ExtendedInventory inv = (ExtendedInventory) CapabilityHelper.getExtendedInventory(player);

						if (inv != null) {
							backpack = inv.getStackInSlot(0);
						}

						if (inventoryCache.containsKey(player.getName())) {
							InventoryCache cache = inventoryCache.get(player.getName());
							if (areInventoriesEqual(inventory, cache.inventory)
									&& ItemStack.areItemStacksEqual(currentItem, cache.currentItem)
									&& ItemStack.areItemStacksEqual(backpack, cache.backpack)
									&& player.inventory.currentItem == cache.index) {
								return;
							}

							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), currentItem, player.inventory.currentItem);
							items.backpack = backpack;

							EntityTracker tracker = ((WorldServer) player.world).getEntityTracker();
							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName());
							for (EntityPlayer entityPlayer : tracker.getTrackingPlayers(player)) {
								PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) entityPlayer);
							}
							PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) player);
							inventoryCache.put(player.getName(), new InventoryCache(inventory.clone(), currentItem,
									backpack, player.inventory.currentItem));

						} else {
							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), currentItem, player.inventory.currentItem);
							items.backpack = backpack;

							EntityTracker tracker = ((WorldServer) player.world).getEntityTracker();
							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName());
							for (EntityPlayer entityPlayer : tracker.getTrackingPlayers(player)) {
								PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) entityPlayer);
							}
							PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) player);

							inventoryCache.put(player.getName(), new InventoryCache(inventory.clone(), currentItem,
									backpack, player.inventory.currentItem));
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void onEntityJoin(EntityJoinWorldEvent event) {
			if (!event.getEntity().world.isRemote) {
				if (ModConfig.players.renderPlayerInventory == true) {
					if (event.getEntity() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) event.getEntity();

						ItemStack[] inventory = new ItemStack[player.inventory.mainInventory.size()];
						inventory = player.inventory.mainInventory.toArray(inventory);
						ItemStack currentItem = player.inventory.getCurrentItem();
						ItemStack backpack = ItemStack.EMPTY;
						ExtendedInventory inv = (ExtendedInventory) CapabilityHelper.getExtendedInventory(player);

						if (inv != null) {
							backpack = inv.getStackInSlot(0);
						}

						if (inventoryCache.containsKey(player.getName())) {

							InventoryCache cache = inventoryCache.get(player.getName());

							if (areInventoriesEqual(inventory, cache.inventory)
									&& ItemStack.areItemStacksEqual(currentItem, cache.currentItem)
									&& ItemStack.areItemStacksEqual(backpack, cache.backpack)
									&& player.inventory.currentItem == cache.index) {
								return;
							}

							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), currentItem, player.inventory.currentItem);
							items.backpack = backpack;

							EntityTracker tracker = ((WorldServer) player.world).getEntityTracker();
							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName());
							for (EntityPlayer entityPlayer : tracker.getTrackingPlayers(player)) {
								PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) entityPlayer);
							}
							PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) player);
							inventoryCache.put(player.getName(), new InventoryCache(inventory.clone(), currentItem,
									backpack, player.inventory.currentItem));

						} else {

							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), currentItem, player.inventory.currentItem);
							items.backpack = backpack;
							EntityTracker tracker = ((WorldServer) player.world).getEntityTracker();
							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName());
							for (EntityPlayer entityPlayer : tracker.getTrackingPlayers(player)) {
								PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) entityPlayer);
							}
							PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) player);

							inventoryCache.put(player.getName(), new InventoryCache(inventory.clone(), currentItem,
									backpack, player.inventory.currentItem));
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
			if (!event.getEntity().world.isRemote) {
				if (ModConfig.players.renderPlayerInventory == true) {
					if (event.getEntity() instanceof EntityPlayer && event.getTarget() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) event.getTarget();
						EntityPlayer trackingPlayer = (EntityPlayer) event.getEntity();
						ItemStack[] inventory = new ItemStack[player.inventory.mainInventory.size()];
						inventory = player.inventory.mainInventory.toArray(inventory);
						ItemStack currentItem = player.inventory.getCurrentItem();
						ItemCache items = new ItemCache();
						ExtendedInventory inv = (ExtendedInventory) CapabilityHelper.getExtendedInventory(player);

						items.selectCorrectItems(inventory.clone(), currentItem, player.inventory.currentItem);
						if (inv != null) {
							items.backpack = inv.getStackInSlot(0);
						}
						SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName());
						PacketManager.syncInventory.sendTo(message, (EntityPlayerMP) trackingPlayer);

					}
				}
			}
		}

		public static boolean areInventoriesEqual(ItemStack[] a, ItemStack[] b) {
			if (a == null || b == null) {
				return false;
			}
			for (int i = 0; i < a.length; i++) {
				if (a[i] == null && b[i] != null) {
					return false;
				}
				if (a[i] != null && b[i] == null) {
					return false;
				}
				if (a[i] == null && b[i] == null) {
					return true;
				}
				if (!ItemStack.areItemStacksEqual(a[i], b[i])) {
					return false;
				}
			}
			return true;
		}

		protected float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
			return (float) livingBase.ticksExisted + partialTicks;
		}

		protected float interpolateRotation(float par1, float par2, float par3) {
			float f;

			for (f = par2 - par1; f < -180.0F; f += 360.0F) {
				;
			}

			while (f >= 180.0F) {
				f -= 360.0F;
			}

			return par1 + par3 * f;
		}
	}
}
