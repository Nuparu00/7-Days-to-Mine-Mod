package nuparu.sevendaystomine.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.inventory.entity.ChemistryStationScreen;
import nuparu.sevendaystomine.client.gui.inventory.entity.ForgeScreen;
import nuparu.sevendaystomine.client.gui.inventory.entity.GrillScreen;
import nuparu.sevendaystomine.client.gui.inventory.entity.WorkbenchScreen;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModRecipeTypes;
import nuparu.sevendaystomine.integration.jei.chemistry.ChemistryShapelessCategory;
import nuparu.sevendaystomine.integration.jei.cooking.BeakerShapelessCategory;
import nuparu.sevendaystomine.integration.jei.cooking.CookingGrillShapelessCategory;
import nuparu.sevendaystomine.integration.jei.cooking.CookingPotShapelessCategory;
import nuparu.sevendaystomine.integration.jei.forge.ForgeMaterialCategory;
import nuparu.sevendaystomine.integration.jei.locked.LockedExtension;
import nuparu.sevendaystomine.integration.jei.workbench.WorkbenchCategory;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import nuparu.sevendaystomine.world.item.crafting.ILockedRecipe;
import nuparu.sevendaystomine.world.item.crafting.chemistry.ChemistryRecipeShapeless;
import nuparu.sevendaystomine.world.item.crafting.cooking.CookingShapeless;
import nuparu.sevendaystomine.world.item.crafting.forge.ForgeRecipeMaterial;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    public static JeiPlugin instance;
    public JeiPlugin(){
        instance = this;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SevenDaysToMine.MODID,"jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ForgeMaterialCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BeakerShapelessCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CookingGrillShapelessCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CookingPotShapelessCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ChemistryShapelessCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new WorkbenchCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        if(Minecraft.getInstance().level == null) return;
        registry.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Set.of(new ItemStack(ModBlocks.WIND_TURBINE_BLADES.get())));
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        registry.addRecipes(RecipeTypes.CRAFTING, getScrapRecipes());
        registry.addRecipes(ForgeMaterialCategory.FORGE_MATERIAL_RECIPE_TYPE,getRecipesForClass(ForgeRecipeMaterial.class,manager));
        registry.addRecipes(BeakerShapelessCategory.BEAKER_SHAPELESS_RECIPE_TYPE,
                getCokingRecipes(new ResourceLocation(SevenDaysToMine.MODID,"beaker"),manager));
        registry.addRecipes(CookingGrillShapelessCategory.COOKING_GRILL_SHAPELESS_RECIPE_TYPE,
                getCokingRecipes(new ResourceLocation(SevenDaysToMine.MODID,"cooking_grill"),manager));
        registry.addRecipes(CookingPotShapelessCategory.COOKING_POT_SHAPELESS_RECIPE_TYPE,
                getCokingRecipes(new ResourceLocation(SevenDaysToMine.MODID,"cooking_pot"),manager));
        registry.addRecipes(ChemistryShapelessCategory.CHEMISTRY_SHAPELESS_RECIPE_TYPE,
                manager.getAllRecipesFor(ModRecipeTypes.CHEMISTRY_STATION_RECIPE_TYPE.get()).stream().filter(recipe -> recipe instanceof ChemistryRecipeShapeless).map(ChemistryRecipeShapeless.class::cast).collect(Collectors.toList()));
        registry.addRecipes(WorkbenchCategory.WORKBENCH_RECIPE_TYPE,
                manager.getAllRecipesFor(RecipeType.CRAFTING).stream().filter(recipe -> recipe instanceof ILockedRecipe lockedRecipe && Math.max(lockedRecipe.getHeight(), lockedRecipe.getWidth()) > 3).map(ILockedRecipe.class::cast).collect(Collectors.toList()));

        registry.addIngredientInfo(new ItemStack(ModBlocks.WORKBENCH.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.workbench"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.SEPARATOR.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.separator"));
        registry.addIngredientInfo(new ItemStack(Items.STONE_AXE), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.stone_axe"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.WIND_TURBINE.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.wind_turbine"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.SOLAR_PANEL.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.solar_panel"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.GENERATOR_COMBUSTION.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.combustion_generator"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.GENERATOR_GAS.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.gas_generator"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.FORGE.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.forge"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.CAMERA.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.camera"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.BATTERY_STATION.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.battery_station"));
        //registry.addIngredientInfo(new ItemStack(ModItems.STETHOSCOPE.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.stethoscope"));
        registry.addIngredientInfo(new ItemStack(ModItems.BLOOD_DRAW_KIT.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.blood_draw_kit"));
        //registry.addIngredientInfo(new ItemStack(ModBlocks.THERMOMETER.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.information.thermometer"));

    }

    public <T extends Recipe> List<T>  getRecipesForClass(Class<T> clazz, RecipeManager recipeManager){
        List<T> recipes = new ArrayList<>();
        for(Recipe recipe : recipeManager.getAllRecipesFor(ModRecipeTypes.FORGE_RECIPE_TYPE.get()).stream()
                .filter(recipe -> clazz.isAssignableFrom(recipe.getClass())).toList()){
            recipes.add((T) recipe);
        }
        return recipes;
    }

    public List<CookingShapeless> getCokingRecipes(ResourceLocation station, RecipeManager recipeManager){
        List<CookingShapeless> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING_RECIPE_TYPE.get()).stream().filter(recipe -> recipe instanceof CookingShapeless && recipe.getRequiredStation().equals(station)).map(CookingShapeless.class::cast).collect(Collectors.toList());
        return recipes;
    }

    public List<CraftingRecipe> getScrapRecipes(){
        ArrayList<CraftingRecipe> result = new ArrayList<>();
        for(EnumMaterial material : EnumMaterial.values()){
            if(ScrapDataManager.INSTANCE.hasScrapResult(material)){
                //ItemStack scrapResult = ScrapDataManager.instance.getScrapResult(material);
                ScrapEntry scrapResult = ScrapDataManager.INSTANCE.getScrapResult(material);
                for(ScrapEntry entry : ScrapDataManager.INSTANCE.getScraps()){
                    if(entry.item() == null || !entry.canBeScrapped())
                        continue;
                    if(entry.material() == material) {
                        if (entry.weight().asDouble() > scrapResult.weight().asDouble()) {
                            NonNullList<Ingredient> ingredients = NonNullList.create();
                            ingredients.add(Ingredient.of(new ItemStack(entry.item(),1)));
                            if(ingredients.get(0).isEmpty()) continue;
                            result.add(new ShapelessRecipe(new ResourceLocation(SevenDaysToMine.MODID,"crafting/scrap"),"", CraftingBookCategory.MISC, new ItemStack(scrapResult.item(), (int) Math.floor(entry.weight().asDouble() * ServerConfig.scrapCoefficient.get())), ingredients));
                        }
                        else{
                            int inputCount = (int)Math.ceil(1d/(entry.weight().asDouble() * ServerConfig.scrapCoefficient.get()));
                            if(inputCount < 1) continue;
                            NonNullList<Ingredient> ingredients = NonNullList.create();
                            ingredients.add(Ingredient.of(new ItemStack(entry.item(),inputCount)));
                            if(ingredients.get(0).isEmpty()) continue;
                            result.add(new ShapelessRecipe(new ResourceLocation(SevenDaysToMine.MODID,"crafting/scrap"),"scrap", CraftingBookCategory.MISC, new ItemStack(scrapResult.item(), (int) Math.floor(entry.weight().asDouble() * inputCount * ServerConfig.scrapCoefficient.get())),ingredients));
                        }
                    }
                }
            }
        }
        return result;
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FORGE.get()), ForgeMaterialCategory.FORGE_MATERIAL_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKING_GRILL.get()), CookingGrillShapelessCategory.COOKING_GRILL_SHAPELESS_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKING_POT.get()), CookingPotShapelessCategory.COOKING_POT_SHAPELESS_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BEAKER.get()), BeakerShapelessCategory.BEAKER_SHAPELESS_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WORKBENCH.get()), WorkbenchCategory.WORKBENCH_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CHEMISTRY_STATION.get()), ChemistryShapelessCategory.CHEMISTRY_SHAPELESS_RECIPE_TYPE);

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
       //registration.addRecipeTransferHandler(ContainerForge.class, ModContainers.FORGE.get(),(RecipeType)ModRecipeTypes.FORGE_RECIPE_TYPE.get(), 0,5, ForgeBlockEntity.EnumSlots.MOLD_SLOT.ordinal(),36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
       registration.addRecipeClickArea(ForgeScreen.class, 118, 43, 28, 23, ForgeMaterialCategory.FORGE_MATERIAL_RECIPE_TYPE);
        registration.addRecipeClickArea(GrillScreen.class, 89, 37, 28, 23, CookingGrillShapelessCategory.COOKING_GRILL_SHAPELESS_RECIPE_TYPE,
                CookingPotShapelessCategory.COOKING_POT_SHAPELESS_RECIPE_TYPE, BeakerShapelessCategory.BEAKER_SHAPELESS_RECIPE_TYPE);
        registration.addRecipeClickArea(ChemistryStationScreen.class, 118, 43, 28, 23, ChemistryShapelessCategory.CHEMISTRY_SHAPELESS_RECIPE_TYPE);
        registration.addRecipeClickArea(WorkbenchScreen.class, 98, 43, 28, 23, WorkbenchCategory.WORKBENCH_RECIPE_TYPE);
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IExtendableRecipeCategory<CraftingRecipe, ICraftingCategoryExtension> craftingCategory = registration.getCraftingCategory();
        craftingCategory.addCategoryExtension(ILockedRecipe.class, LockedExtension::new);
    }
}
