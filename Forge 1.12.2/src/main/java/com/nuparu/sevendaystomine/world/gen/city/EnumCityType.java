package com.nuparu.sevendaystomine.world.gen.city;

import com.nuparu.sevendaystomine.init.ModBlocks;

import net.minecraft.block.state.IBlockState;

public enum EnumCityType {

	VILLAGE("village", false),
	RURAL("rural", false),
	URBAN("urban", true),
	METROPOLIS("metropolis", true);
	
	String name;
	boolean sewers = true;
	int roadLength = 64;
	//How many "asphalt blocks"
	int roadWidth = 7;
	//How many "pavement blocks" on each side
	int pavementWidth;
	//Asphalt block
	IBlockState roadBLock = ModBlocks.ASPHALT.getDefaultState();
	
	EnumCityType(String name, boolean sewers) {
		this.name = name;
		this.sewers = sewers;
	}
}
