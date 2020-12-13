package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderChlorineGreande;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFlame;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFragmentationGrenade;
import com.nuparu.sevendaystomine.entity.EntityChlorineGrenade;
import com.nuparu.sevendaystomine.entity.EntityFlame;
import com.nuparu.sevendaystomine.entity.EntityFragmentationGrenade;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFragmentationGrenadeFactory implements IRenderFactory<EntityFragmentationGrenade> {

	public static final RenderFragmentationGrenadeFactory INSTANCE = new RenderFragmentationGrenadeFactory();

	@Override
	public Render<? super EntityFragmentationGrenade> createRenderFor(RenderManager manager) {
		return new RenderFragmentationGrenade(manager);
	}

}
