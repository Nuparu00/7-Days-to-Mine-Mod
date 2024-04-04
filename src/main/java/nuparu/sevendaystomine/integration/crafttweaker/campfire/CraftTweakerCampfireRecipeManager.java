package nuparu.sevendaystomine.integration.crafttweaker.campfire;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.campfire.CampfireRecipeManager;
import nuparu.sevendaystomine.crafting.campfire.CampfireRecipeShaped;
import nuparu.sevendaystomine.crafting.campfire.CampfireRecipeShapeless;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.integration.crafttweaker.repairs.CraftTweakerUpgradeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods." + SevenDaysToMine.MODID + ".recipe.CampfireRecipeManager")
@ZenRegister
public class CraftTweakerCampfireRecipeManager {
    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IItemStack pot, IItemStack[][] matrix, int xp){
        CampfireRecipeManager.getInstance().addRecipe(new CampfireRecipeShaped((ItemStack) output.getInternal(), (ItemStack) pot.getInternal(),
                Arrays.stream(matrix).map(CraftTweakerUpgradeManager::toVanillaItemStacks).toArray(ItemStack[][]::new), xp));
    }
    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IItemStack pot, IItemStack[] itemStacks, int xp){
        CampfireRecipeManager.getInstance().addRecipe(new CampfireRecipeShapeless((ItemStack) output.getInternal(), (ItemStack) pot.getInternal(),
                Arrays.asList(CraftTweakerUpgradeManager.toVanillaItemStacks(itemStacks)), xp));
    }
}
