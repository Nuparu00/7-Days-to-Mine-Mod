package nuparu.sevendaystomine.integration.crafttweaker.chemistry;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeShapeless;
import nuparu.sevendaystomine.integration.crafttweaker.upgrades.CraftTweakerUpgradeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods." + SevenDaysToMine.MODID + ".recipe.ChemistryRecipeManager")
@ZenRegister
public class CraftTweakerChemistryRecipeManager {
    @ZenMethod
    public static void addRecipe(IItemStack output, int xp, IItemStack... ingredients){
        ChemistryRecipeManager.getInstance()
                .addRecipe(new ChemistryRecipeShapeless((ItemStack) output.getInternal(),
                       Arrays.asList(CraftTweakerUpgradeManager.toVanillaItemStacks(ingredients)),xp));
    }
}
