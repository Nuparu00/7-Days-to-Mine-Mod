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
import com.nuparu.sevendaystomine.capability.ExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IItemHandlerExtended;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.IQuality;
import com.nuparu.sevendaystomine.item.ItemBackpack;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.horde.BloodmoonHorde;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
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

			public boolean isItemValid(ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof ItemBackpack;
			}

			@SideOnly(Side.CLIENT)
			public boolean isEnabled() {
				return (!player.isCreative() && !player.isSpectator());
			}
		};
		Slot slot1 = new SlotItemHandler(extendedInv, 1, 77, 26);
		Slot slot2 = new SlotItemHandler(extendedInv, 2, 77, 8);
		addSlot(slotBackpack, container);

		// addSlot(slot1, container);
		// addSlot(slot2, container);

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

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.isRemote || event.getHand() == EnumHand.OFF_HAND)
			return;

		ItemStack stack = player.getHeldItemMainhand();

		if (event.getResult() != Result.DENY && stack.isEmpty() && !player.isSneaking() && !player.isCreative()
				&& !player.isSpectator()) {
			handleDrinkingWater(player);
		}
	}

	public void handleDrinkingWater(EntityPlayer player) {
		IExtendedPlayer ep = CapabilityHelper.getExtendedPlayer(player);

		World world = player.world;
		RayTraceResult ray = getMovingObjectPositionFromPlayer(world, player, true);

		if (ray != null)

			if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = ray.getBlockPos();
				if (world.getBlockState(blockpos).getMaterial() == Material.WATER) {

					ep.addThirst(35);
					ep.addStamina(20);
					player.removePotionEffect(Potions.thirst);
					world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_DRINK,
							SoundCategory.BLOCKS, 0.2F, world.rand.nextFloat() * 0.1F + 0.9F, false);
					if (world.rand.nextInt(10) == 0) {
						PotionEffect effect = new PotionEffect(Potions.dysentery, world.rand.nextInt(4000) + 18000, 4,
								false, false);
						effect.setCurativeItems(new ArrayList<ItemStack>());
						player.addPotionEffect(effect);
					}
				}
			}

	}

	public RayTraceResult getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.posX;
		double d1 = playerIn.posY + (double) playerIn.getEyeHeight();
		double d2 = playerIn.posZ;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 5.0D;
		if (playerIn instanceof net.minecraft.entity.player.EntityPlayerMP) {
			d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		}
		Vec3d vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
	}

	@SubscribeEvent
	public void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
		float speed = event.getOriginalSpeed() / 32f;
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if (!stack.isEmpty() && stack.getItem() instanceof IQuality) {
			speed = speed * (1 + (float) ((IQuality) stack.getItem()).getQuality(stack) / 128f);
		}
		event.setNewSpeed(speed);

	}

	@SubscribeEvent
	public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
		World world = event.getEntityPlayer().world;
		if (Utils.isBloodmoon(world) && (event.getResultStatus() == SleepResult.OK || event.getResultStatus() == null)
				&& !event.getEntityPlayer().isCreative()) {
			event.setResult(SleepResult.OTHER_PROBLEM);
		}
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		final EntityPlayer player = event.player;
		final ItemStack stack = new ItemStack(ModItems.SURVIVAL_GUIDE);
		final NBTTagCompound entityData = player.getEntityData();
		final NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedData);

		final String key = "7d2m_guide";

		if (!persistedData.getBoolean(key)) {
			persistedData.setBoolean(key, true);

			if (!player.inventory.addItemStackToInventory(stack)) {
				player.dropItem(stack, false);
			}
		}
	}

}