package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderZombieSoldier;
import nuparu.sevendaystomine.entity.EntityZombieSoldier;

public class RenderZombieSoldierFactory implements IRenderFactory<EntityZombieSoldier> {

	public static final RenderZombieSoldierFactory INSTANCE = new RenderZombieSoldierFactory();
	@Override
	public Render<? super EntityZombieSoldier> createRenderFor(RenderManager manager) {
		return new RenderZombieSoldier(manager, new ModelBiped(),0.5f);
	}

}
