package com.nuparu.sevendaystomine.world;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.nuparu.sevendaystomine.world.gen.ChunkGeneratorOverworldEnhanced;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;

public class WorldTypeOverworld extends WorldType {

	public WorldTypeOverworld(String name) {
		super(name);
		WORLD_TYPES[this.getId()] = WORLD_TYPES[0];
		WORLD_TYPES[0] = this;

		try {
			Field f_id = WorldType.class.getDeclaredField("id");
			f_id.setAccessible(true);
			int modifiers = f_id.getModifiers();
			Field modifierField = f_id.getClass().getDeclaredField("modifiers");
			modifiers = modifiers & ~Modifier.FINAL;
			modifierField.setAccessible(true);
			modifierField.setInt(f_id, modifiers);
			f_id.set(WORLD_TYPES[this.getId()], this.getId());
			f_id.set(this, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public net.minecraft.world.gen.IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkGeneratorOverworldEnhanced(world, world.getSeed(),
				world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
	}

}
