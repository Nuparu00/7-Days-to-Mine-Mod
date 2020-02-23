package com.nuparu.sevendaystomine.util;

import net.minecraft.item.Item;

import java.util.HashMap;

import java.util.Map;

import com.nuparu.sevendaystomine.item.EnumMaterial;

public class ItemUtils {

	public static ItemUtils INSTANCE = new ItemUtils();
	
	public HashMap<EnumMaterial, Item> scrapResults = new HashMap<EnumMaterial, Item>();

	public void addScrapResult(EnumMaterial mat, Item item) {
		scrapResults.put(mat, item);
	}

	public Item getScrapResult(EnumMaterial mat) {
		for (Map.Entry<EnumMaterial, Item> entry : scrapResults.entrySet()) {
			if (mat == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}
}