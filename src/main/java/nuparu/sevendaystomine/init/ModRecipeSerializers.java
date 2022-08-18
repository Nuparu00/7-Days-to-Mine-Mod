package nuparu.sevendaystomine.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.crafting.DummyRecipe;
import nuparu.sevendaystomine.world.item.crafting.QualityShapedRecipe;
import nuparu.sevendaystomine.world.item.crafting.ScrapRecipe;
import nuparu.sevendaystomine.world.item.crafting.chemistry.ChemistryRecipeShapeless;
import nuparu.sevendaystomine.world.item.crafting.cooking.CookingShapeless;
import nuparu.sevendaystomine.world.item.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.world.item.crafting.forge.ForgeRecipeShapeless;
import nuparu.sevendaystomine.world.item.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            SevenDaysToMine.MODID);

    public static final RegistryObject<ScrapRecipe.Serializer> SCRAP_SERIALIZER = SERIALIZERS.register("recipe_scrap",
            ScrapRecipe.Serializer::new);
    public static final RegistryObject<QualityShapedRecipe.Serializer> QUALITY_SHAPED_SERIALIZER = SERIALIZERS.register("quality_shaped",
            QualityShapedRecipe.Serializer::new);

    public static final RegistryObject<DummyRecipe.Serializer> DUMMY_SERIALIZER = SERIALIZERS.register("dummy",
            DummyRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> FORGE_SHAPELESS = SERIALIZERS.register("forge_shapeless",
            ForgeRecipeShapeless.Factory::new);
    public static final RegistryObject<RecipeSerializer<?>> FORGE_MATERIAL = SERIALIZERS.register("forge_material",
            ForgeRecipeMaterial.Factory::new);

    public static final RegistryObject<RecipeSerializer<?>> COOKING_SHAPELESS = SERIALIZERS.register("cooking_shapeless",
            CookingShapeless.Factory::new);


    public static final RegistryObject<RecipeSerializer<?>> CHEMISTRY_SHAPELESS = SERIALIZERS.register("chemistry_shapeless",
            ChemistryRecipeShapeless.Factory::new);

}
