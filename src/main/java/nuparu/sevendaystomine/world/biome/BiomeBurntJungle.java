package nuparu.sevendaystomine.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.block.BlockBurntLog;
import nuparu.sevendaystomine.block.BlockPillarBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.gen.feature.WorldGenBigTreeBurnt;
import nuparu.sevendaystomine.world.gen.feature.WorldGenMegaJungleBurnt;
import nuparu.sevendaystomine.world.gen.feature.WorldGenTreesBurnt;

public class BiomeBurntJungle extends BiomeWastelandBase {

	private final boolean isEdge = false;

	public static final WorldGenTreesBurnt BURNT_TREE_FEATURE = new WorldGenTreesBurnt(false, 4,
			ModBlocks.BURNT_LOG.getDefaultState().withProperty(BlockPillarBase.AXIS, EnumFacing.Axis.Y),
			Blocks.AIR.getDefaultState(), false);
	public static final WorldGenBigTreeBurnt BURNT_BIG_TREE_FEATURE = new WorldGenBigTreeBurnt(false);

	public BiomeBurntJungle() {
		super(new BiomeProperties("Burnt Jungle").setTemperature(2.0F).setRainfall(0.4F).setWaterColor(0x404736));
		this.flowers.clear();
		this.spawnableCreatureList.clear();
		this.decorator.flowersPerChunk = 0;
		this.decorator.treesPerChunk = 12;
		this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return getModdedBiomeGrassColor(0x5c5041);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		return getModdedBiomeGrassColor(0x5c5041);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0x5e5140;
	}

	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		if (rand.nextInt(10) == 0) {
			return BURNT_BIG_TREE_FEATURE;
		} else if (rand.nextInt(2) == 0) {
			 return new WorldGenShrub(ModBlocks.BURNT_LOG.getDefaultState().withProperty(BlockBurntLog.AXIS, EnumFacing.Axis.Y), Blocks.AIR.getDefaultState());
		} else {
			return (WorldGenAbstractTree) (!this.isEdge && rand.nextInt(3) == 0
					? new WorldGenMegaJungleBurnt(false, 10, 20, ModBlocks.BURNT_LOG.getDefaultState().withProperty(BlockBurntLog.AXIS, EnumFacing.Axis.Y),
							Blocks.AIR.getDefaultState())
					: new WorldGenTrees(false, 4 + rand.nextInt(7), ModBlocks.BURNT_LOG.getDefaultState().withProperty(BlockBurntLog.AXIS, EnumFacing.Axis.Y),
							Blocks.AIR.getDefaultState(), false));
		}
	}
	
	@Override
	public boolean floatingParticles() {
		return true;
	}
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {

		this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);

		if (rand.nextInt(12) == 0) {
			if (noiseVal > 1D) {
				this.topBlock = Blocks.GRASS.getDefaultState();
			} else if (noiseVal > 0.5D) {
				this.topBlock = Blocks.SAND.getDefaultState();
			}
		}

		this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
	}
}
