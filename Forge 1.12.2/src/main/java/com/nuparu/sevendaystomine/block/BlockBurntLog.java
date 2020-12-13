package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockBurntLog extends BlockPillarBase {

	public BlockBurntLog() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setHardness(2F);
		setResistance(10F);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

}
