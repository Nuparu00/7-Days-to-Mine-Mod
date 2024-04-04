package nuparu.sevendaystomine.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.block.BlockPillarBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.gen.feature.WorldGenTreesBurnt;

public class BiomeWasteland extends BiomeWastelandBase {
	public static final WorldGenTreesBurnt BURNT_TREE_FEATURE = new WorldGenTreesBurnt(false, 4,
			ModBlocks.BURNT_LOG.getDefaultState().withProperty(BlockPillarBase.AXIS, EnumFacing.Axis.Y),
			Blocks.AIR.getDefaultState(), false);
	public BiomeWasteland() {
		super(new BiomeProperties("Wasteland").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(2.0F)
				.setRainfall(0.4F).setWaterColor(0x404736));
		this.flowers.clear();
		this.spawnableCreatureList.clear();
		this.topBlock = ModBlocks.DRY_GROUND.getDefaultState();
		this.decorator.flowersPerChunk = 0;
		this.decorator.treesPerChunk = 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return getModdedBiomeGrassColor(0x786043);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		return getModdedBiomeGrassColor(0x786043);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0x5e5140;
	}

	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {

		this.topBlock = ModBlocks.DRY_GROUND.getDefaultState();

		if (rand.nextInt(12) == 0) {
			if (noiseVal > 1D) {
				this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT,
						BlockDirt.DirtType.COARSE_DIRT);
			} else if (noiseVal > 0.5D) {
				this.topBlock = Blocks.SAND.getDefaultState();
			}
		}

		this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return BURNT_TREE_FEATURE;
	}

}
