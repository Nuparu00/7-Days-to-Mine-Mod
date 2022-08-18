package nuparu.sevendaystomine.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.crafting.DummyRecipe;
import nuparu.sevendaystomine.world.item.crafting.chemistry.IChemistryRecipe;
import nuparu.sevendaystomine.world.item.crafting.cooking.ICookingRecipe;
import nuparu.sevendaystomine.world.item.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.world.level.block.CookingGrillBLock;
import nuparu.sevendaystomine.world.level.block.entity.ChemistryBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.GrillBlockEntity;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES,
            SevenDaysToMine.MODID);

    public static final RegistryObject<RecipeType<IForgeRecipe<ForgeBlockEntity>>> FORGE_RECIPE_TYPE = RECIPE_TYPES.register("forge",
            () -> RecipeType.simple(new ResourceLocation(SevenDaysToMine.MODID,"forge")));

    public static final RegistryObject<RecipeType<ICookingRecipe<GrillBlockEntity>>> COOKING_RECIPE_TYPE = RECIPE_TYPES.register("cooking",
            () -> RecipeType.simple(new ResourceLocation(SevenDaysToMine.MODID,"cooking")));

    public static final RegistryObject<RecipeType<IChemistryRecipe<ChemistryBlockEntity>>> CHEMISTRY_STATION_RECIPE_TYPE = RECIPE_TYPES.register("chemistry_station",
            () -> RecipeType.simple(new ResourceLocation(SevenDaysToMine.MODID,"chemistry_station")));
}
