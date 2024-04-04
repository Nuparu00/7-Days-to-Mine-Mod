package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityZombieCrawler;

@SideOnly(Side.CLIENT)
public class RenderZombieCrawler extends RenderCorpse<EntityZombieCrawler>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/zombie_crawler.png");
	private static final ResourceLocation EYES = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/crawler_zombie_eyes.png");
	
	public RenderZombieCrawler(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		layerRenderers.removeIf(r -> r instanceof LayerZombieEyes);

		
		for(LayerRenderer<?> layer : this.layerRenderers) {
			if(layer instanceof LayerZombieEyes) {
				this.layerRenderers.remove(layer);
				break;
			}
		}
		eyes = new LayerZombieEyes<EntityZombieCrawler>(this,EYES);
		this.addLayer(eyes);
	}
	
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityZombieCrawler entity) {
		return TEXTURE;
	}

}
