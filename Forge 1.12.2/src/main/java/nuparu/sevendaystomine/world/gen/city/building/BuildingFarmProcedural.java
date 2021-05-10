package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public class BuildingFarmProcedural extends Building {

	public BuildingFarmProcedural(int weight) {
		super(null, weight, 0, null);
		this.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA)).stream().toArray(Biome[]::new));
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			int width = MathUtils.getIntInRange(rand, 10, 20);
			int length = MathUtils.getIntInRange(rand, 10, 20);
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < length; j++) {
					if (rand.nextDouble() < 0.2)
						continue;
					BlockPos pos2 = Utils.getTopGroundBlock(pos.add(i, 0, j), world, true, true);
					IBlockState state = world.getBlockState(pos2);
					
					if (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT) {
						world.setBlockState(pos2, Blocks.FARMLAND.getDefaultState());
						if (rand.nextDouble() < 0.2)
							continue;
						world.setBlockState(pos2.up(), Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE,MathUtils.getIntInRange(rand, 0, 7)));
					}
				}
			}
		}
	}

}
