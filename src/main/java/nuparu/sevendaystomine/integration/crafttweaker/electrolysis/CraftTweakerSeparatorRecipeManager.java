package nuparu.sevendaystomine.integration.crafttweaker.electrolysis;

import com.google.common.collect.Lists;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipe;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;

@ZenClass("mods." + SevenDaysToMine.MODID + ".recipe.SeparatorRecipeManager")
@ZenRegister
public class CraftTweakerSeparatorRecipeManager {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output1, IItemStack output2, int xp){
        SeparatorRecipeManager.getInstance().addRecipe(new SeparatorRecipe((ItemStack) input.getInternal(),
                Arrays.asList((ItemStack) output1.getInternal(), (ItemStack) output2.getInternal()), xp));
    }
}
