package com.nuparu.sevendaystomine.world.biome;

import java.util.Random;

import com.nuparu.sevendaystomine.block.BlockPillarBase;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenBigTreeBurnt;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenTreesBurnt;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.Biome.FlowerEntry;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeWasteland extends Biome {

	public static final WorldGenTreesBurnt BURNT_TREE_FEATURE = new WorldGenTreesBurnt(false, 4,
			ModBlocks.BURNT_LOG.getDefaultState().withProperty(BlockPillarBase.AXIS, EnumFacing.Axis.Y),
			Blocks.AIR.getDefaultState(), false);
	public static final WorldGenBigTreeBurnt BURNT_BIG_TREE_FEATURE = new WorldGenBigTreeBurnt(false);

	public BiomeWasteland() {
		super(new BiomeProperties("Wasteland").setTemperature(2.0F).setRainfall(0.4F).setWaterColor(0x404736));
		this.flowers.clear();
		this.spawnableCreatureList.clear();
		this.topBlock = ModBlocks.DRY_GROUND.getDefaultState();
		this.decorator.flowersPerChunk = 0;
		this.decorator.treesPerChunk = 12;
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
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return (WorldGenAbstractTree) (rand.nextInt(10) == 0 ? BURNT_BIG_TREE_FEATURE : BURNT_TREE_FEATURE);
	}
}
