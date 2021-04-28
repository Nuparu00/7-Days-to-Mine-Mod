package com.nuparu.sevendaystomine.crafting.forge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.item.EnumMaterial;

import net.minecraft.item.ItemStack;

public class ForgeResult {
	public boolean matches;
	@Nullable
	public HashMap<EnumMaterial, Integer> usedItems;
	int outputAmount = 0;
	
	public ForgeResult(boolean matches) {
		this.matches = matches;
		this.usedItems=null;
	}
	
	public ForgeResult(boolean matches, @Nullable HashMap<EnumMaterial, Integer> usedItems) {
		this.matches = matches;
		this.usedItems=usedItems;
	}
	public ForgeResult(boolean matches, @Nullable HashMap<EnumMaterial, Integer> usedItems, int outputAmount) {
		this.matches = matches;
		this.usedItems=usedItems;
		this.outputAmount = outputAmount;
	}
	
	
	public void simplify(ForgeRecipeMaterial recipe) {
		Iterator<Entry<EnumMaterial, Integer>> thisIterator = usedItems.entrySet().iterator();
		
		int factor = -1;
		
		while (thisIterator.hasNext()) {
			Entry<EnumMaterial, Integer> entry = thisIterator.next();
			EnumMaterial mat = entry.getKey();
			int weight = entry.getValue();
			
			//Weight from the basic recipe
			if(!recipe.ingredients.containsKey(mat)) return;
			int recipeWeight = recipe.ingredients.get(mat);
			
			//Checks if a whole number
			double d = (double)weight/recipeWeight;
			int i = (int) Math.round(d);
			if(i != d) return;
			
			if(factor!=-1 && factor != i) return;
			factor = i;		
		}
		//System.out.println("Factor " + factor);
		
		if(factor == -1) return;
		
		thisIterator = usedItems.entrySet().iterator();
		while (thisIterator.hasNext()) {
			Entry<EnumMaterial, Integer> entry = thisIterator.next();
			entry.setValue(entry.getValue()/factor);
			
			//System.out.println(entry.getKey() + " " + entry.getValue());
		}
		
		outputAmount/=factor;
		
	}
}
