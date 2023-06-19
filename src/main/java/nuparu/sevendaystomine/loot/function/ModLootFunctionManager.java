package nuparu.sevendaystomine.loot.function;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModLootFunctionManager {

    public static final LootItemFunctionType RANDOM_COLOR_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_color"), new RandomColorFunction.Serializer());
    public static final LootItemFunctionType RANDOM_QUALITY_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_quality"), new RandomQualityFunction.Serializer());


    public static LootItemFunctionType register(ResourceLocation resourceLocation, Serializer<? extends LootItemFunction> lootSerializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, resourceLocation, new LootItemFunctionType(lootSerializer));
    }
}
