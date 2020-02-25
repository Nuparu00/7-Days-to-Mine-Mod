package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderBlindZombie;
import com.nuparu.sevendaystomine.entity.EntityBlindZombie;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderBlindZombieFactory implements IRenderFactory<EntityBlindZombie> {

	public static final RenderBlindZombieFactory INSTANCE = new RenderBlindZombieFactory();
	@Override
	public Render<? super EntityBlindZombie> createRenderFor(RenderManager manager) {
		return new RenderBlindZombie(manager, new ModelZombie(),0.5f);
	}

}
