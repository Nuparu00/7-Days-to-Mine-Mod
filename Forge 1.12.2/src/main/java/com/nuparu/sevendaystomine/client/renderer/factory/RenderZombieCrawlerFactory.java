package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.model.ModelZombieCrawler;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombieCrawler;
import com.nuparu.sevendaystomine.entity.EntityZombieCrawler;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderZombieCrawlerFactory implements IRenderFactory<EntityZombieCrawler> {

	public static final RenderZombieCrawlerFactory INSTANCE = new RenderZombieCrawlerFactory();
	@Override
	public Render<? super EntityZombieCrawler> createRenderFor(RenderManager manager) {
		return new RenderZombieCrawler(manager, new ModelZombieCrawler(),0.5f);
	}

}
