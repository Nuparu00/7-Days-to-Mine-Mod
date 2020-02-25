package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderInfectedSurvivor;
import com.nuparu.sevendaystomine.entity.EntityInfectedSurvivor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderInfectedSurvivorFactory implements IRenderFactory<EntityInfectedSurvivor> {

	public static final RenderInfectedSurvivorFactory INSTANCE = new RenderInfectedSurvivorFactory();
	@Override
	public Render<? super EntityInfectedSurvivor> createRenderFor(RenderManager manager) {
		return new RenderInfectedSurvivor(manager, new ModelBiped(),0.5f);
	}

}
