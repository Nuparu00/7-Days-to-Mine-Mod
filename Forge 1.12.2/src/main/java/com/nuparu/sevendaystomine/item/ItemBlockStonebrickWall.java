package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.block.BlockStoneBrickWall;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockStonebrickWall extends ItemBlockMetadata {

	public ItemBlockStonebrickWall(Block block) {
		super(block);
	}
	
	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
		return super.getUnlocalizedName() + "." + BlockStoneBrickWall.EnumVariant.byMetadata(itemStack.getMetadata()).getUnlocalizedName();
    }

}
