package nuparu.sevendaystomine.integration.crafttweaker.scrap;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.integration.crafttweaker.forge.CraftTweakerMaterialStack;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.util.Utils;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + SevenDaysToMine.MODID + ".item.ScrapDataManager")
@ZenRegister
public class CraftTweakerScrapDataManager {
    @ZenMethod
    public static void addScrap(IItemStack itemStack, String materialName, String weightString, @Optional(valueBoolean = true) boolean canBeScrapped, @Optional boolean isScrapBit){
        EnumMaterial.byName(materialName)
                .ifPresent(material ->
                                Utils.parseNumber(weightString).ifPresent(weight ->
                                    ScrapDataManager.getInstance()
                                            .addScrap(((ItemStack)itemStack.getInternal()).getItem(), material, weight, canBeScrapped, isScrapBit,false)));
    }

    @ZenMethod
    public static void addScrapResult(IItemStack itemStack, String materialName, String weightString){
        EnumMaterial.byName(materialName)
                .ifPresent(material ->
                        Utils.parseNumber(weightString).ifPresent(weight ->
                            ScrapDataManager.getInstance()
                                    .addScrap(((ItemStack)itemStack.getInternal()).getItem(), material, weight, false, true,false)));
    }
    @ZenMethod
    public static void addScrap(IItemStack itemStack, String materialName, int weight, @Optional(valueBoolean = true) boolean canBeScrapped, @Optional boolean isScrapBit){
        EnumMaterial.byName(materialName)
                .ifPresent(material ->
                        ScrapDataManager.getInstance()
                                .addScrap(((ItemStack)itemStack.getInternal()).getItem(), material, weight, canBeScrapped, isScrapBit,false));
    }

    @ZenMethod
    public static void addScrapResult(IItemStack itemStack, String materialName, int weight){
        EnumMaterial.byName(materialName)
                .ifPresent(material ->
                        ScrapDataManager.getInstance()
                                .addScrap(((ItemStack)itemStack.getInternal()).getItem(), material, weight, false, true,false));
    }
    @ZenMethod
    public static void addScrap(IItemStack itemStack, String materialName, double weight, @Optional(valueBoolean = true) boolean canBeScrapped, @Optional boolean isScrapBit){
        EnumMaterial.byName(materialName)
                .ifPresent(material ->
                        ScrapDataManager.getInstance()
                                .addScrap(((ItemStack)itemStack.getInternal()).getItem(), material, weight, canBeScrapped, isScrapBit,false));
    }

    @ZenMethod
    public static void addScrapResult(IItemStack itemStack, String materialName, double weight){
        EnumMaterial.byName(materialName)
                .ifPresent(material ->
                        ScrapDataManager.getInstance()
                                .addScrap(((ItemStack)itemStack.getInternal()).getItem(), material, weight, false, true,false));
    }

    @ZenMethod
    public static void addScrap(IItemStack itemStack, CraftTweakerMaterialStack materialStack, @Optional(valueBoolean = true) boolean canBeScrapped, @Optional boolean isScrapBit){
        ScrapDataManager.getInstance()
                .addScrap(((ItemStack)itemStack.getInternal()).getItem(), materialStack.materialStack().material(), materialStack.weight(), canBeScrapped, isScrapBit,false);
    }

    @ZenMethod
    public static void addScrapResult(IItemStack itemStack, CraftTweakerMaterialStack materialStack){
        ScrapDataManager.getInstance()
                .addScrap(((ItemStack)itemStack.getInternal()).getItem(), materialStack.materialStack().material(), materialStack.weight(), false, true,false);
    }

    @ZenMethod
    public static void removeScrap(IItemStack itemStack){
        ScrapDataManager.getInstance().removeScrapLater(((ItemStack)itemStack.getInternal()).getItem());
    }
}
