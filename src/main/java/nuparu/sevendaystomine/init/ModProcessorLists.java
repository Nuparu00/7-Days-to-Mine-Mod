package nuparu.sevendaystomine.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.levelgen.processor.BookshelfProcessor;

public class ModProcessorLists {
    public static final Holder<StructureProcessorList> DEFAULT = register("default", ImmutableList.of(new BookshelfProcessor()));
    private static Holder<StructureProcessorList> register(String p_206438_, ImmutableList<StructureProcessor> p_206439_) {
        ResourceLocation resourcelocation = new ResourceLocation(SevenDaysToMine.MODID,p_206438_);
        StructureProcessorList structureprocessorlist = new StructureProcessorList(p_206439_);

        return BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST,resourcelocation,  structureprocessorlist);
    }
}
