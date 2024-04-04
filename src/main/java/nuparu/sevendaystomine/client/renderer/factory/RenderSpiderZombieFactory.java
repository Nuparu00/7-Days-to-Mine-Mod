package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.model.ModelSpiderZombie;
import nuparu.sevendaystomine.client.renderer.entity.RenderSpiderZombie;
import nuparu.sevendaystomine.entity.EntitySpiderZombie;

public class RenderSpiderZombieFactory implements IRenderFactory<EntitySpiderZombie> {

	public static final RenderSpiderZombieFactory INSTANCE = new RenderSpiderZombieFactory();
	@Override
	public Render<? super EntitySpiderZombie> createRenderFor(RenderManager manager) {
		return new RenderSpiderZombie(manager, new ModelSpiderZombie(),0.5f);
	}

}
