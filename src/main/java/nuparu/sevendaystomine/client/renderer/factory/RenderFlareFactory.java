package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.EntityFlare;
import nuparu.sevendaystomine.init.ModItems;

public class RenderFlareFactory implements IRenderFactory<EntityFlare> {

	public static final RenderFlareFactory INSTANCE = new RenderFlareFactory();

	@Override
	public Render<? super EntityFlare> createRenderFor(RenderManager manager) {
		return new RenderSnowball(manager, ModItems.FLARE, Minecraft.getMinecraft().getRenderItem());
	}

}
