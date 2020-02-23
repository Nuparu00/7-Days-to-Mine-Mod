package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class BlockBlueberry extends BlockFruitBush {

	public BlockBlueberry() {
		
	}
	@Override
    protected Item getCrop()
    {
        return ModItems.BLUEBERRY;
    }

}
