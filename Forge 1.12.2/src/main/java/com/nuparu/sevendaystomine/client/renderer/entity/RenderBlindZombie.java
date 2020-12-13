package com.nuparu.sevendaystomine.client.renderer.entity;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityBlindZombie;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlindZombie extends RenderCorpse<EntityBlindZombie> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/blind_zombie.png");
	private static final ResourceLocation EYES = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/bloated_zombie_eyes.png");

	public RenderBlindZombie(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		this.layerRenderers.remove(eyes);

	}

	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityBlindZombie entity) {
		return TEXTURE;
	}

}
