package nuparu.sevendaystomine.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.SimplexNoise;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.City;

public class RoadGenerationWorldGen implements IWorldGenerator {

	private static SimplexNoise noise;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0:
			generateOverworld(world, random, chunkX, chunkZ);
			break;

		}
	}

	private void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {

		if (world.getWorldType() == WorldType.FLAT) {
			return;
		}
		if (world.getWorldType() == SevenDaysToMine.DEFAULT_WORLD
				|| world.getWorldType() == SevenDaysToMine.WASTELAND) {
			return;
		}
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		ChunkPos chunkPos = chunk.getPos();
		if (ModConfig.worldGen.generateRoads) {
			if (noise == null || noise.seed != world.getSeed()) {
				noise = new SimplexNoise(world.getSeed());
			}
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					double value = Math.abs(getNoiseValue((chunkPos.x << 4) + i, (chunkPos.z << 4) + j, 0));
					if (value < 0.005d) {
						for (int k = ModConfig.worldGen.roadMaxY; k >= ModConfig.worldGen.roadMinY; --k) {
							BlockPos pos = chunkPos.getBlock(i, k, j);
							Biome biome = world.getBiome(pos);
							IBlockState state = world.getBlockState(pos);
							Block block = state.getBlock();

							if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)
									|| BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH))
								continue;

							if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)
									&& rand.nextInt(4096) == 0) {
								break;
							}
							if (Utils.isSolid(world, pos, state) && state.getMaterial() != Material.WOOD) {
								for (int l = 1; l < 4; l++) {
									IBlockState state2 = world.getBlockState(pos.up(l));
									if (state2.getMaterial() == Material.WOOD
											|| state2.getBlock().isFoliage(world, pos.up(l)))
										break;
									world.setBlockState(pos.up(l), Blocks.AIR.getDefaultState());
								}
								IBlockState state3 = world.getBlockState(pos.up());
								if (state3.getBlock().isReplaceable(world, pos.up())) {
									world.setBlockState(pos, ModBlocks.ASPHALT.getDefaultState());
								}
								break;
							} else if (k == ModConfig.worldGen.roadMinY && block == Blocks.AIR) {
								world.setBlockState(chunkPos.getBlock(i, k + 1, j),
										ModBlocks.ASPHALT.getDefaultState());
								if (value < 0.001d && rand.nextInt(10) == 0) {
									for (int k2 = k; k2 > 0; --k2) {
										BlockPos pos2 = chunkPos.getBlock(i, k2, j);
										IBlockState state2 = world.getBlockState(pos2);
										if (!state2.getMaterial().isReplaceable()) {
											break;
										}
										world.setBlockState(pos2, Blocks.COBBLESTONE.getDefaultState());
									}

								}
								break;
							}
						}
					}
				}
			}
		}

		long k = rand.nextLong() / 2L * 2L + 1L;
		long l = rand.nextLong() / 2L * 2L + 1L;

		Random rand2 = new Random((long) chunkX * k + (long) chunkZ * l ^ world.getSeed());

		if (Utils.canCityBeGeneratedHere(world, chunkX, chunkZ)) {
			City city = City.foundCity(world, chunkPos, rand2);
			city.startCityGen();
		}
	}

	public static double getNoiseValue(int x, int y, int z) {
		if (noise == null)
			return 0;
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
