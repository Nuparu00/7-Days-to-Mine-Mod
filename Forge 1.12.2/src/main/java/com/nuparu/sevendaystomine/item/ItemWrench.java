package com.nuparu.sevendaystomine.item;

import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.advancements.ModTriggers;
import com.nuparu.sevendaystomine.block.ISalvageable;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends ItemUpgrader {
	public ItemWrench() {
		super(SevenDaysToMine.IRON_TOOLS);
		setEffectiveness(0.33334f);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos blockPos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = playerIn.getHeldItem(hand);

		BlockPos pos = new BlockPos(itemstack.getTagCompound().getInteger("X"),
				itemstack.getTagCompound().getInteger("Y"), itemstack.getTagCompound().getInteger("Z"));
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof ISalvageable) {
			ISalvageable salvageable = (ISalvageable) block;
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, salvageable.getSound(),
					SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f), MathUtils.getFloatInRange(0.9f, 1f));
			itemstack.getTagCompound().setFloat("Percent",
					itemstack.getTagCompound().getFloat("Percent") - effect*salvageable.getUpgradeRate(worldIn, pos, state, playerIn) / 30f);
			playerIn.swingArm(hand);
			if (itemstack.getTagCompound().getFloat("Percent") <= -1F) {
				itemstack.damageItem(1, playerIn);
				salvageable.onSalvage(worldIn, pos, state);
				if (!worldIn.isRemote) {
					ModTriggers.BLOCK_UPGRADE.trigger((EntityPlayerMP) playerIn, state);
				}
				itemstack.getTagCompound().setFloat("Percent", 0F);
				if (!worldIn.isRemote) {
					List<ItemStack> stacks = salvageable.getItems(worldIn, pos, state, playerIn);
					for (ItemStack stack : stacks) {
						if (!playerIn.addItemStackToInventory(stack)) {
							playerIn.dropItem(stack, false);
						}
					}
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(playerIn, worldIn, blockPos, hand, facing, hitX, hitY, hitZ);
	}
}
