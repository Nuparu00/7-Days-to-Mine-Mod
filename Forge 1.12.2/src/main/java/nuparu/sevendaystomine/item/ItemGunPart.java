package nuparu.sevendaystomine.item;

import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemGunPart extends ItemQualityScrapable {

	public String name;
	
	public ItemGunPart(String name, EnumMaterial mat) {
		super(mat);
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}

	public ItemGunPart(String name, EnumMaterial mat, int weight) {
		super(mat, weight);
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_FORGING);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}
}
