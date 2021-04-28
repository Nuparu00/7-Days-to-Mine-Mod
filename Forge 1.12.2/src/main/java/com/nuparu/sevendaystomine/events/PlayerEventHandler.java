package com.nuparu.sevendaystomine.events;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockFlowerPotEnhanced;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import com.nuparu.sevendaystomine.capability.ExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IItemHandlerExtended;
import com.nuparu.sevendaystomine.client.sound.MovingSoundChainsawCut;
import com.nuparu.sevendaystomine.client.sound.MovingSoundChainsawIdle;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.inventory.ContainerPlayerExtended;
import com.nuparu.sevendaystomine.inventory.InventoryPlayerExtended;
import com.nuparu.sevendaystomine.item.IQuality;
import com.nuparu.sevendaystomine.item.IScrapable;
import com.nuparu.sevendaystomine.item.ItemBackpack;
import com.nuparu.sevendaystomine.item.ItemFuelTool;
import com.nuparu.sevendaystomine.item.ItemQuality;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.horde.BloodmoonHorde;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PlayerEventHandler {

	public Method m_addSlotToContainer = ObfuscationReflectionHelper.findMethod(Container.class, "func_75146_a",
			Slot.class, Slot.class);
	public Field f_allInventories;

	protected static long nextChainsawIdleSound = 0l;
	public static long nextChainsawCutSound = 0l;
	protected static long lastTimeHittingBlock = 0l;

	@SubscribeEvent
	public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos().offset(event.getFace());
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		Item item = stack.getItem();
		IBlockState state = world.getBlockState(event.getPos());
		Block block = state.getBlock();
		if (event.getHand() == EnumHand.MAIN_HAND) {
			if (block == Blocks.FLOWER_POT) {
				if (state.getValue(BlockFlowerPot.CONTENTS) == BlockFlowerPot.EnumFlowerType.EMPTY) {
					if (((BlockFlowerPotEnhanced) ModBlocks.FLOWER_POT_ENHANCED).canBePotted(stack)) {
						world.setBlockState(event.getPos(), ModBlocks.FLOWER_POT_ENHANCED.getDefaultState());
						TileEntity tile = world.getTileEntity(event.getPos());
						if (tile != null) {
							TileEntityFlowerPot te = (TileEntityFlowerPot) tile;
							te.setItemStack(stack);
							player.addStat(StatList.FLOWER_POTTED);
							if (!player.capabilities.isCreativeMode) {
								stack.shrink(1);
							}
							te.markDirty();
							world.notifyBlockUpdate(pos, state, state, 3);
						}
						world.markBlockRangeForRenderUpdate(pos, pos);
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
						world.scheduleBlockUpdate(pos, ModBlocks.FLOWER_POT_ENHANCED, 0, 0);
						event.setCanceled(true);
					}
				}
			} else if (block == ModBlocks.FLOWER_POT_ENHANCED) {
				TileEntity tile = world.getTileEntity(event.getPos());
				if (tile != null && !world.isRemote) {
					TileEntityFlowerPot te = (TileEntityFlowerPot) tile;
					ItemStack itemstack1 = te.getFlowerItemStack();
					if (stack.isEmpty()) {
						player.setHeldItem(event.getHand(), itemstack1);
					} else if (!player.addItemStackToInventory(itemstack1)) {
						player.dropItem(itemstack1, false);
					}

					world.setBlockState(event.getPos(), Blocks.FLOWER_POT.getDefaultState());
				}
				world.markBlockRangeForRenderUpdate(pos, pos);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
				world.scheduleBlockUpdate(pos, Blocks.FLOWER_POT, 0, 0);
				event.setCanceled(true);
			}
		}

		if (item instanceof ItemBlock) {
			if (!stack.isEmpty()) {
				List<EntityLootableCorpse> list = world.getEntitiesWithinAABB(EntityLootableCorpse.class,
						new AxisAlignedBB(pos, pos.add(1, 1, 1)));
				if (list.size() > 0) {
					event.setCanceled(true);
				}
			}
		}

	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		if(!ModConfig.players.backpackSlot)return;
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
		/*
		 * Slot slot1 = new SlotItemHandler(extendedInv, 1, 77, 26); Slot slot2 = new
		 * SlotItemHandler(extendedInv, 2, 77, 8);
		 */
		addSlot(slotBackpack, container);

		/*
		 * addSlot(slot1, container); addSlot(slot2, container);
		 */

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
		IItemHandlerExtended extendedInv = CapabilityHelper.getExtendedInventory(player);
		if (extendedInv == null)
			return;
		for (int i = 0; i < extendedInv.getSlots(); i++) {
			ItemStack stack = extendedInv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				player.dropItem(stack, false, false);
			}
		} /*
			 * player.world.getScoreboard().addPlayerToTeam(player.getName(), "death");
			 * player.world.getScoreboard().addScoreObjective("death",
			 * IScoreCriteria.DUMMY);
			 * player.world.getScoreboard().setObjectiveInDisplaySlot(0,
			 * player.world.getScoreboard().getObjective("death"));
			 */
		// player.world.getScoreboard().getOrCreateScore(player.getName(),
		// player.world.getScoreboard().getObjective("death")).setScorePoints(player.experienceTotal);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		if (event.getHand() == EnumHand.OFF_HAND)
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
				boolean flag = false;
				BlockPos blockpos = ray.getBlockPos();
				IBlockState state = world.getBlockState(blockpos);
				if (state.getMaterial() == Material.WATER) {
					// System.out.println("DDD");
					flag = true;
				} else if (state.getBlock() instanceof BlockCauldron) {
					int level = state.getValue(BlockCauldron.LEVEL);
					if (level > 0) {
						flag = true;/*
									 * if (!world.isRemote) { world.setBlockState(blockpos,
									 * state.withProperty(BlockCauldron.LEVEL, level - 1)); }
									 */
					}
				}
				if (flag) {
					world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_DRINK,
							SoundCategory.BLOCKS, 0.2F, world.rand.nextFloat() * 0.1F + 0.9F, false);
					if (!world.isRemote) {
						/*
						 * ep.addThirst(35); ep.addStamina(20);
						 * player.removePotionEffect(Potions.thirst);
						 * 
						 * if (world.rand.nextInt(10) == 0) { PotionEffect effect = new
						 * PotionEffect(Potions.dysentery, world.rand.nextInt(4000) + 18000, 4, false,
						 * false); effect.setCurativeItems(new ArrayList<ItemStack>());
						 * player.addPotionEffect(effect); }
						 */
						ep.setDrinkCounter(ep.getDrinkCounter() + 10);
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
		float speed = event.getOriginalSpeed()
				/ (ModConfig.players.immersiveBlockBreaking && event.getState().getMaterial() != Material.CIRCUITS ? 32f
						: 1f);
		if (ModConfig.players.qualitySystem) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			Item item = stack.getItem();
			if (!stack.isEmpty() && item instanceof IQuality) {
				speed = speed * (1 + (float) ((IQuality) item).getQuality(stack) / 128f);
			}
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
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (!ModConfig.players.survivalGuide)
			return;
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

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		if (ModConfig.players.qualitySystem) {
			if (left.getItem() instanceof IQuality && right.getItem() == left.getItem()) {
				ItemStack stack = left.copy();
				int l = ItemQuality.getQualityForStack(left);
				int r = ItemQuality.getQualityForStack(right);
				ItemQuality.setQualityForStack(stack, Math.max(l, r) + 6);
				l = left.getItemDamage();
				r = right.getItemDamage();
				int m = r < l ? right.getMaxDamage() : left.getMaxDamage();
				stack.setItemDamage(Math.max(l, r) - (m - Math.min(l, r)));
				event.setOutput(stack);
				event.setCost(1);
				event.setMaterialCost(1);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {

		EntityPlayer original = event.getOriginal();
		EntityPlayer clone = event.getEntityPlayer();

		if (original.inventory instanceof InventoryPlayerExtended
				&& clone.inventory instanceof InventoryPlayerExtended) {
			((InventoryPlayerExtended) original.inventory).copy((InventoryPlayerExtended) clone.inventory);
		}
	}

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		if (event.player.world.isRemote)
			return;
		ItemStack stack = event.crafting;
		if (ModConfig.players.qualitySystem) {
			if (stack.getItem() instanceof IQuality) {
				if (!event.player.isCreative()) {
					Utils.consumeXp(event.player, MathHelper.floor(
							event.player.experienceTotal * (event.player.world.rand.nextDouble() * 0.04 + 0.01)));
				}
			}
		}
	}

	@SubscribeEvent
	public void onEatenEvent(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack stack = event.getItem();
			if (stack.getItem() instanceof ItemFood && stack.getItem().getMaxDamage() > 0
					&& (stack.getMaxDamage() - stack.getItemDamage()) > 1) {
				stack.damageItem(1, player);
				event.setResultStack(stack);
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase livingEntity = event.getEntityLiving();
		World world = livingEntity.world;
		if (!world.isRemote)
			return;
		if (livingEntity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) livingEntity;
			ItemStack activeStack = player.getHeldItem(EnumHand.MAIN_HAND);
			NBTTagCompound nbt = activeStack.getTagCompound();
			if (activeStack.isEmpty()
					|| (activeStack.getItem() != ModItems.CHAINSAW && activeStack.getItem() != ModItems.AUGER))
				return;
			if (nbt != null && nbt.hasKey("FuelMax") && nbt.getInteger("FuelMax") > 0) {
				if (SevenDaysToMine.proxy.isHittingBlock(player)) {
					lastTimeHittingBlock = System.currentTimeMillis();
				}

				if (System.currentTimeMillis() > nextChainsawIdleSound) {
					Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundChainsawIdle(player));
					nextChainsawIdleSound = System.currentTimeMillis() + 3000l;
				}
				if (System.currentTimeMillis() > nextChainsawCutSound
						&& System.currentTimeMillis() - getLastTimeHittingBlock() <= 500) {
					Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundChainsawCut(player));
					nextChainsawCutSound = System.currentTimeMillis() + 1600l;
				}
			}
		}

	}

	public static long getLastTimeHittingBlock() {
		return lastTimeHittingBlock;
	}
}