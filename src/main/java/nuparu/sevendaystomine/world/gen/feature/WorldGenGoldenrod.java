package nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import nuparu.sevendaystomine.world.biome.BiomeWastelandBase;

public class WorldGenGoldenrod extends WorldGenerator
{
    private BlockBush flower;
    private IBlockState state;
    public WorldGenGoldenrod(BlockBush flowerIn)
    {
        this.flower = flowerIn;
        this.state = flowerIn.getDefaultState();
    }

    public void setGeneratedBlock(BlockBush flowerIn)
    {
        this.flower = flowerIn;
        this.state = flowerIn.getDefaultState();
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
    	Biome biome = worldIn.getBiome(position);
    	if(biome instanceof BiomeWastelandBase || biome.getBaseHeight() > 0.25f || biome.getTemperature(position) < 0.2f || biome.getRainfall() < 0.2f) return false;
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < 255) && this.flower.canBlockStay(worldIn, blockpos, state))
            {
                worldIn.setBlockState(blockpos, this.state, 2);
            }
        }

        return true;
    }
}