package nuparu.sevendaystomine.integration.crafttweaker.forge;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods." + SevenDaysToMine.MODID + ".recipe.ForgeRecipeManager")
@ZenRegister
public class CraftTweakerForgeRecipeManager {
    @ZenMethod
    public static void addMaterialRecipe(String name, IItemStack output, IItemStack mold, float experience, int cookingTime, CraftTweakerMaterialStack... materialStacks){
        ForgeRecipeManager.getInstance().addMaterialRecipe(new ResourceLocation(name),(ItemStack) output.getInternal(), (ItemStack) mold.getInternal(),
                experience, cookingTime, Arrays.stream(materialStacks).map(CraftTweakerMaterialStack::materialStack).toArray(MaterialStack[]::new));
    }

    @ZenMethod
    public static void removeRecipe(String name){
        ForgeRecipeManager.getInstance().removeRecipe(new ResourceLocation(name));
    }
}
