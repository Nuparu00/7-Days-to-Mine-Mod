package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderHuman;
import nuparu.sevendaystomine.entity.EntityHuman;

public class RenderHumanFactory implements IRenderFactory<EntityHuman> {

	public static final RenderHumanFactory INSTANCE = new RenderHumanFactory();
	@Override
	public Render<? super EntityHuman> createRenderFor(RenderManager manager) {
		return new RenderHuman(manager);
	}

}
