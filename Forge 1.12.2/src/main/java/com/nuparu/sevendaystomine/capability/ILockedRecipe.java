package com.nuparu.sevendaystomine.capability;

import java.util.HashMap;

public interface ILockedRecipe {

	public void setLock(String name, boolean lock);

	public boolean isLocked(String name);

	public void copy(ILockedRecipe recipe);

	public HashMap<String, Boolean> getRecipes();
}