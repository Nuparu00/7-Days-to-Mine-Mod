package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderInfectedSurvivor;
import nuparu.sevendaystomine.entity.EntityInfectedSurvivor;

public class RenderInfectedSurvivorFactory implements IRenderFactory<EntityInfectedSurvivor> {

	public static final RenderInfectedSurvivorFactory INSTANCE = new RenderInfectedSurvivorFactory();
	@Override
	public Render<? super EntityInfectedSurvivor> createRenderFor(RenderManager manager) {
		return new RenderInfectedSurvivor(manager, new ModelBiped(),0.5f);
	}

}
