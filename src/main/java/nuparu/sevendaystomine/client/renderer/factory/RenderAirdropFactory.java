package nuparu.sevendaystomine.client.renderer.factory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.client.renderer.entity.RenderAirdrop;
import nuparu.sevendaystomine.entity.EntityAirdrop;

public class RenderAirdropFactory implements IRenderFactory<EntityAirdrop> {

	public static final RenderAirdropFactory INSTANCE = new RenderAirdropFactory();

	@Override
	public Render<? super EntityAirdrop> createRenderFor(RenderManager manager) {
		return new RenderAirdrop(manager);
	}

}
