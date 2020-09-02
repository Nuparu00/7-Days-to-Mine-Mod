package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderAirplane;
import com.nuparu.sevendaystomine.entity.EntityAirplane;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderAirplaneFactory implements IRenderFactory<EntityAirplane> {

	public static final RenderAirplaneFactory INSTANCE = new RenderAirplaneFactory();

	@Override
	public Render<? super EntityAirplane> createRenderFor(RenderManager manager) {
		return new RenderAirplane(manager);
	}

}
