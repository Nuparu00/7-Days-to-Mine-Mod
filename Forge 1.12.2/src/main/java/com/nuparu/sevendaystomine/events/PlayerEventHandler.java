package com.nuparu.sevendaystomine.events;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IItemHandlerExtended;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.item.ItemBackpack;
import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PlayerEventHandler {

	public Method m_addSlotToContainer = ObfuscationReflectionHelper.findMethod(Container.class, "func_75146_a",
			Slot.class, Slot.class);
	public Field f_allInventories;

	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	 * public void onEvent(EntityViewRenderEvent.FogDensity event) { Entity entity =
	 * event.getEntity(); World world = entity.world; if
	 * (Utils.isInsideBlock(entity, ModBlocks.GASOLINE)) {
	 * GlStateManager.setFog(GlStateManager.FogMode.EXP); event.setDensity(2F); }
	 * else { event.setDensity(-1F); } event.setCanceled(true); }
	 * 
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	 * public void onEvent(EntityViewRenderEvent.FogColors event) { Entity entity =
	 * event.getEntity(); World world = entity.world; if
	 * (Utils.isInsideBlock(entity, ModBlocks.GASOLINE)) { event.setRed(0.5f);
	 * event.setGreen(0.3f); event.setBlue(0.2f); } }
	 */

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {

		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			World world = player.world;
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
			if (!player.isCreative() && !player.isSpectator()) {
				handleExtendedPlayer(player, world, extendedPlayer);
			}
		}
	}

	@SubscribeEvent
	public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event) {

		BlockPos pos = event.getPos().offset(event.getFace());
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
			List<EntityLootableCorpse> list = event.getWorld().getEntitiesWithinAABB(EntityLootableCorpse.class,
					new AxisAlignedBB(pos, pos.add(1, 1, 1)));
			if (list.size() > 0) {
				event.setCanceled(true);
			}
		}

	}

	public void handleExtendedPlayer(EntityPlayer player, World world, IExtendedPlayer extendedPlayer) {
		if (world.rand.nextInt(15) == 0) {
			extendedPlayer.consumeThirst((int) 1);
		}

		if (player.isSprinting()) {
			if (extendedPlayer.getStamina() > 0) {
				int stamina_chance = 1;
				if (world.rand.nextInt(stamina_chance) == 0) {
					extendedPlayer.consumeStamina(2);
				}

				if (world.rand.nextInt(40) == 0) {
					extendedPlayer.consumeThirst((int) 1);
				}
			}

		}

		if (extendedPlayer.getStamina() > extendedPlayer.getMaximumStamina()) {
			extendedPlayer.setStamina(extendedPlayer.getMaximumStamina());
		}
		if (extendedPlayer.getStamina() < 0) {
			extendedPlayer.setStamina(0);
		}

		if (extendedPlayer.getThirst() <= 0) {

			extendedPlayer.setThirst(0);
			if (!world.isRemote) {
				PotionEffect effect = new PotionEffect(Potions.thirst, 4, 4, false, false);
				effect.setCurativeItems(new ArrayList<ItemStack>());
				player.addPotionEffect(effect);
			}
		}
		if (extendedPlayer.getThirst() > extendedPlayer.getMaximumThirst()) {
			extendedPlayer.setThirst(extendedPlayer.getMaximumThirst());
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		final EntityPlayer player = (EntityPlayer) event.getEntity();

		IItemHandler extendedInv = player.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null);
		Container container = player.inventoryContainer;
		

		Slot slotBackpack = new SlotItemHandler(extendedInv, 0, 77, 44) {
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return SevenDaysToMine.MODID + ":items/empty_backpack_slot";
			}
			
			public boolean isItemValid(ItemStack stack)
		    {
		        return !stack.isEmpty() && stack.getItem() instanceof ItemBackpack;
		    }
		};
		Slot slot1 = new SlotItemHandler(extendedInv, 1, 77, 26);
		Slot slot2 = new SlotItemHandler(extendedInv, 2, 77, 8);
		addSlot(slotBackpack, container);
		addSlot(slot1, container);
		addSlot(slot2, container);
		
		

	}

	public void addSlot(Slot slot, Container container) {
		try {
			m_addSlotToContainer.invoke(container, slot);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;
		final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (player.world.isRemote)
			return;
		IItemHandlerExtended extendedInv = CapabilityHelper.getExtendedInventory(player);
		for (int i = 0; i < extendedInv.getSlots(); i++) {
			ItemStack stack = extendedInv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				player.dropItem(stack, false, false);
			}
		}
	}
}