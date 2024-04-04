package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderZombiePig;
import nuparu.sevendaystomine.entity.EntityZombiePig;

public class RenderZombiePigFactory implements IRenderFactory<EntityZombiePig> {

	public static final RenderZombiePigFactory INSTANCE = new RenderZombiePigFactory();

	@Override
	public Render<? super EntityZombiePig> createRenderFor(RenderManager manager) {
		return new RenderZombiePig(manager, new ModelPig(), 0.5f);
	}

}
