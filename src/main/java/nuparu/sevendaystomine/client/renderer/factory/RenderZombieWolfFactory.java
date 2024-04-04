package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.model.ModelZombieWolf;
import nuparu.sevendaystomine.client.renderer.entity.RenderZombieWolf;
import nuparu.sevendaystomine.entity.EntityZombieWolf;

public class RenderZombieWolfFactory implements IRenderFactory<EntityZombieWolf> {

	public static final RenderZombieWolfFactory INSTANCE = new RenderZombieWolfFactory();

	@Override
	public Render<? super EntityZombieWolf> createRenderFor(RenderManager manager) {
		return new RenderZombieWolf(manager, new ModelZombieWolf(), 0.5f);
	}

}
