package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

public class ItemQualityScrapable extends ItemQuality implements IScrapable {

	private EnumMaterial material;

	private int weight;

	public ItemQualityScrapable(EnumMaterial mat) {
		this(mat, 1);
	}

	public ItemQualityScrapable(EnumMaterial mat, int weight) {

		this.material = mat;
		this.weight = weight;
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
		return true;
	}

}
