package com.nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;

public class BlockGoldenrod extends BlockBush {

	public BlockGoldenrod() {
		setHardness(0.01F);
		setResistance(2.0F);
		setLightLevel(0.0F);
		setSoundType(SoundType.PLANT);
	}
	
	public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

}
