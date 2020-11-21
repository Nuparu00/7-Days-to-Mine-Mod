package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.ItemUtils;

import net.minecraft.item.Item;

public class ItemScrapable extends Item implements IScrapable {
	private EnumMaterial material;

	private int weight;

	public ItemScrapable(EnumMaterial mat) {
		this(mat, 1);
	}
	public ItemScrapable(EnumMaterial mat, int weight) {

		this.material = mat;
		this.weight = weight;
		this.setCreativeTab(SevenDaysToMine.TAB_MATERIALS);
	}
	
	public ItemScrapable setSmallestBit() {
		ItemUtils.INSTANCE.addSmallestBit(material, this);
		return this;
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