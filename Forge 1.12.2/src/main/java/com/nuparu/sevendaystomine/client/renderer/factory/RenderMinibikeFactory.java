package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderMinibike;
import com.nuparu.sevendaystomine.entity.EntityMinibike;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMinibikeFactory implements IRenderFactory<EntityMinibike> {

	public static final RenderMinibikeFactory INSTANCE = new RenderMinibikeFactory();

	@Override
	public Render<? super EntityMinibike> createRenderFor(RenderManager manager) {
		return new RenderMinibike(manager);
	}

}
