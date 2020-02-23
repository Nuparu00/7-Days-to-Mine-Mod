package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderHuman;
import com.nuparu.sevendaystomine.entity.EntitySurvivor;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHumanFactory implements IRenderFactory<EntitySurvivor> {

	public static final RenderHumanFactory INSTANCE = new RenderHumanFactory();
	@Override
	public Render<? super EntitySurvivor> createRenderFor(RenderManager manager) {
		return new RenderHuman(manager);
	}

}
