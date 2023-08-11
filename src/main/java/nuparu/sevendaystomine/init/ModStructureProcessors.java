package nuparu.sevendaystomine.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.levelgen.processor.BookshelfProcessor;
import nuparu.sevendaystomine.world.level.levelgen.processor.ExtendedBlockAgeProcessor;

public class ModStructureProcessors {

    public static final StructureProcessorType<BookshelfProcessor> BOOKSHELF_PROCESSOR = () -> BookshelfProcessor.CODEC;
    public static final StructureProcessorType<ExtendedBlockAgeProcessor> EXTENDED_BLOCK_AGE_PROCESSOR = () -> ExtendedBlockAgeProcessor.CODEC;

    public static void register(){

        Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, new ResourceLocation(SevenDaysToMine.MODID, "bookshelf_processor"), BOOKSHELF_PROCESSOR);
        Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, new ResourceLocation(SevenDaysToMine.MODID, "extended_block_age"), EXTENDED_BLOCK_AGE_PROCESSOR);
    }
}
