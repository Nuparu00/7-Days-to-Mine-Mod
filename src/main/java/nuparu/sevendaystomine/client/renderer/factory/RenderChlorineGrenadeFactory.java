package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderChlorineGreande;
import nuparu.sevendaystomine.entity.EntityChlorineGrenade;

public class RenderChlorineGrenadeFactory implements IRenderFactory<EntityChlorineGrenade> {

	public static final RenderChlorineGrenadeFactory INSTANCE = new RenderChlorineGrenadeFactory();

	@Override
	public Render<? super EntityChlorineGrenade> createRenderFor(RenderManager manager) {
		return new RenderChlorineGreande(manager);
	}

}
