package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.model.ModelZombiePoliceman;
import nuparu.sevendaystomine.client.renderer.entity.RenderZombiePoliceman;
import nuparu.sevendaystomine.entity.EntityZombiePoliceman;

public class RenderZombiePolicemanFactory implements IRenderFactory<EntityZombiePoliceman> {

	public static final RenderZombiePolicemanFactory INSTANCE = new RenderZombiePolicemanFactory();
	@Override
	public Render<? super EntityZombiePoliceman> createRenderFor(RenderManager manager) {
		return new RenderZombiePoliceman(manager, new ModelZombiePoliceman(),0.75f);
	}

}
