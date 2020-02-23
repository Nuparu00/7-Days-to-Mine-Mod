package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.Item;

public class BlockBaneberry extends BlockFruitBush {

	public BlockBaneberry() {
		
	}
	@Override
    protected Item getCrop()
    {
        return ModItems.BANEBERRY;
    }
}
