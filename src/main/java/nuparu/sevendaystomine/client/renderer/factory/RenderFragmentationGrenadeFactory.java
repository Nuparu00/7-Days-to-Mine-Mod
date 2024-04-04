package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderFragmentationGrenade;
import nuparu.sevendaystomine.entity.EntityFragmentationGrenade;

public class RenderFragmentationGrenadeFactory implements IRenderFactory<EntityFragmentationGrenade> {

	public static final RenderFragmentationGrenadeFactory INSTANCE = new RenderFragmentationGrenadeFactory();

	@Override
	public Render<? super EntityFragmentationGrenade> createRenderFor(RenderManager manager) {
		return new RenderFragmentationGrenade(manager);
	}

}
