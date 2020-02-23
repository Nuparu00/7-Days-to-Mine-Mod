package com.nuparu.sevendaystomine.capability;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class LockedRecipe implements ILockedRecipe {

	public HashMap<String, Boolean> recipes = new HashMap<String, Boolean>();

	public void setLock(String name, boolean lock) {
		recipes.put(name, (Boolean) lock);
	}

	public boolean isLocked(String name) {
		return recipes.get(name);
	}

	public void copy(ILockedRecipe recipe) {
		this.recipes = recipe.getRecipes();
	}

	public HashMap<String, Boolean> getRecipes() {
		return recipes;
	}

	public static class Factory implements Callable<ILockedRecipe> {

		@Override
		public ILockedRecipe call() throws Exception {
			return new LockedRecipe();
		}
	}

}