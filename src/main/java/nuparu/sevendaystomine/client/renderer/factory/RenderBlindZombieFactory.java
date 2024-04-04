package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderBlindZombie;
import nuparu.sevendaystomine.entity.EntityBlindZombie;

public class RenderBlindZombieFactory implements IRenderFactory<EntityBlindZombie> {

	public static final RenderBlindZombieFactory INSTANCE = new RenderBlindZombieFactory();
	@Override
	public Render<? super EntityBlindZombie> createRenderFor(RenderManager manager) {
		return new RenderBlindZombie(manager, new ModelZombie(),0.5f);
	}

}
