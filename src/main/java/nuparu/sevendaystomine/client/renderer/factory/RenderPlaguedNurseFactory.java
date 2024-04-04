package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderPlaguedNurse;
import nuparu.sevendaystomine.entity.EntityPlaguedNurse;

public class RenderPlaguedNurseFactory implements IRenderFactory<EntityPlaguedNurse> {

	public static final RenderPlaguedNurseFactory INSTANCE = new RenderPlaguedNurseFactory();
	@Override
	public Render<? super EntityPlaguedNurse> createRenderFor(RenderManager manager) {
		return new RenderPlaguedNurse(manager, new ModelBiped(),0.5f);
	}

}
