package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombieSoldier;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderZombieSoldierFactory implements IRenderFactory<EntityZombieSoldier> {

	public static final RenderZombieSoldierFactory INSTANCE = new RenderZombieSoldierFactory();
	@Override
	public Render<? super EntityZombieSoldier> createRenderFor(RenderManager manager) {
		return new RenderZombieSoldier(manager, new ModelBiped(),0.5f);
	}

}
