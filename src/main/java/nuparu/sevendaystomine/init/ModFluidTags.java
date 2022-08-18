package nuparu.sevendaystomine.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModFluidTags {
    public static final TagKey<Fluid> MURKY_WATER_SOURCE = FluidTags.create(new ResourceLocation(SevenDaysToMine.MODID,"murky_water_source"));
}
