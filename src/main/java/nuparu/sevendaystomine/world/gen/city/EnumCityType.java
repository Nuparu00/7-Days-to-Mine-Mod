package nuparu.sevendaystomine.world.gen.city;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import nuparu.sevendaystomine.block.BlockAsphalt;
import nuparu.sevendaystomine.init.ModBlocks;

public enum EnumCityType {

	VILLAGE("village", false,48,5,2,0.25f,ModBlocks.ASPHALT.getDefaultState().withProperty(BlockAsphalt.CITY, true), Blocks.GRASS_PATH.getDefaultState()),
	TOWN("town", false,64,5,2,0.5f,ModBlocks.ASPHALT.getDefaultState().withProperty(BlockAsphalt.CITY, true), Blocks.DOUBLE_STONE_SLAB.getDefaultState()),
	CITY("city", true,80,7,3,0.8f),
	METROPOLIS("metropolis", true,64,7,3,1f);
	
	String name;
	boolean sewers = true;
	int roadLength = 64;
	//How many "asphalt blocks"
	int roadWidth = 7;
	//How many "pavement blocks" on each side
	int pavementWidth = 3;
	float populationMultiplier = 1f;
	//Asphalt block
	IBlockState roadBlock = ModBlocks.ASPHALT.getDefaultState().withProperty(BlockAsphalt.CITY, true);
	IBlockState pavementBlock = null;
	
	EnumCityType(String name, boolean sewers, int roadLength, int roadWidth, int pavementWidth, float populationMultiplier) {
		this.name = name;
		this.sewers = sewers;
		this.roadLength = roadLength;
		this.roadWidth = roadWidth;
		this.pavementWidth = pavementWidth;
	}
	
	EnumCityType(String name, boolean sewers, int roadLength, int roadWidth, int pavementWidth, float populationMultiplier, IBlockState roadBlock) {
		this(name,sewers,roadLength,roadWidth,pavementWidth, populationMultiplier);
		this.roadBlock = roadBlock;
	}
	
	EnumCityType(String name, boolean sewers, int roadLength, int roadWidth, int pavementWidth, float populationMultiplier, IBlockState roadBlock, IBlockState pavementBlock) {
		this(name,sewers,roadLength,roadWidth,pavementWidth, populationMultiplier, roadBlock);
		this.pavementBlock = pavementBlock;
	}

	public int getPavementWidth() {
		return pavementWidth;
	}

	public int getRoadWidth() {
		return roadWidth;
	}
	
	public static EnumCityType getByName(String name) {
		for(EnumCityType type : values()) {
			if(type.name.equals(name)) {
				return type;
			}
		}
		return null;
	}
}
