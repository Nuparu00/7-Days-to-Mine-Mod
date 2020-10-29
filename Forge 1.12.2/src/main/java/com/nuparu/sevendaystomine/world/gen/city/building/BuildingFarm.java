package com.nuparu.sevendaystomine.world.gen.city.building;

import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BuildingFarm extends Building {

	public BuildingFarm(ResourceLocation res, int weight, int yOffset, IBlockState pedestalState) {
		super(res, weight, yOffset, pedestalState);
		this.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new));
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return super.getDimensions(world, facing).add(1, 1, 1);
	}

}
