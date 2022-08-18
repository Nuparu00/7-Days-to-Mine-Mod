package nuparu.sevendaystomine.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.tags.ITag;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModBlocksTags {
    public static final TagKey<Block> MURKY_WATER_SOURCE = BlockTags.create(new ResourceLocation(SevenDaysToMine.MODID,"murky_water_source"));
    public static final TagKey<Block> NEEDS_SCRAP_TOOL = BlockTags.create(new ResourceLocation(SevenDaysToMine.MODID,"needs_scrap_tool"));
    public static final TagKey<Block> NEEDS_COPPER_TOOL = BlockTags.create(new ResourceLocation(SevenDaysToMine.MODID,"needs_copper_tool"));
    public static final TagKey<Block> NEEDS_BRONZE_TOOL = BlockTags.create(new ResourceLocation(SevenDaysToMine.MODID,"needs_bronze_tool"));
    public static final TagKey<Block> NEEDS_STEEL_TOOL = BlockTags.create(new ResourceLocation(SevenDaysToMine.MODID,"needs_steel_tool"));
}
