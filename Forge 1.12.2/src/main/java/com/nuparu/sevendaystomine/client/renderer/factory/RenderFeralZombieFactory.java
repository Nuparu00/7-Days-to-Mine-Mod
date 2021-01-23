package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.model.ModelFeralZombie;
import com.nuparu.sevendaystomine.client.model.ModelZombiePoliceman;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFeralZombie;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombiePoliceman;
import com.nuparu.sevendaystomine.entity.EntityFeralZombie;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFeralZombieFactory implements IRenderFactory<EntityFeralZombie> {

	public static final RenderFeralZombieFactory INSTANCE = new RenderFeralZombieFactory();
	@Override
	public Render<? super EntityFeralZombie> createRenderFor(RenderManager manager) {
		return new RenderFeralZombie(manager, new ModelFeralZombie(),0.75f);
	}

}
