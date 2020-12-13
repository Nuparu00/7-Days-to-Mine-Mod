package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombieMiner;
import com.nuparu.sevendaystomine.entity.EntityZombieMiner;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderZombieMinerFactory implements IRenderFactory<EntityZombieMiner> {

	public static final RenderZombieMinerFactory INSTANCE = new RenderZombieMinerFactory();
	@Override
	public Render<? super EntityZombieMiner> createRenderFor(RenderManager manager) {
		return new RenderZombieMiner(manager, new ModelBiped(),0.5f);
	}

}
