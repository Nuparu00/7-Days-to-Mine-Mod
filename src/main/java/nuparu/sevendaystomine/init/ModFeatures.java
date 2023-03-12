package nuparu.sevendaystomine.init;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.levelgen.feature.FeatureRoads;
import nuparu.sevendaystomine.world.level.levelgen.feature.FeatureSmallStone;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
            SevenDaysToMine.MODID);

    //public static final RegistryObject<Feature> ROADS = FEATURES.register("roads",() -> new FeatureRoads(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature> SMALL_STONE = FEATURES.register("small_stone",() -> new FeatureSmallStone(NoneFeatureConfiguration.CODEC.stable()));

}
