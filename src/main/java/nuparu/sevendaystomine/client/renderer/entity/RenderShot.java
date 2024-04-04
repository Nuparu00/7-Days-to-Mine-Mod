package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityShot;

@SideOnly(Side.CLIENT)
public class RenderShot extends Render<EntityShot>{

	public RenderShot(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0f;
	}

	@Override
	public void doRender(EntityShot entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityShot entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
