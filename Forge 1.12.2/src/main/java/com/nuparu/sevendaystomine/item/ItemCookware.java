package com.nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemCookware extends ItemBlock implements IScrapable{

	private EnumMaterial material;
	private int weight;
	
	public ItemCookware(Block block) {
		super(block);
		setMaxStackSize(1);
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
