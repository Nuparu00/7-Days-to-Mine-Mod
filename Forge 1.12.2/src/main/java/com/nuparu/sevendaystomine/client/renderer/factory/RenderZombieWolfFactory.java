package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.model.ModelZombieWolf;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombieWolf;
import com.nuparu.sevendaystomine.entity.EntityZombieWolf;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderZombieWolfFactory implements IRenderFactory<EntityZombieWolf> {

	public static final RenderZombieWolfFactory INSTANCE = new RenderZombieWolfFactory();

	@Override
	public Render<? super EntityZombieWolf> createRenderFor(RenderManager manager) {
		return new RenderZombieWolf(manager, new ModelZombieWolf(), 0.5f);
	}

}
