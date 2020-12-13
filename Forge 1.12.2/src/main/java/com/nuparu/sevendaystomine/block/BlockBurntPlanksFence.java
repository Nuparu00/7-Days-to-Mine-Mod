package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockBurntPlanksFence extends BlockFenceBase {

	public BlockBurntPlanksFence() {
		super(Material.WOOD, MapColor.BLACK);
		setSoundType(SoundType.WOOD);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

}
