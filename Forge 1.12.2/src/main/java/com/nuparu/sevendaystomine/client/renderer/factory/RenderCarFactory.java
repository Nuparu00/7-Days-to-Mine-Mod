package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderCar;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderMinibike;
import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.entity.EntityMinibike;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCarFactory implements IRenderFactory<EntityCar> {

	public static final RenderCarFactory INSTANCE = new RenderCarFactory();

	@Override
	public Render<? super EntityCar> createRenderFor(RenderManager manager) {
		return new RenderCar(manager);
	}

}
