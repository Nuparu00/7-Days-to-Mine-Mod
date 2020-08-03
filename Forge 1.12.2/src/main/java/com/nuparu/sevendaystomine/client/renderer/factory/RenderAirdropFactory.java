package com.nuparu.sevendaystomine.client.renderer.factory;

import com.nuparu.sevendaystomine.client.renderer.entity.RenderAirdrop;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderAirdropFactory implements IRenderFactory<EntityAirdrop> {

	public static final RenderAirdropFactory INSTANCE = new RenderAirdropFactory();

	@Override
	public Render<? super EntityAirdrop> createRenderFor(RenderManager manager) {
		return new RenderAirdrop(manager);
	}

}
