package com.nuparu.sevendaystomine.world.gen.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenNull extends MapGenVillage
{
    /** A list of all the biomes villages can spawn in. */
    public static List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.<Biome>asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA);
    /** None */
    private int size;
    private int distance;
    private final int minTownSeparation;

    public MapGenNull()
    {
        this.distance = 32;
        this.minTownSeparation = 8;
    }

    public MapGenNull(Map<String, String> map)
    {
        this();
    }

    @Override
    public String getStructureName()
    {
        return "City";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        return false;
    }
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, this.distance, 8, 10387312, false, 100, findUnexplored);
    }
    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenNull.Start(this.world, this.rand, chunkX, chunkZ, this.size);
    }

    public static class Start extends StructureStart
        {
            /** well ... thats what it does */
            private boolean hasMoreThanTwoComponents;

            public Start()
            {
            }

            public Start(World worldIn, Random rand, int x, int z, int size)
            {
                super(x, z);
               
            }

            /**
             * currently only defined for Villages, returns true if Village has more than 2 non-road components
             */
            @Override
            public boolean isSizeableStructure()
            {
                return this.hasMoreThanTwoComponents;
            }
            @Override
            public void writeToNBT(NBTTagCompound tagCompound)
            {
                super.writeToNBT(tagCompound);
                tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
            }
            @Override
            public void readFromNBT(NBTTagCompound tagCompound)
            {
                super.readFromNBT(tagCompound);
                this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
            }
            
            @Override
            public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
            {
            	
            }
        }
}
