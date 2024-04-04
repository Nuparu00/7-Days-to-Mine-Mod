package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderAirplane;
import nuparu.sevendaystomine.entity.EntityAirplane;

public class RenderAirplaneFactory implements IRenderFactory<EntityAirplane> {

	public static final RenderAirplaneFactory INSTANCE = new RenderAirplaneFactory();

	@Override
	public Render<? super EntityAirplane> createRenderFor(RenderManager manager) {
		return new RenderAirplane(manager);
	}

}
