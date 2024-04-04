package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemCircuit extends Item {

	public ItemCircuit() {
		this.setMaxStackSize(1);
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String data = SevenDaysToMine.proxy.localize("stat.data.null.name");
		if(stack.getTagCompound() != null &&  stack.getTagCompound().hasKey("data")) {
			data = stack.getTagCompound().getString("data");
		}
		tooltip.add(SevenDaysToMine.proxy.localize("stat.data.name",data));
	}
}
