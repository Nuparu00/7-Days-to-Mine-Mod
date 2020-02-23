package com.nuparu.sevendaystomine.client.renderer.entity;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityFrozenLumberjack;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderFrozenLumberjack extends RenderCorpse<EntityFrozenLumberjack>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/frozen_lumberjack.png");
	
	public RenderFrozenLumberjack(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}
	
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityFrozenLumberjack entity) {
		return TEXTURE;
	}

}
