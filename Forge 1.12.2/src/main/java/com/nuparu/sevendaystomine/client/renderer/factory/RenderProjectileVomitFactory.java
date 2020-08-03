package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderProjectileVomit;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderZombieSoldier;
import com.nuparu.sevendaystomine.entity.EntityProjectileVomit;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderProjectileVomitFactory implements IRenderFactory<EntityProjectileVomit> {

	public static final RenderProjectileVomitFactory INSTANCE = new RenderProjectileVomitFactory();
	@Override
	public Render<? super EntityProjectileVomit> createRenderFor(RenderManager manager) {
		return new RenderProjectileVomit(manager);
	}

}
