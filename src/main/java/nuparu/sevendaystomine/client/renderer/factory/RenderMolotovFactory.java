package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.EntityMolotov;
import nuparu.sevendaystomine.init.ModItems;

public class RenderMolotovFactory implements IRenderFactory<EntityMolotov> {

	public static final RenderMolotovFactory INSTANCE = new RenderMolotovFactory();

	@Override
	public Render<? super EntityMolotov> createRenderFor(RenderManager manager) {
		return new RenderSnowball(manager, ModItems.MOLOTOV, Minecraft.getMinecraft().getRenderItem());
	}

}
