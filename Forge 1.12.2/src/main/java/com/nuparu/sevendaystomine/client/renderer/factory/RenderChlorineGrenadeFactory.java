package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderChlorineGreande;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFlame;
import com.nuparu.sevendaystomine.entity.EntityChlorineGrenade;
import com.nuparu.sevendaystomine.entity.EntityFlame;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderChlorineGrenadeFactory implements IRenderFactory<EntityChlorineGrenade> {

	public static final RenderChlorineGrenadeFactory INSTANCE = new RenderChlorineGrenadeFactory();

	@Override
	public Render<? super EntityChlorineGrenade> createRenderFor(RenderManager manager) {
		return new RenderChlorineGreande(manager);
	}

}
