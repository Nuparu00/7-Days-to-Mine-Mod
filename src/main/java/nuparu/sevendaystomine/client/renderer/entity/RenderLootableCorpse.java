package nuparu.sevendaystomine.client.renderer.entity;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.entity.EntityLootableCorpse;
import nuparu.sevendaystomine.entity.EntityZombieBase;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class RenderLootableCorpse<T extends EntityLootableCorpse> extends Render<T> {

	public RenderLootableCorpse(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Entity original = entity.getOriginal();
		if (original == null)
			return;

		Field f = ReflectionHelper.findField(RenderingRegistry.class, "INSTANCE");
		f.setAccessible(true);
		Object obj = null;
		try {
			obj = f.get(null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (obj == null)
			return;

		RenderingRegistry registry = (RenderingRegistry) obj;

		Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenderers = null;
		Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderersOld = null;

		entityRenderers = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) ReflectionHelper
				.getPrivateValue(RenderingRegistry.class, registry, "entityRenderers");
		entityRenderersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) ReflectionHelper
				.getPrivateValue(RenderingRegistry.class, registry, "entityRenderersOld");

		Render render = null;

		for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : RenderUtils.entityRenderers
				.entrySet()) {
			Class<? extends Entity> clazz = entry.getKey();
			if (clazz == original.getClass()) {
				render = entry.getValue();
				break;
			}
		}
		if (render == null) {
			for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenderers
					.entrySet()) {
				Class<? extends Entity> clazz = entry.getKey();
				if (clazz == original.getClass()) {
					render = entry.getValue().createRenderFor(renderManager);
					RenderUtils.entityRenderers.put(clazz, render);
					break;
				}
			}

			if (render == null) {
				for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRenderersOld
						.entrySet()) {
					Class<? extends Entity> clazz = entry.getKey();
					if (clazz == original.getClass()) {
						render = entry.getValue();
						RenderUtils.entityRenderers.put(clazz, render);
						break;
					}
				}
			}
		}

		if (render == null) {
			return;
		}

		float new_x = (float) (x + (original.height / 2f) * Math.cos((entityYaw + 90) * Math.PI / 180));
		float new_y = (float) (y + (original.width / 4f));
		float new_z = (float) (z + (original.height / 2f) * Math.sin((entityYaw + 90) * Math.PI / 180));

		float rotX = -1;
		float rotY = 0;
		float rotZ = 0;

		if (original instanceof EntityZombieBase) {
			EntityZombieBase zombie = ((EntityZombieBase) original);
			if (zombie.customCoprseTransform()) {
				Vec3d rot = zombie.corpseRotation();
				rotX += (float) rot.x;
				rotY += (float) rot.y;
				rotZ += (float) rot.z;
				Vec3d pos = zombie.corpseTranslation();
				new_x = (float) (x + pos.x * Math.cos((entityYaw + 90) * Math.PI / 180));
				new_y += pos.y;
				new_z = (float) (z + pos.z * Math.sin((entityYaw + 90) * Math.PI / 180));
			}
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(new_x, new_y, new_z);
		GlStateManager.rotate(entityYaw, 0, -1, 0);
		GlStateManager.rotate(90, rotX, rotY, rotZ);

		render.doRender(original, 0, 0, 0, entityYaw, partialTicks);
		GlStateManager.popMatrix();
		
	}

}
