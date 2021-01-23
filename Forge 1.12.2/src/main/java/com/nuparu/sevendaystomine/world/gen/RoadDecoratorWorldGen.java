package com.nuparu.sevendaystomine.world.gen;

import java.util.Arrays;
import java.util.Random;

import com.nuparu.sevendaystomine.block.BlockGarbage;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedChunk;
import com.nuparu.sevendaystomine.entity.EntityBandit;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.SimplexNoise;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.CitySavedData;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenLookoutBurnt;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenStick;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.items.IItemHandler;

public class RoadDecoratorWorldGen implements IWorldGenerator {

	private static SimplexNoise noise;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, chunkX, chunkZ);
			break;
		case 0:
			generateOverworld(world, random, chunkX, chunkZ);
			break;
		case 1:
			generateEnd(world, random, chunkX, chunkZ);
			break;
		}
	}

	private void generateNether(World world, Random rand, int chunkX, int blockZ) {
	}

	private void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		if (world.getWorldType() == WorldType.FLAT) {
			return;
		}
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
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
							if (rand.nextInt(300) == 0) {
								world.setBlockState(pos.up(), ModBlocks.GARBAGE.getDefaultState()
										.withProperty(BlockGarbage.FACING, EnumFacing.HORIZONTALS[rand.nextInt(4)]));
								TileEntity te = world.getTileEntity(pos.up());
								if (te != null && te instanceof TileEntityGarbage) {
									TileEntityGarbage tg = (TileEntityGarbage) te;
									ItemUtils.fillWithLoot((IItemHandler) tg.getInventory(), ModLootTables.TRASH, world,
											rand);
								}
								break;
							} else if (rand.nextInt(300) == 0) {
								CityHelper.placeRandomCar(world, pos.up(), EnumFacing.HORIZONTALS[rand.nextInt(4)],true,rand);
								break;
							}
							else if(rand.nextInt(2048) == 0) {
								int count = 2+world.rand.nextInt(6);
								while (count-- > 0){
									EntityBandit bandit = new EntityBandit(world);
									bandit.setPositionAndRotation(pos.getX(), pos.getY()+2, pos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
									if(!world.isRemote) {
										world.spawnEntity(bandit);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void generateEnd(World world, Random rand, int chunkX, int blockZ) {

	}

	public static double getNoiseValue(int x, int y, int z) {
		if(noise == null) return 0;
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
