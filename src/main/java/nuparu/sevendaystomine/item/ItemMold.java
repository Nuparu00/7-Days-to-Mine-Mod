package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemMold extends Item{

	public ItemMold() {
		setMaxStackSize(1);
		this.setMaxDamage(64);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
	}

	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}
}
