package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.model.ModelFeralZombie;
import nuparu.sevendaystomine.client.renderer.entity.RenderFeralZombie;
import nuparu.sevendaystomine.entity.EntityFeralZombie;

public class RenderFeralZombieFactory implements IRenderFactory<EntityFeralZombie> {

	public static final RenderFeralZombieFactory INSTANCE = new RenderFeralZombieFactory();
	@Override
	public Render<? super EntityFeralZombie> createRenderFor(RenderManager manager) {
		return new RenderFeralZombie(manager, new ModelFeralZombie(),0.75f);
	}

}
