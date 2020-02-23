package com.nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.util.ModConstants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemWire extends ItemScrapable {

	public ItemWire() {
		super(EnumMaterial.COPPER, 1);
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		TileEntity te = worldIn.getTileEntity(pos);

		if (te == null || !(te instanceof IVoltage))
			return EnumActionResult.PASS;

		IVoltage voltage = (IVoltage) te;

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if (player.isSneaking()) {
			stack.getTagCompound().setLong("from", pos.toLong());
			if (stack.getTagCompound().hasKey("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getTagCompound().getLong("to");
				BlockPos to = BlockPos.fromLong(l);
				TileEntity te2 = worldIn.getTileEntity(to);
				if (l != pos.toLong() && te2 != null && te2 instanceof IVoltage) {
					ElectricConnection connection = new ElectricConnection(pos, BlockPos.fromLong(l));

					if (voltage.canConnect(connection) && ((IVoltage) te2).canConnect(connection)) {
						voltage.tryToConnect(connection);
						((IVoltage) te2).tryToConnect(connection);
						if (!player.isCreative()) {
							player.setHeldItem(hand, ItemStack.EMPTY);
						}
					}
					stack.setTagCompound(new NBTTagCompound());
				}
			}
		} else {
			stack.getTagCompound().setLong("to", pos.toLong());
			if (stack.getTagCompound().hasKey("from", Constants.NBT.TAG_LONG)) {
				long l = stack.getTagCompound().getLong("from");
				BlockPos from = BlockPos.fromLong(l);
				TileEntity te2 = worldIn.getTileEntity(from);
				if (l != pos.toLong() && te2 != null && te2 instanceof IVoltage) {
					ElectricConnection connection = new ElectricConnection(BlockPos.fromLong(l), pos);

					if (voltage.canConnect(connection) && ((IVoltage) te2).canConnect(connection)) {
						voltage.tryToConnect(connection);
						((IVoltage) te2).tryToConnect(connection);
						if (!player.isCreative()) {
							player.setHeldItem(hand, ItemStack.EMPTY);
						}
					}
					stack.setTagCompound(new NBTTagCompound());
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (isSelected) {
			if (stack.getTagCompound() == null)
				return;
			if (stack.getTagCompound().hasKey("from", Constants.NBT.TAG_LONG)) {
				long l = stack.getTagCompound().getLong("from");
				BlockPos from = BlockPos.fromLong(l);
				if (entityIn.getDistance(from.getX(), from.getY(), from.getZ()) > ModConstants.MAXIMAL_LENGTH) {
					stack.setTagCompound(new NBTTagCompound());
					worldIn.playSound(entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.ENTITY_LEASHKNOT_BREAK,
							SoundCategory.NEUTRAL, 1, 1, false);
				}
				else {
					TileEntity te = worldIn.getTileEntity(from);
					if (te == null || !(te instanceof IVoltage)) {
						stack.setTagCompound(new NBTTagCompound());
						worldIn.playSound(entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.ENTITY_LEASHKNOT_BREAK,
								SoundCategory.NEUTRAL, 1, 1, false);
					}
					
				}

			}

			if (stack.getTagCompound().hasKey("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getTagCompound().getLong("to");
				BlockPos to = BlockPos.fromLong(l);
				if (entityIn.getDistance(to.getX(), to.getY(), to.getZ()) > ModConstants.MAXIMAL_LENGTH) {
					stack.setTagCompound(new NBTTagCompound());
					worldIn.playSound(entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.ENTITY_LEASHKNOT_BREAK,
							SoundCategory.NEUTRAL, 1, 1, false);
				}
			}
		}
	}

	@Override
	public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem) {
		ItemStack stack = entityItem.getItem();

		if (stack.getTagCompound() != null) {
			if (stack.getTagCompound().hasKey("from", Constants.NBT.TAG_LONG)) {
				long l = stack.getTagCompound().getLong("from");
				BlockPos from = BlockPos.fromLong(l);
				if (entityItem.getDistance(from.getX(), from.getY(), from.getZ()) > ModConstants.MAXIMAL_LENGTH) {
					stack.setTagCompound(new NBTTagCompound());
					entityItem.world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ,
							SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
				}

			}

			if (stack.getTagCompound().hasKey("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getTagCompound().getLong("to");
				BlockPos to = BlockPos.fromLong(l);
				if (entityItem.getDistance(to.getX(), to.getY(), to.getZ()) > ModConstants.MAXIMAL_LENGTH) {
					stack.setTagCompound(new NBTTagCompound());
					entityItem.world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ,
							SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
				}
			}
		}

		return false;
	}

}
