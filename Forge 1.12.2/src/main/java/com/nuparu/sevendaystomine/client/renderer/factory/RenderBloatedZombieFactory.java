package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.model.ModelBloatedZombie;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderBloatedZombie;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderBloatedZombieFactory implements IRenderFactory<EntityBloatedZombie> {

	public static final RenderBloatedZombieFactory INSTANCE = new RenderBloatedZombieFactory();
	@Override
	public Render<? super EntityBloatedZombie> createRenderFor(RenderManager manager) {
		return new RenderBloatedZombie(manager, new ModelBloatedZombie(),0.75f);
	}

}
