package com.nuparu.sevendaystomine.util.computer.application;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;

import net.minecraft.util.ResourceLocation;

public class Application {

	public final ResourceLocation ICON;
	public final String name;
	public ResourceLocation key;
	public String processName;
	//public final String path;
	//public final String author;
	//public final String createdOn;
	
	public Application(ResourceLocation icon, String name) {
		this.ICON = icon;
		this.name = name;
		this.processName = name;
	}
	
	
	public void open(TileEntityComputer te) {
		
	}
	
	public String getUnlocalizedName() {
		return "computer.app."+name;
	}
	
	public String getUnlocalizedDesc() {
		return getUnlocalizedName()+".desc";
	}
	
	public String getLocalizedName() {
		return SevenDaysToMine.proxy.localize(getUnlocalizedName());
	}
	
	public String getLocalizedDesc() {
		return SevenDaysToMine.proxy.localize(getUnlocalizedDesc());
	}
}
