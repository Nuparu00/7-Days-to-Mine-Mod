package com.nuparu.sevendaystomine.client.renderer.entity;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;
import com.nuparu.sevendaystomine.entity.EntityReanimatedCorpse;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderBloatedZombie extends RenderCorpse<EntityBloatedZombie>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/bloated_zombie.png");
	
	public RenderBloatedZombie(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}
	
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityBloatedZombie entity) {
		return TEXTURE;
	}

}
