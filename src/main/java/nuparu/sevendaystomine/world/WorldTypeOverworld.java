package nuparu.sevendaystomine.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import nuparu.sevendaystomine.world.gen.ChunkGeneratorOverworldEnhanced;

public class WorldTypeOverworld extends WorldType {

	public WorldTypeOverworld(String name) {
		super(name);
		/*
		 * try { Field f_id = ObfuscationReflectionHelper.findField(WorldType.class,
		 * "id"); f_id.setAccessible(true); int modifiers = f_id.getModifiers(); Field
		 * modifierField = f_id.getClass().getDeclaredField("modifiers"); modifiers =
		 * modifiers & ~Modifier.FINAL; modifierField.setAccessible(true);
		 * modifierField.setInt(f_id, modifiers); f_id.set(WORLD_TYPES[this.getId()],
		 * this.getId()); f_id.set(this, 0); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */

		WORLD_TYPES[this.getId()] = WORLD_TYPES[0];
		WORLD_TYPES[0] = this;
	}

	@Override
	public net.minecraft.world.gen.IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkGeneratorOverworldEnhanced(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(),
				generatorOptions);
	}

}
