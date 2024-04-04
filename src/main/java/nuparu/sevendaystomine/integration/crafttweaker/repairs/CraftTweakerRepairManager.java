package nuparu.sevendaystomine.integration.crafttweaker.repairs;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.repair.RepairManager;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.integration.crafttweaker.forge.CraftTweakerMaterialStack;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.util.Utils;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + SevenDaysToMine.MODID + ".repair.RepairManager")
@ZenRegister
public class CraftTweakerRepairManager {
    @ZenMethod
    public static void addRepair(IBlock block, IItemStack stack, float percentage){
        RepairManager.INSTANCE.addRepair((Block)block.getDefinition().getInternal(),(ItemStack) stack.getInternal(), percentage);
    }
}
