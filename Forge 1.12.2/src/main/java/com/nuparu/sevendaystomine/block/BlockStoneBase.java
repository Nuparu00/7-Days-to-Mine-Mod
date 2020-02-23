package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.block.material.Material;

public class BlockStoneBase extends BlockBase {

	public BlockStoneBase() {
		super(Material.ROCK);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(25);
		setResistance(10);
	}

}
