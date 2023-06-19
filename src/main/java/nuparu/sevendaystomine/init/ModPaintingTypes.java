package nuparu.sevendaystomine.init;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModPaintingTypes {

    public static final DeferredRegister<PaintingVariant> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, SevenDaysToMine.MODID);
    public static RegistryObject<PaintingVariant> EARTH = PAINTING_TYPES.register("earth",()-> new PaintingVariant(32,16));
    public static RegistryObject<PaintingVariant> PERIODIC_TABLE = PAINTING_TYPES.register("periodic_table",()-> new PaintingVariant(48,32));
    public static RegistryObject<PaintingVariant> UNITED_STATES_MAP = PAINTING_TYPES.register("united_states_map",()-> new PaintingVariant(48,32));
    public static RegistryObject<PaintingVariant> ROMA = PAINTING_TYPES.register("roma",()-> new PaintingVariant(48,32));

}
