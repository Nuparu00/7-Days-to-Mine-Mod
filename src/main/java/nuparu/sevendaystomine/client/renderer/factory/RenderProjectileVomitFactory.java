package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.EntityProjectileVomit;
import nuparu.sevendaystomine.init.ModItems;

public class RenderProjectileVomitFactory implements IRenderFactory<EntityProjectileVomit> {

	public static final RenderProjectileVomitFactory INSTANCE = new RenderProjectileVomitFactory();
	@Override
	public Render<? super EntityProjectileVomit> createRenderFor(RenderManager manager) {
		return  new RenderSnowball(manager, ModItems.VOMIT, Minecraft.getMinecraft().getRenderItem());
	}

}
