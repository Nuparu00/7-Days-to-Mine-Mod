package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.entity.EntityFlare;
import com.nuparu.sevendaystomine.entity.EntityMolotov;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFlareFactory implements IRenderFactory<EntityFlare> {

	public static final RenderFlareFactory INSTANCE = new RenderFlareFactory();

	@Override
	public Render<? super EntityFlare> createRenderFor(RenderManager manager) {
		return new RenderSnowball(manager, ModItems.FLARE, Minecraft.getMinecraft().getRenderItem());
	}

}
