package com.nuparu.sevendaystomine.client.renderer.entity;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityBurntZombie;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderBurntZombie extends RenderCorpse<EntityBurntZombie> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/burnt_zombie.png");

	public RenderBurntZombie(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerBurntGlow<EntityBurntZombie>(this));
	}

	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityBurntZombie entity) {
		return TEXTURE;
	}
}
