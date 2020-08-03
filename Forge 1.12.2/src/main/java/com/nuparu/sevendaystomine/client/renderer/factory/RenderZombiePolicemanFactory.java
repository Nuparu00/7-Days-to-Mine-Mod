package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.model.ModelZombiePoliceman;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombiePoliceman;
import com.nuparu.sevendaystomine.entity.EntityZombiePoliceman;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderZombiePolicemanFactory implements IRenderFactory<EntityZombiePoliceman> {

	public static final RenderZombiePolicemanFactory INSTANCE = new RenderZombiePolicemanFactory();
	@Override
	public Render<? super EntityZombiePoliceman> createRenderFor(RenderManager manager) {
		return new RenderZombiePoliceman(manager, new ModelZombiePoliceman(),0.75f);
	}

}
