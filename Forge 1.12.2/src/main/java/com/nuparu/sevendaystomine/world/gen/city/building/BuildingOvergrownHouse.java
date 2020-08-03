package com.nuparu.sevendaystomine.world.gen.city.building;

import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BuildingOvergrownHouse extends Building {
	
	public BuildingOvergrownHouse(ResourceLocation res, int weight) {
		super(res, weight);
	}
	
	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data) {
		Rotation rot = Utils.facingToRotation(facing);
		if (data.equals("sedan")) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			CityHelper.placeRandomCar(world, pos, facing);
		} else if (data.equals("wheels")) {
			if (world.rand.nextInt(10) == 0) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.setBlockState(pos, ModBlocks.WHEELS.getDefaultState().withRotation(rot));
			}
		} else if (data.equals("corpse")) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			if (world.rand.nextInt(5) == 0) {
				world.setBlockState(pos, ModBlocks.CORPSE_00.getDefaultState().withRotation(rot));
			}
		}
	}
}
