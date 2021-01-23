package com.nuparu.sevendaystomine.world;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.nuparu.sevendaystomine.world.gen.ChunkGeneratorOverworldEnhanced;
import com.nuparu.sevendaystomine.world.gen.ChunkGeneratorWasteland;
import com.nuparu.sevendaystomine.world.gen.layer.WastelandGenLayerBiome;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class WorldTypeWasteland extends WorldType {

	public WorldTypeWasteland(String name) {
		super(name);
	}

	@Override
	public net.minecraft.world.gen.IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkGeneratorWasteland(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(),
				generatorOptions);
	}
	@Override
	public net.minecraft.world.biome.BiomeProvider getBiomeProvider(World world)
    {
	return new BiomeProviderWasteland(world);	
    }

	@Override
	public net.minecraft.world.gen.layer.GenLayer getBiomeLayer(long worldSeed, net.minecraft.world.gen.layer.GenLayer parentLayer, net.minecraft.world.gen.ChunkGeneratorSettings chunkSettings)
    {
        net.minecraft.world.gen.layer.GenLayer ret = new WastelandGenLayerBiome(200L, parentLayer, this, chunkSettings);
        ret = net.minecraft.world.gen.layer.GenLayerZoom.magnify(1000L, ret, 2);
        ret = new net.minecraft.world.gen.layer.GenLayerBiomeEdge(1000L, ret);
        return ret;
    }
}
