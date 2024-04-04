package nuparu.sevendaystomine.item;

import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemGunPart extends ItemQuality {

	public String name;
	
	public ItemGunPart(String name) {
		super();
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}
}
