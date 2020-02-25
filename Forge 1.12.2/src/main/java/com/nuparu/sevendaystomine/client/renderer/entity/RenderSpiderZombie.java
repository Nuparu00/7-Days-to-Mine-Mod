package com.nuparu.sevendaystomine.client.renderer.entity;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntitySpiderZombie;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderSpiderZombie extends RenderCorpse<EntitySpiderZombie>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/spider_zombie.png");
	
	public RenderSpiderZombie(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}
	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpiderZombie entity) {
		return TEXTURE;
	}

}
