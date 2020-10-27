package com.nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import com.nuparu.sevendaystomine.block.BlockFruitBush;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBlueberry extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		pos = pos.add(8, 0, 8);
		if (worldIn.getBiome(pos) == ModBiomes.BURNT_FOREST)
			return false;
		IBlockState toPlace = ModBlocks.BLUEBERRY_PLANT.getDefaultState().withProperty(BlockFruitBush.AGE,
				rand.nextInt(8));
		int y = Utils.getTopSolidGroundHeight(pos, worldIn);

		BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
		IBlockState stateToReplace = worldIn.getBlockState(blockpos);
		Block toReplace = stateToReplace.getBlock();
		
		if(toReplace != Blocks.AIR) return false;

		IBlockState underState = worldIn.getBlockState(blockpos.down());

		if (stateToReplace.getMaterial().isReplaceable()
				&& ((BlockFruitBush) ModBlocks.BLUEBERRY_PLANT).canSustainBush(underState)) {
			worldIn.setBlockState(blockpos, toPlace, 2);
			return true;
		}
		return false;
	}

}
