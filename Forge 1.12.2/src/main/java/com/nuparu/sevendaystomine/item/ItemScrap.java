package com.nuparu.sevendaystomine.item;

import net.minecraft.item.Item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.ItemUtils;

public class ItemScrap extends Item implements IScrapable {

	private EnumMaterial material;

	private int weight;

	public ItemScrap(EnumMaterial mat) {
		this(mat, 1);
	}

	public ItemScrap(EnumMaterial mat, int weight) {

		this.material = mat;
		this.weight = weight;
		ItemUtils.INSTANCE.addScrapResult(mat, this);
		this.setCreativeTab(SevenDaysToMine.TAB_MATERIALS);
	}

	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	public EnumMaterial getMaterial() {
		return material;
	}

	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	public int getWeight() {
		return weight;
	}

	public boolean canBeScraped() {
		return false;
	}
}
