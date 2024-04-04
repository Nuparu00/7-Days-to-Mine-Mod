package nuparu.sevendaystomine.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import nuparu.sevendaystomine.client.gui.inventory.GuiCampfire;
import nuparu.sevendaystomine.client.gui.inventory.GuiChemistryStation;
import nuparu.sevendaystomine.client.gui.inventory.GuiForge;
import nuparu.sevendaystomine.client.gui.inventory.GuiSeparator;
import nuparu.sevendaystomine.client.gui.inventory.GuiWorkbench;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.crafting.scrap.ScrapEntry;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.integration.jei.campfire.CampfireRecipeCategory;
import nuparu.sevendaystomine.integration.jei.campfire.CampfireRecipeMaker;
import nuparu.sevendaystomine.integration.jei.chemistry.ChemistryRecipeCategory;
import nuparu.sevendaystomine.integration.jei.chemistry.ChemistryRecipeMaker;
import nuparu.sevendaystomine.integration.jei.forge.ForgeRecipeCategory;
import nuparu.sevendaystomine.integration.jei.forge.ForgeRecipeMaker;
import nuparu.sevendaystomine.integration.jei.separator.SeparatorRecipeCategory;
import nuparu.sevendaystomine.integration.jei.separator.SeparatorRecipeMaker;
import nuparu.sevendaystomine.integration.jei.workbench.WorkbenchRecipeCategory;
import nuparu.sevendaystomine.integration.jei.workbench.WorkbenchRecipeMaker;
import nuparu.sevendaystomine.item.EnumMaterial;

