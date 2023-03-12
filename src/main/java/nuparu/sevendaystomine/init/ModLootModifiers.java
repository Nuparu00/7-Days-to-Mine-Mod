package nuparu.sevendaystomine.init;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.loot.modifier.QualityModifier;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SevenDaysToMine.MODID);

    public static RegistryObject<Codec<? extends IGlobalLootModifier>> QUALITY = LOOT_MODIFIER_SERIALIZERS.register("quality", QualityModifier.CODEC);

}
