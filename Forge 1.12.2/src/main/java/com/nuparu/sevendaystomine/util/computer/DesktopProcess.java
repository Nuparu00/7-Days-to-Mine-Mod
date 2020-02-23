package com.nuparu.sevendaystomine.util.computer;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class DesktopProcess extends TickingProcess {

	public ArrayList<ResourceLocation> processQueue = new ArrayList<ResourceLocation>();
	
	public DesktopProcess() {
		super();
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		
	}
}
