package nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import nuparu.sevendaystomine.block.BlockCornPlant;
import nuparu.sevendaystomine.world.biome.BiomeWastelandBase;

public class WorldGenCorn extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		int bottomAge = rand.nextInt(4);
		Biome biome = worldIn.getBiome(pos);
		if(biome instanceof BiomeWastelandBase || biome.getBaseHeight() > 0.25f || biome.getTemperature(pos) < 0.2f || biome.getRainfall() < 0.2f) return false;
		IBlockState bottom = nuparu.sevendaystomine.init.ModBlocks.CORN_PLANT.getDefaultState()
				.withProperty(BlockCornPlant.AGE, bottomAge)
				.withProperty(BlockCornPlant.HALF, BlockCornPlant.EnumBlockHalf.LOWER);
		int y = 1 + getGroundFromAbove(worldIn, pos.getX()+8, pos.getZ()+8);
		//if (y >= pos.getY()) {
			BlockPos blockpos = new BlockPos(pos.getX()+8, y, pos.getZ()+8);
			IBlockState state = worldIn.getBlockState(blockpos);
			Block toReplace = state.getBlock();
			IBlockState state_up = worldIn.getBlockState(blockpos.up());
			if ((toReplace == Blocks.AIR || state.getMaterial() == Material.PLANTS)
					&& (state_up.getBlock() == Blocks.AIR || state_up.getMaterial() == Material.PLANTS)) {
				worldIn.setBlockState(blockpos, bottom, 2);
				if (bottomAge == 3 || (bottomAge == 2 && rand.nextInt(4) == 0)) {
					IBlockState up = nuparu.sevendaystomine.init.ModBlocks.CORN_PLANT.getDefaultState()
							.withProperty(BlockCornPlant.AGE, bottomAge == 3 ? 3 : (rand.nextInt(3)))
							.withProperty(BlockCornPlant.HALF, BlockCornPlant.EnumBlockHalf.UPPER);
					worldIn.setBlockState(blockpos.up(), up, 2);
				}

			//}
		}
		return false;
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