import java.util.ArrayList;
import java.util.Arrays;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers helpers = registry.getJeiHelpers();
		final IGuiHelper gui = helpers.getGuiHelper();
		
		registry.addRecipeCategories(new ForgeRecipeCategory(gui));
		registry.addRecipeCategories(new CampfireRecipeCategory(gui));
		registry.addRecipeCategories(new ChemistryRecipeCategory(gui));
		registry.addRecipeCategories(new SeparatorRecipeCategory(gui));
		registry.addRecipeCategories(new WorkbenchRecipeCategory(gui));
	}
	
	@Override
	public void register(IModRegistry registry) {
		final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		final IJeiHelpers helpers = registry.getJeiHelpers();
		IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

		registry.addRecipes(ForgeRecipeMaker.getRecipes(helpers),"Forge");
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.FORGE), "Forge");
		registry.addRecipeClickArea(GuiForge.class, 115, 38, 24, 17, "Forge");
		
		registry.addRecipes(CampfireRecipeMaker.getRecipes(helpers),"Campfire");
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.CAMPFIRE), "Campfire");
		registry.addRecipeClickArea(GuiCampfire.class, 115, 38, 24, 17, "Campfire");
		
		registry.addRecipes(ChemistryRecipeMaker.getRecipes(helpers),"Chemistry Station");
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.CHEMISTRY_STATION), "Chemistry Station");
		registry.addRecipeClickArea(GuiChemistryStation.class, 115, 38, 24, 17, "Chemistry Station");
		
		registry.addRecipes(SeparatorRecipeMaker.getRecipes(helpers),"Electrolytic Cell");
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.SEPARATOR), "Electrolytic Cell");
		registry.addRecipeClickArea(GuiSeparator.class, 115, 38, 24, 17, "Electrolytic Cell");
		
		registry.addRecipes(WorkbenchRecipeMaker.getRecipes(helpers),"Workbench");
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.WORKBENCH), "Workbench");
		registry.addRecipeClickArea(GuiWorkbench.class, 96, 38, 24, 17, "Workbench");
		
		registry.addIngredientInfo(new ItemStack(ModBlocks.WORKBENCH), VanillaTypes.ITEM, "jei.information.workbench");
		registry.addIngredientInfo(new ItemStack(ModBlocks.SEPARATOR), VanillaTypes.ITEM, "jei.information.separator");
		registry.addIngredientInfo(new ItemStack(ModItems.STONE_AXE), VanillaTypes.ITEM, "jei.information.stone_axe");
		registry.addIngredientInfo(new ItemStack(ModBlocks.WIND_TURBINE), VanillaTypes.ITEM, "jei.information.wind_turbine");
		registry.addIngredientInfo(new ItemStack(ModBlocks.SOLAR_PANEL), VanillaTypes.ITEM, "jei.information.solar_panel");
		registry.addIngredientInfo(new ItemStack(ModBlocks.GENERATOR_COMBUSTION), VanillaTypes.ITEM, "jei.information.combustion_generator");
		registry.addIngredientInfo(new ItemStack(ModBlocks.GENERATOR_GAS), VanillaTypes.ITEM, "jei.information.gas_generator");
		registry.addIngredientInfo(new ItemStack(ModBlocks.FORGE), VanillaTypes.ITEM, "jei.information.forge");
		registry.addIngredientInfo(new ItemStack(ModBlocks.CAMPFIRE), VanillaTypes.ITEM, "jei.information.campfire");
		registry.addIngredientInfo(new ItemStack(ModBlocks.CAMERA), VanillaTypes.ITEM, "jei.information.camera");
		registry.addIngredientInfo(new ItemStack(ModBlocks.BATTERY_STATION), VanillaTypes.ITEM, "jei.information.battery_station");
		registry.addIngredientInfo(new ItemStack(ModItems.STETHOSCOPE), VanillaTypes.ITEM, "jei.information.stethoscope");
		registry.addIngredientInfo(new ItemStack(ModItems.BLOOD_DRAW_KIT), VanillaTypes.ITEM, "jei.information.blood_draw_kit");
		registry.addIngredientInfo(new ItemStack(ModBlocks.THERMOMETER), VanillaTypes.ITEM, "jei.information.thermometer");

		registerScrapRecipes(registry);

		IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

		blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.FLOWER_POT_ENHANCED));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.KEY_SAFE));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.SLEEPING_BAG));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.FRIDGE,1, OreDictionary.WILDCARD_VALUE));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.STRUCTURE_STONE,1, OreDictionary.WILDCARD_VALUE));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.STRUCTURE_COBBLESTONE,1, OreDictionary.WILDCARD_VALUE));
		blacklist.addIngredientToBlacklist(new ItemStack(ModItems.VOMIT));
		blacklist.addIngredientToBlacklist(new ItemStack(ModItems.BLOODMOON));
		blacklist.addIngredientToBlacklist(new ItemStack(ModItems.MICROPHONE));
		blacklist.addIngredientToBlacklist(new ItemStack(ModItems.REALITY_WAND));
	}

	private void registerScrapRecipes(IModRegistry registry) {
		ArrayList<IRecipe> recipes = new ArrayList<>();
		for(ScrapEntry scrapEntry : ScrapDataManager.getInstance().getScraps()){
			EnumMaterial material = scrapEntry.material();

			ScrapEntry scrapResult = ScrapDataManager.getInstance().getScrapResult(material);
			if(scrapResult == null || scrapResult == scrapEntry) continue;

			if(scrapEntry.weight().asDouble() * ModConfig.players.scrapCoefficient < scrapResult.weight().asDouble()) {
				int count = (int) Math.ceil(scrapResult.weight().asDouble() / (scrapEntry.weight().asDouble() * ModConfig.players.scrapCoefficient));

				if(count <= 0 || scrapEntry.item() == null){
					int i = 0;
				}

				recipes.add(new ShapelessRecipes("scrap", new ItemStack(scrapResult.item(), 1), NonNullList.withSize(count,Ingredient.fromItem(scrapEntry.item()))));
			}
			else{
				int count = (int) Math.floor((scrapEntry.weight().asDouble() * ModConfig.players.scrapCoefficient) / scrapResult.weight().asDouble());

				if(count <= 0 || scrapEntry.item() == null){
					int i = 0;
				}
				recipes.add(new ShapelessRecipes("scrap", new ItemStack(scrapResult.item(), count), NonNullList.withSize(1,Ingredient.fromItem(scrapEntry.item()))));
			}
		}
		registry.addRecipes(recipes, VanillaRecipeCategoryUid.CRAFTING);
	}

}
