package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityZombieBase;

@SideOnly(Side.CLIENT)
public abstract class RenderCorpse<T extends EntityZombieBase> extends RenderLiving<T> {
	private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");
	public ModelBase model = new ModelBiped();
	protected float field_77070_b;

	public LayerZombieEyes<T> eyes;

	public RenderCorpse(RenderManager manager, ModelBase model, float shadowSize) {
		this(manager, model, shadowSize, 1.0F);
		if (model instanceof ModelBase) {
			this.addLayer(new LayerHeldItem(this));
		}
		eyes = new LayerZombieEyes<T>(this);
		this.addLayer(eyes);

		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this);
		this.addLayer(layerbipedarmor);
	}

	public RenderCorpse(RenderManager manager, ModelBase model, float shadowSize, float p_i46169_4_) {
		super(manager, model, shadowSize);
		this.model = model;
		this.field_77070_b = p_i46169_4_;
		if (model instanceof ModelBiped) {
			this.addLayer(new LayerCustomHead(((ModelBiped) model).bipedHead));
		}
		eyes = new LayerZombieEyes<T>(this);
		this.addLayer(eyes);

		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this);
		this.addLayer(layerbipedarmor);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return DEFAULT_RES_LOC;
	}

	public void func_82422_c() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(T entity) {
		return DEFAULT_RES_LOC;
	}

}