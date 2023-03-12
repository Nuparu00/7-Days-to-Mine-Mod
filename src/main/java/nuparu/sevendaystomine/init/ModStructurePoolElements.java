package nuparu.sevendaystomine.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.levelgen.deserializers.ModSinglePoolElement;

public class ModStructurePoolElements {
    public static final StructurePoolElementType<ModSinglePoolElement> MODDED = register( new ResourceLocation(SevenDaysToMine.MODID, "mod_single_pool_element"), ModSinglePoolElement.CODEC);

    static <P extends StructurePoolElement> StructurePoolElementType<P> register(ResourceLocation resourceLocation, Codec<P> p_210552_) {
        return Registry.register(Registry.STRUCTURE_POOL_ELEMENT, resourceLocation, () -> p_210552_);
    }
}
