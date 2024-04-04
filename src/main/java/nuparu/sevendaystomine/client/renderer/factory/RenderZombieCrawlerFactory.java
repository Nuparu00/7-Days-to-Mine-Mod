package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.model.ModelZombieCrawler;
import nuparu.sevendaystomine.client.renderer.entity.RenderZombieCrawler;
import nuparu.sevendaystomine.entity.EntityZombieCrawler;

public class RenderZombieCrawlerFactory implements IRenderFactory<EntityZombieCrawler> {

	public static final RenderZombieCrawlerFactory INSTANCE = new RenderZombieCrawlerFactory();
	@Override
	public Render<? super EntityZombieCrawler> createRenderFor(RenderManager manager) {
		return new RenderZombieCrawler(manager, new ModelZombieCrawler(),0.5f);
	}

}
