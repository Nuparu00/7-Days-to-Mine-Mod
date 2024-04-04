package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.model.ModelBloatedZombie;
import nuparu.sevendaystomine.client.renderer.entity.RenderBloatedZombie;
import nuparu.sevendaystomine.entity.EntityBloatedZombie;

public class RenderBloatedZombieFactory implements IRenderFactory<EntityBloatedZombie> {

	public static final RenderBloatedZombieFactory INSTANCE = new RenderBloatedZombieFactory();
	@Override
	public Render<? super EntityBloatedZombie> createRenderFor(RenderManager manager) {
		return new RenderBloatedZombie(manager, new ModelBloatedZombie(),0.75f);
	}

}
