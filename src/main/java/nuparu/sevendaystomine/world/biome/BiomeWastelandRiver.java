package nuparu.sevendaystomine.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeWastelandRiver extends BiomeWastelandBase {

	public BiomeWastelandRiver() {
		super(new Biome.BiomeProperties("Wasteland River").setBaseHeight(-0.5F).setHeightVariation(0.0F).setWaterColor(0x404736));
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
}
