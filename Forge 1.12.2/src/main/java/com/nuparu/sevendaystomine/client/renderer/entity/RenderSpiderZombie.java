package com.nuparu.sevendaystomine.client.renderer.entity;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;
import com.nuparu.sevendaystomine.entity.EntitySpiderZombie;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderSpiderZombie extends RenderCorpse<EntitySpiderZombie>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/spider_zombie.png");
	private static final ResourceLocation EYES = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/spider_zombie_eyes.png");
	
	public RenderSpiderZombie(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		this.layerRenderers.remove(eyes);
		
		for(LayerRenderer<?> layer : this.layerRenderers) {
			if(layer instanceof LayerZombieEyes) {
				this.layerRenderers.remove(layer);
				break;
			}
		}
		eyes = new LayerZombieEyes<EntitySpiderZombie>(this,EYES);
		this.addLayer(eyes);
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
