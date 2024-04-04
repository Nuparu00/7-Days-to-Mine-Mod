package nuparu.sevendaystomine.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenModScatteredFeature extends MapGenStructure {

	private int maxDistanceBetweenScatteredFeatures;
	private final int minDistanceBetweenScatteredFeatures;

	public MapGenModScatteredFeature() {
		this.maxDistanceBetweenScatteredFeatures = 32;
		this.minDistanceBetweenScatteredFeatures = 8;
	}

	@Override
	public String getStructureName() {
		return "Scattered";
	}

	@Override
	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
		this.world = worldIn;
		return findNearestStructurePosBySpacing(worldIn, this, pos, this.maxDistanceBetweenScatteredFeatures, 8,
				14357617, false, 100, findUnexplored);
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		int i = chunkX;
		int j = chunkZ;

		if (chunkX < 0) {
			chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
		}

		if (chunkZ < 0) {
			chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
		}

		int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
		int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
		Random random = this.world.setRandomSeed(k, l, 14357617);
		k = k * this.maxDistanceBetweenScatteredFeatures;
		l = l * this.maxDistanceBetweenScatteredFeatures;
		k = k + random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
		l = l + random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);

		if (i == k && j == l) {
			Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8));

			if (biome == null) {
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new MapGenModScatteredFeature.Start(this.world, this.rand, chunkX, chunkZ);
	}

	public static class Start extends StructureStart {
		public Start() {
		}

		public Start(World worldIn, Random random, int chunkX, int chunkZ) {
			this(worldIn, random, chunkX, chunkZ, worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)));
		}

		public Start(World worldIn, Random random, int chunkX, int chunkZ, Biome biomeIn) {
			super(chunkX, chunkZ);
		}

	}

}
