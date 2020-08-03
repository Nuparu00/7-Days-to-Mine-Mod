package com.nuparu.sevendaystomine.client.renderer.entity;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityZombieCrawler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieCrawler extends RenderCorpse<EntityZombieCrawler>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/zombie_crawler.png");
	
	public RenderZombieCrawler(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		layerRenderers.removeIf(r -> r instanceof LayerZombieEyes);
	}
	
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityZombieCrawler entity) {
		return TEXTURE;
	}

}
