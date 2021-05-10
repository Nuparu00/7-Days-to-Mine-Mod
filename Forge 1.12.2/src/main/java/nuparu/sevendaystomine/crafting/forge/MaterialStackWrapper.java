package nuparu.sevendaystomine.crafting.forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nuparu.sevendaystomine.item.EnumMaterial;

public class MaterialStackWrapper {

	EnumMaterial mat;
	int weight;
	
	public MaterialStackWrapper(EnumMaterial mat, int weight) {
		this.mat = mat;
		this.weight = weight;
	}

	public EnumMaterial getMaterial() {
		return mat;
	}
	
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MaterialStackWrapper)) return false;
		MaterialStackWrapper wrapper = (MaterialStackWrapper)obj;
		if(mat == wrapper.getMaterial()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "MaterialStackWrapper ["+mat.toString()+"]";
	}
	
	public static List<MaterialStackWrapper> wrapList(HashMap<EnumMaterial, Integer> mats, boolean perfect){
		List<MaterialStackWrapper> list = new ArrayList<MaterialStackWrapper>();
		for (Map.Entry<EnumMaterial, Integer> entry : mats.entrySet()) {
		    EnumMaterial enumMat = entry.getKey();
		    int i = entry.getValue();
		    list.add(new MaterialStackWrapper(enumMat,i));
		}
		return list;
	}
	
}