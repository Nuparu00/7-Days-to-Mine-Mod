package nuparu.sevendaystomine.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import nuparu.sevendaystomine.util.SimplexNoise;

public class TerrainGenEventHandler {
	private List<IBlockState> states = new ArrayList<IBlockState>();
	private SimplexNoise noise;

	public TerrainGenEventHandler() {
		Biome.REGISTRY.forEach(b -> {
			states.add(b.topBlock);
		});
	}


}
