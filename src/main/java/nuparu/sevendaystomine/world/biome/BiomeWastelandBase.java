package nuparu.sevendaystomine.world.biome;

import net.minecraft.world.biome.Biome;

public class BiomeWastelandBase extends Biome {

	public BiomeWastelandBase(BiomeProperties properties) {
		super(properties);
	}
	
	public boolean floatingParticles() {
		return false;
	}

}
