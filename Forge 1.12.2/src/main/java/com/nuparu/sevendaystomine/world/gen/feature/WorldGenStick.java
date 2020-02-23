package com.nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import com.nuparu.sevendaystomine.block.BlockStick;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenStick extends WorldGenerator {


	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		IBlockState toPlace = ((BlockStick) com.nuparu.sevendaystomine.init.ModBlocks.STICK)
				.getRandomVariant(rand);
		int y = 1 + getGroundFromAbove(worldIn, pos.getX()+8, pos.getZ()+8);
		if (y >= pos.getY()) {
			BlockPos blockpos = new BlockPos(pos.getX()+8, y, pos.getZ()+8);
			IBlockState state = worldIn.getBlockState(blockpos);
			Block toReplace = state.getBlock();
			if (toReplace == Blocks.AIR || state.getMaterial() == Material.PLANTS) {
				worldIn.setBlockState(blockpos, toPlace, 2);

			}
		}
		return false;
	}

	public static int getGroundFromAbove(World world, int x, int z) {
		int y = 255;
		boolean foundGround = false;
		while (!foundGround && y-- >= 0) {
			Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = blockAt == Blocks.DIRT || blockAt == Blocks.GRASS;
		}

		return y;
	}
}
