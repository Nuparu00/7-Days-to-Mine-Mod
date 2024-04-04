package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderFlame;
import nuparu.sevendaystomine.entity.EntityFlame;

public class RenderFlameFactory implements IRenderFactory<EntityFlame> {

	public static final RenderFlameFactory INSTANCE = new RenderFlameFactory();

	@Override
	public Render<? super EntityFlame> createRenderFor(RenderManager manager) {
		return new RenderFlame(manager);
	}

}
