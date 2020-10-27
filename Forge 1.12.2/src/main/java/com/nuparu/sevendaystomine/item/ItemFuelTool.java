package com.nuparu.sevendaystomine.item;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFuelTool extends ItemQualityTool {

	public SoundEvent refillSound;

	public ItemFuelTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
	}

	public ItemFuelTool(ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
		super(materialIn, effectiveBlocksIn);

	}

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
		super.onCreated(itemstack, world, player);
		initNBT(itemstack);

	}
	
	public void initNBT(ItemStack itemstack) {
		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setFloat("FuelMax", 1000.0F);
		itemstack.getTagCompound().setFloat("FuelCurrent", 0.0F);
		itemstack.getTagCompound().setInteger("ReloadTime", 0);
		itemstack.getTagCompound().setBoolean("Reloading", false);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("FuelCurrent")
				&& stack.getTagCompound().getFloat("FuelCurrent") < 0F) {

			stack.getTagCompound().setFloat("FuelCurrent", 0);
		}

	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("FuelCurrent"))
			return false;
		if (nbt.getFloat("FuelCurrent") > 0.0F) {
			nbt.setFloat("FuelCurrent", nbt.getFloat("FuelCurrent") - (1000F / getQuality(stack)));
			if (nbt.getFloat("FuelCurrent") < 0.1F) {
				nbt.setFloat("FuelCurrent", 0);
			}
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("null")
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		if (stack == null)
			return;
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("FuelCurrent") && nbt.hasKey("FuelMax")) {
			tooltip.add(nbt.getFloat("FuelCurrent") + "/" + nbt.getFloat("FuelMax"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setQuality(stack, (int) Math.min(Math.max(Math.floor(player.getScore()/ItemQuality.XP_PER_QUALITY_POINT), 1),ItemQuality.MAX_QUALITY));
				NBTTagCompound nbt = stack.getTagCompound();
				nbt.setFloat("FuelMax", 1000.0F);
				nbt.setFloat("FuelCurrent", 0.0F);
				nbt.setInteger("ReloadTime", 90000);
				nbt.setBoolean("Reloading", false);
			}
			items.add(stack);
		}
	}

}