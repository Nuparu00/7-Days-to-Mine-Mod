package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraftforge.oredict.OreDictionary;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemRiotShield extends ItemShield {

	public static final List<ItemStack> ingots = OreDictionary.getOres("ingotIron");
	
	public ItemRiotShield() {
     super();
     this.setMaxDamage(500);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return SevenDaysToMine.proxy.localize("item.riot_shield." + enumdyecolor.getUnlocalizedName() + ".name");
        }
        else
        {
            return SevenDaysToMine.proxy.localize("item.riot_shield.name");
        }
    }
	
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        for(ItemStack stack : ingots) {
        	return ItemStack.areItemsEqual(stack, repair);
        }
        return false;
    }
	
	@Override
	 public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
    {
        return stack.getItem() instanceof ItemShield;
    }
}
