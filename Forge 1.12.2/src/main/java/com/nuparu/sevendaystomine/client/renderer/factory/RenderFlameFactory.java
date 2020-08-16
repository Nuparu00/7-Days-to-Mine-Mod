package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderFlame;
import com.nuparu.sevendaystomine.entity.EntityFlame;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFlameFactory implements IRenderFactory<EntityFlame> {

	public static final RenderFlameFactory INSTANCE = new RenderFlameFactory();

	@Override
	public Render<? super EntityFlame> createRenderFor(RenderManager manager) {
		return new RenderFlame(manager);
	}

}
