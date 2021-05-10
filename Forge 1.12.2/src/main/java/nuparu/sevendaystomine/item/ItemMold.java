package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemMold extends Item{

	public ItemMold() {
		setMaxStackSize(1);
		this.setMaxDamage(64);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
	}
}
