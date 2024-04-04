package nuparu.sevendaystomine.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeWastelandOcean extends BiomeWastelandBase {

	public BiomeWastelandOcean(Biome.BiomeProperties properties) {
		super(properties);
		this.flowers.clear();
		this.spawnableCreatureList.clear();
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

	public Biome.TempCategory getTempCategory()
    {
        return Biome.TempCategory.OCEAN;
    }
}
