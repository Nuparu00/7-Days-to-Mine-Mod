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
import nuparu.sevendaystomine.world.entity.item.MountableBlockEntity;
import nuparu.sevendaystomine.world.entity.zombie.ZombieBaseEntity;

import java.util.Map;

public class MountableBlockRenderer<T extends MountableBlockEntity> extends EntityRenderer<T> {

	public MountableBlockRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack matrix,
					   MultiBufferSource buffer, int p_225623_6_) {
	}

	@Override
	public ResourceLocation getTextureLocation(T p_114482_) {
		return null;
	}

}
