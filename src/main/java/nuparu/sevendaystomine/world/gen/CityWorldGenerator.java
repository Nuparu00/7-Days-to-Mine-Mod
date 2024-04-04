package nuparu.sevendaystomine.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class CityWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, blockX, blockZ);
			break;
		case 0:
			generateOverworld(world, random, chunkX, chunkZ);
			break;
		case 1:
			generateEnd(world, random, blockX, blockZ);
			break;
		}
	}

	private void generateNether(World world, Random rand, int blockX, int blockZ) {
	}


	private void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		if(world.getWorldType()==WorldType.FLAT) {
			return;
		}
		
		if(chunkX % 64 != 0 || chunkZ % 64 != 0) {
			return;
		}	
		
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		
		BlockPos pos = new BlockPos(blockX, 64, blockZ);
		Biome biome = world.getBiomeForCoordsBody(pos);
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		/*
		for(int i = -3; i < 3; i++) {
			for(int j = -3; j < 3; j++) {
				if(i == 0 && j == 0) continue;
				if(!world.isChunkGeneratedAt((chunk.x+i)*16, (chunk.z+j)*16)) continue;
				if(!world.isBlockLoaded(new BlockPos((chunk.x+i)*16,0, (chunk.z+j)*16))) continue;
				Chunk chunk2 = world.getChunkFromChunkCoords(chunk.x+i, chunk.z+j);
				IExtendedChunk extendedChunk = CapabilityHelper.getExtendedChunk(chunk2);
				if(extendedChunk.hasCity()) {
					return;
				}
			}
		}*/
			/*City city = new City(world,pos);
			city.startCityGen();
			IExtendedChunk extendedChunk = CapabilityHelper.getExtendedChunk(chunk);
			extendedChunk.setCity(city);*/

	}

	private void generateEnd(World world, Random rand, int blockX, int blockZ) {

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
