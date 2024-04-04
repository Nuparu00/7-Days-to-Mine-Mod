package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderCar;
import nuparu.sevendaystomine.entity.EntityCar;

public class RenderCarFactory implements IRenderFactory<EntityCar> {

	public static final RenderCarFactory INSTANCE = new RenderCarFactory();

	@Override
	public Render<? super EntityCar> createRenderFor(RenderManager manager) {
		return new RenderCar(manager);
	}

}
