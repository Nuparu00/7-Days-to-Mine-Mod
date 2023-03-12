package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.world.entity.item.MountableBlockEntity;
import org.jetbrains.annotations.NotNull;

public class MountableBlockRenderer<T extends MountableBlockEntity> extends EntityRenderer<T> {

	public MountableBlockRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack matrix,
                       @NotNull MultiBufferSource buffer, int p_225623_6_) {
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T p_114482_) {
		return null;
	}

}
