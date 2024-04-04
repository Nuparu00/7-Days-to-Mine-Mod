package nuparu.sevendaystomine.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.block.IUpgradeable;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakSavedData;
import nuparu.sevendaystomine.block.repair.Repair;
import nuparu.sevendaystomine.block.repair.RepairManager;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.util.VanillaManager.VanillaBlockUpgrade;

@SuppressWarnings("deprecation")
public class ItemUpgrader extends ItemQualityTool {

	public float effect = 1f;

	public ItemUpgrader(float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
	}

	public ItemUpgrader(Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
		super(materialIn, effectiveBlocksIn);
	}

	public ItemUpgrader(Item.ToolMaterial materialIn) {
		this(materialIn, com.google.common.collect.Sets.newHashSet(new Block[] {}));
	}

	public ItemUpgrader setEffectiveness(float effect) {
		this.effect = effect;
		return this;
	}

	@Nullable
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
			@Nullable NBTTagCompound nbt) {
		return null;
	}

	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos blockPos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = playerIn.getHeldItem(hand);

		BlockPos pos = new BlockPos(itemstack.getTagCompound().getInteger("X"),
				itemstack.getTagCompound().getInteger("Y"), itemstack.getTagCompound().getInteger("Z"));
		BreakSavedData data = BreakSavedData.get(worldIn);
		BreakData breakData = data.getBreakData(pos, worldIn.provider.getDimension());
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if (breakData != null && breakData.getState() != 0f) {
			float damage = breakData.getState();
			Repair repair = RepairManager.INSTANCE.getRepair(block, playerIn.inventory);
			if (repair != null) {
				for (int slot = 0; slot < playerIn.inventory.getSizeInventory(); slot++) {
					ItemStack stack = playerIn.inventory.getStackInSlot(slot);
					if (ItemStack.areItemsEqual(stack, repair.getItemStack())) {
						float size = (repair.getPercentage() * 10) * (effect);
						if (damage < (effect)) {
							size = damage * (effect / 10);
						}
						if (stack.getCount() < (int) Math.floor(size * 10f)) {
							continue;
						}
						playerIn.inventory.decrStackSize(slot, (int) Math.ceil(size * 10f));
						worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHelper.UPGRADE_WOOD,
								SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
								MathUtils.getFloatInRange(0.9f, 1f));
						playerIn.swingArm(hand);
						itemstack.damageItem(1, playerIn);
						if (damage - (effect / 1f) <= 0f) {
							data.removeBreakData(pos, worldIn);
							break;
						} else {
							data.setBreakData(pos, worldIn, damage - (effect / 1f));
							break;
						}

					} else {
						if (slot == playerIn.inventory.getSizeInventory() - 1) {
							if (!worldIn.isRemote && !playerIn.isCreative() && !playerIn.isSpectator()) {
								playerIn.sendMessage(new TextComponentTranslation("repair.missing",
										SevenDaysToMine.proxy.localize(
												(repair.getItemStack().getItem().getUnlocalizedName() + ".name")),
										SevenDaysToMine.proxy.localize(
												worldIn.getBlockState(pos).getBlock().getUnlocalizedName() + ".name")));
							}
						}
					}
				}
			}
		} else if (block instanceof IUpgradeable && ((IUpgradeable) block).getResult(worldIn, pos) != null) {
			IUpgradeable upgradeable = ((IUpgradeable) block);
			ItemStack[] itemStacks = upgradeable.getItems();

			if (hasItemStacks(playerIn, block, itemStacks) || playerIn.isCreative()) {
				worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHelper.UPGRADE_WOOD,
						SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
						MathUtils.getFloatInRange(0.9f, 1f));
				itemstack.getTagCompound().setFloat("Percent", itemstack.getTagCompound().getFloat("Percent") + effect);
				playerIn.swingArm(hand);
				if (itemstack.getTagCompound().getFloat("Percent") >= 1F) {
					itemstack.damageItem(1, playerIn);
					upgradeable.onUpgrade(worldIn, pos, state);
					worldIn.setBlockState(pos, upgradeable.getResult(worldIn, pos), 3);
					if (!worldIn.isRemote) {
						ModTriggers.BLOCK_UPGRADE.trigger((EntityPlayerMP) playerIn, state);
					}
					itemstack.getTagCompound().setFloat("Percent", 0F);
					if (!playerIn.isCreative()) {
						removeItemStacks(playerIn.inventory, itemStacks);
					}
				}
			}
		} else if (VanillaManager.getVanillaUpgrade(state) != null) {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			ItemStack[] itemStacks = upgrade.getItems();
			if (hasItemStacks(playerIn, block, itemStacks) || playerIn.isCreative()) {
				worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHelper.UPGRADE_WOOD,
						SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
						MathUtils.getFloatInRange(0.9f, 1f));
				itemstack.getTagCompound().setFloat("Percent", itemstack.getTagCompound().getFloat("Percent") + effect);
				playerIn.swingArm(hand);
				if (itemstack.getTagCompound().getFloat("Percent") >= 1F) {
					itemstack.damageItem(1, playerIn);
					worldIn.setBlockState(pos, upgrade.getResult(), 3);
					if (!worldIn.isRemote) {
						ModTriggers.BLOCK_UPGRADE.trigger((EntityPlayerMP) playerIn, state);
					}
					itemstack.getTagCompound().setFloat("Percent", 0F);
					if (!playerIn.isCreative()) {
						removeItemStacks(playerIn.inventory, itemStacks);
					}
				}
			}
		}

		return EnumActionResult.PASS;
	}

	public boolean hasItemStack(EntityPlayer player, Block block, ItemStack itemStack) {
		int count = 0;
		for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
			ItemStack stack = player.inventory.getStackInSlot(slot);
			if (stack != null && stack.getItem() == itemStack.getItem()) {
				count += stack.getCount();
			}
		}
		return (count >= Math.ceil(itemStack.getCount() * (1f - (effect - 0.25f))));
	}

	public boolean hasItemStacks(EntityPlayer player, Block block, ItemStack[] itemStacks) {
		for (ItemStack itemStack : itemStacks) {
			if (!hasItemStack(player, block, itemStack)) {
				if (!player.world.isRemote && !player.isCreative() && !player.isSpectator()) {
					player.sendMessage(new TextComponentTranslation("upgrade.missing",
							SevenDaysToMine.proxy.localize((itemStack.getItem().getUnlocalizedName() + ".name")),
							SevenDaysToMine.proxy.localize(block.getUnlocalizedName() + ".name")));
				}
				return false;
			}
		}
		return true;
	}

	public void removeItemStacks(InventoryPlayer inv, ItemStack[] itemStacks) {
		for (ItemStack itemStack : itemStacks) {
			int count = (int) Math.ceil(itemStack.getCount() * (1f - (effect - 0.25f)));
			for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
				ItemStack stack = inv.getStackInSlot(slot);
				if (stack != null && stack.getItem() == itemStack.getItem()) {
					int decrease = Math.min(count, stack.getCount());
					inv.decrStackSize(slot, decrease);
					count -= decrease;
					if (count <= 0) {
						break;
					}
				}
			}
		}
	}

	public RayTraceResult rayTrace(Entity entity, double blockReachDistance, float partialTicks) {
		Vec3d vec3 = getPositionEyes(entity, partialTicks);
		Vec3d vec31 = entity.getLook(partialTicks);
		Vec3d vec32 = vec3.addVector(vec31.x * blockReachDistance, vec31.y * blockReachDistance,
				vec31.z * blockReachDistance);
		return entity.world.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	public Vec3d getPositionEyes(Entity entity, float partialTicks) {
		if (partialTicks == 1.0F) {
			return new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ);
		} else {
			double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
			double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks
					+ (double) entity.getEyeHeight();
			double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
			return new Vec3d(d0, d1, d2);
		}
	}

	public void onUpdate(ItemStack itemstack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (itemstack.getTagCompound() == null) {

			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();
			String pattern = "yyyy/MM/dd/HH/mm/ss/SSS";
			SimpleDateFormat f = new SimpleDateFormat(pattern);

			itemstack.setTagCompound(new NBTTagCompound());
			itemstack.getTagCompound().setInteger("X", 0);
			itemstack.getTagCompound().setInteger("Y", 0);
			itemstack.getTagCompound().setInteger("Z", 0);
			itemstack.getTagCompound().setFloat("Percent", 0F);
			itemstack.getTagCompound().setString("ID", entityIn.getUniqueID() + "" + f.format(date));
		}
		if (isSelected) {

			RayTraceResult ray = rayTrace(entityIn, 5, 1);
			if (ray != null) {
				BlockPos blockName = ray.getBlockPos();
				if (blockName != null) {
					if (blockName.getX() != itemstack.getTagCompound().getInteger("X")
							|| blockName.getY() != itemstack.getTagCompound().getInteger("Y")
							|| blockName.getZ() != itemstack.getTagCompound().getInteger("Z")) {
						itemstack.getTagCompound().setInteger("X", blockName.getX());
						itemstack.getTagCompound().setInteger("Y", blockName.getY());
						itemstack.getTagCompound().setInteger("Z", blockName.getZ());
						itemstack.getTagCompound().setFloat("Percent", 0F);

					}
				} else {
					if (0 != itemstack.getTagCompound().getInteger("X")
							|| 0 != itemstack.getTagCompound().getInteger("Y")
							|| 0 != itemstack.getTagCompound().getInteger("Z")
							|| itemstack.getTagCompound().getFloat("Percent") != 0f) {

						itemstack.getTagCompound().setInteger("X", 0);
						itemstack.getTagCompound().setInteger("Y", 0);
						itemstack.getTagCompound().setInteger("Z", 0);
						itemstack.getTagCompound().setFloat("Percent", 0F);
					}
				}

			} else {
				if (0 != itemstack.getTagCompound().getInteger("X") || 0 != itemstack.getTagCompound().getInteger("Y")
						|| 0 != itemstack.getTagCompound().getInteger("Z")
						|| itemstack.getTagCompound().getFloat("Percent") != 0f) {

					itemstack.getTagCompound().setInteger("X", 0);
					itemstack.getTagCompound().setInteger("Y", 0);
					itemstack.getTagCompound().setInteger("Z", 0);
					itemstack.getTagCompound().setFloat("Percent", 0F);
				}
			}

		} else {
			if (0 != itemstack.getTagCompound().getInteger("X") || 0 != itemstack.getTagCompound().getInteger("Y")
					|| 0 != itemstack.getTagCompound().getInteger("Z")
					|| itemstack.getTagCompound().getFloat("Percent") != 0f) {

				itemstack.getTagCompound().setInteger("X", 0);
				itemstack.getTagCompound().setInteger("Y", 0);
				itemstack.getTagCompound().setInteger("Z", 0);
				itemstack.getTagCompound().setFloat("Percent", 0F);
			}
		}
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		if (!slotChanged) {
			if (oldStack.getItem() == newStack.getItem()
					&& (oldStack.getTagCompound().getString("ID")).equals(newStack.getTagCompound().getString("ID"))) {
				return false;
			}
		}
		return true;
	}

	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {

		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String pattern = "yyyy/MM/dd/HH/mm/ss/SSS";
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		itemstack.getTagCompound().setInteger("X", 0);
		itemstack.getTagCompound().setInteger("Y", 0);
		itemstack.getTagCompound().setInteger("Z", 0);
		itemstack.getTagCompound().setFloat("Percent", 0F);
		itemstack.getTagCompound().setString("ID", player.getUniqueID() + "" + f.format(date));
	}
}