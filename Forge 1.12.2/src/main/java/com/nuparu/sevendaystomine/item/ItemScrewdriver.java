package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScrewdriver extends ItemQuality {

	public ItemScrewdriver() {

	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			TileEntity TE = worldIn.getTileEntity(pos);
			if (TE != null) {
				if(TE instanceof TileEntityComputer) {
					player.openGui(SevenDaysToMine.instance, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
		return EnumActionResult.PASS;
	}
}
