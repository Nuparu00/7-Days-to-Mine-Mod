package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityBurntZombie;

@SideOnly(Side.CLIENT)
public class RenderBurntZombie extends RenderCorpse<EntityBurntZombie> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/burnt_zombie.png");

	public RenderBurntZombie(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerBurntGlow<EntityBurntZombie>(this));
	}

	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return TEXTURE;
	}

	protected ResourceLocation getEntityTexture(EntityBurntZombie entity) {
		return TEXTURE;
	}
}
