package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderZombieMiner;
import nuparu.sevendaystomine.entity.EntityZombieMiner;

public class RenderZombieMinerFactory implements IRenderFactory<EntityZombieMiner> {

	public static final RenderZombieMinerFactory INSTANCE = new RenderZombieMinerFactory();
	@Override
	public Render<? super EntityZombieMiner> createRenderFor(RenderManager manager) {
		return new RenderZombieMiner(manager, new ModelBiped(),0.5f);
	}

}
