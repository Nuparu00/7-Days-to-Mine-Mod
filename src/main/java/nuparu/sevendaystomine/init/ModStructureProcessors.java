package nuparu.sevendaystomine.init;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.levelgen.processor.BookshelfProcessor;

public class ModStructureProcessors {

    public static StructureProcessorType<BookshelfProcessor> BOOKSHELF_PROCESSOR = () -> BookshelfProcessor.CODEC;

    public static void register(){
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(SevenDaysToMine.MODID, "bookshelf_processor"), BOOKSHELF_PROCESSOR);
    }
}
