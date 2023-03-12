package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import nuparu.sevendaystomine.world.entity.item.LootableCorpseEntity;
import nuparu.sevendaystomine.world.entity.zombie.ZombieBaseEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LootableCorpseRenderer<T extends LootableCorpseEntity> extends EntityRenderer<T> {

	public LootableCorpseRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack matrix,
                       @NotNull MultiBufferSource buffer, int p_225623_6_) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, p_225623_6_);
		Entity original = entity.getOriginal();
		if (original == null)
			return;




		EntityRenderer renderer = null;

		for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
			EntityType<?> type = entry.getKey();
			if (type == original.getType()) {
				renderer = entry.getValue();
				break;
			}
		}
		if (renderer == null) {
			return;
		}
		
		float new_x = (float) ((original.getBbHeight() / 2f) * Math.cos((entityYaw + 90) * Math.PI / 180));
		float new_y = (original.getBbWidth() / 4f);
		float new_z = (float) ((original.getBbHeight() / 2f) * Math.sin((entityYaw + 90) * Math.PI / 180));

		float rotX = -90;
		float rotY = entityYaw;
		float rotZ = 0;

		if (original instanceof ZombieBaseEntity zombie) {

			if (zombie.customCorpseTransform()) {
				Vec3 rot = zombie.corpseRotation();
				rotX+=rot.x;
				rotY+=rot.y;
				rotZ+=rot.z;

				new_y += zombie.corpseTranslation().y;
			}
		}
		matrix.pushPose();
		matrix.translate(-new_x, new_y, new_z);
		matrix.mulPose(Vector3f.YP.rotationDegrees(rotY));
		matrix.pushPose();
		matrix.mulPose(Vector3f.ZP.rotationDegrees(rotZ));
		matrix.mulPose(Vector3f.XP.rotationDegrees(rotX));
		renderer.render(original, entityYaw, partialTicks, matrix,buffer,p_225623_6_);
		matrix.popPose();
		matrix.popPose();
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T p_114482_) {
		return null;
	}

}
