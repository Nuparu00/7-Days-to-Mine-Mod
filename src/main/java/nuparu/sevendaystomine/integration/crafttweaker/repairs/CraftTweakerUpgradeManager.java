package nuparu.sevendaystomine.integration.crafttweaker.repairs;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.VanillaManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods." + SevenDaysToMine.MODID + ".upgrade.UpgradeManager")
@ZenRegister
public class CraftTweakerUpgradeManager {
    @ZenMethod
    public static void addUpgrade(IBlockState blockState, IBlockState to, IBlockState prev,  String soundName, IItemStack... items){
        VanillaManager.addVanillaBlockUpgrade(toVanillaState(blockState),toVanillaItemStacks(items), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundName)),toVanillaState(to), toVanillaState(prev));
    }

    public static net.minecraft.block.state.IBlockState toVanillaState(IBlockState state){
        return state == null ? null : (net.minecraft.block.state.IBlockState) state.getInternal();
    }

    public static ItemStack[] toVanillaItemStacks(IItemStack... stacks){
        return Arrays.stream(stacks).map(stack -> stack == null ? null : (ItemStack)stack.getInternal()).toArray(ItemStack[]::new);
    }
}
