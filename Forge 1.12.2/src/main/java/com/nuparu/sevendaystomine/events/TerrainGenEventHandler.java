package com.nuparu.sevendaystomine.events;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.block.BlockGarbage;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.SimplexNoise;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.CitySavedData;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TerrainGenEventHandler {
	private List<IBlockState> states = new ArrayList<IBlockState>();
	private SimplexNoise noise;

	public TerrainGenEventHandler() {
		Biome.REGISTRY.forEach(b -> {
			states.add(b.topBlock);
		});
	}

	public void onDecoratePrevEvent(PopulateChunkEvent.Post event) {
		World world = event.getWorld();
		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		ChunkPos chunkPos = chunk.getPos();

		if (noise == null || noise.seed != world.getSeed()) {
			noise = new SimplexNoise(world.getSeed());
		}
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				double value = Math.abs(getNoiseValue((chunkPos.x << 4) + i, (chunkPos.z << 4) + j, 0));

				if (value < 0.005d) {
					for (BlockPos pos = chunkPos.getBlock(i, 255, j); pos.getY() >= 63; pos = pos.down()) {
						IBlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						Material material = state.getMaterial();
						if (block == ModBlocks.ASPHALT) {
							if (event.getRand().nextInt(200) == 0) {
								world.setBlockState(pos.up(), ModBlocks.GARBAGE.getDefaultState().withProperty(
										BlockGarbage.FACING, EnumFacing.HORIZONTALS[event.getRand().nextInt(4)]));
								break;
							} else if (event.getRand().nextInt(400) == 0) {
								CityHelper.placeRandomCar(world, pos.up(),
										EnumFacing.HORIZONTALS[event.getRand().nextInt(4)]);
								break;
							} else if (event.getRand().nextInt(1024) == 0) {
								Biome biome = world.getBiome(pos);
								if (biome.getHeightVariation() > 0.2)
									continue;
								if (!CitySavedData.get(world).isCityNearby(pos, 1048576)) {
									City city = new City(world, pos);
									city.startCityGen();
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	public double getNoiseValue(int x, int y, int z) {
		double q1 = noise.getNoise(x, y, z);
		/*
		 * double q2 = noise.getNoise(x + 1.3, y + 0.7, 0.0);
		 * 
		 * double r1 = noise.getNoise(x + 1 * q1 + 1.7, y + 1 * q2 + 9.2, 0.0); double
		 * r2 = noise.getNoise(x + 1 * q1 + 8.3, y + 1 * q2 + 2.8, 0.0);
		 * 
		 * double value = noise.getNoise(x + 2 * q1, y + 2 * q2, z);
		 * 
		 */
		return q1;
	}

}
