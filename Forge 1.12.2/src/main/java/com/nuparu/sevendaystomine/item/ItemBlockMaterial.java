package com.nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockMaterial extends ItemBlock implements IScrapable {

	private EnumMaterial material;
	private int weight;
	public boolean canBeScraped;

	public ItemBlockMaterial(Block block) {
		super(block);
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
	
	public void setCanBeScraped(boolean canBeScraped) {
		this.canBeScraped = canBeScraped;
	}

	public boolean canBeScraped() {
		return canBeScraped;
	}

}
