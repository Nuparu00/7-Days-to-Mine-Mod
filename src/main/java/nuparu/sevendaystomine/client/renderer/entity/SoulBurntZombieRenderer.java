package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.renderer.entity.layers.BurnsLayer;
import nuparu.sevendaystomine.world.entity.zombie.SoulBurntZombieEntity;
import org.jetbrains.annotations.NotNull;

public class SoulBurntZombieRenderer<T extends SoulBurntZombieEntity, M extends HumanoidModel<T>>
		extends ZombieBipedRenderer<T, M> {

	public static final Material FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS,
			new ResourceLocation("block/soul_fire_0"));
	public static final Material FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS,
			new ResourceLocation("block/soul_fire_1"));
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/burnt/soul_burnt_zombie.png");
	public final BurnsLayer burnsLayer;

	public SoulBurntZombieRenderer(EntityRendererProvider.Context context) {
		super(context, ClientSetup.REANIMATED_CORPSE_LAYER);
		this.addLayer(burnsLayer = new BurnsLayer<T, M, M>(this,new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/zombie/burnt/soul_burnt_zombie_glow.png")));
	}

	public @NotNull ResourceLocation getTextureLocation(@NotNull T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected boolean isShaking(@NotNull T p_230495_1_) {
		return false;
	}

	@Override
	public void render(@NotNull T entity, float p_225623_2_, float p_225623_3_, @NotNull PoseStack matrix, @NotNull MultiBufferSource buffer,
                       int p_225623_6_) {
		super.render(entity, p_225623_2_, p_225623_3_, matrix, buffer, p_225623_6_);
		if(!entity.isAlive()) return;
		renderFlame(matrix, buffer, entity);
	}

	private void renderFlame(PoseStack p_114454_, MultiBufferSource p_114455_, Entity p_114456_) {

		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

		TextureAtlasSprite textureatlassprite = FIRE_0.sprite();
		TextureAtlasSprite textureatlassprite1 = FIRE_1.sprite();
		p_114454_.pushPose();
		float f = p_114456_.getBbWidth() * 1.4F;
		p_114454_.scale(f, f, f);
		float f1 = 0.5F;
		float f2 = 0.0F;
		float f3 = p_114456_.getBbHeight() / f;
		float f4 = 0.0F;
		p_114454_.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
		p_114454_.translate(0.0D, 0.0D, -0.3F + (float)((int)f3) * 0.02F);
		float f5 = 0.0F;
		int i = 0;
		VertexConsumer vertexconsumer = p_114455_.getBuffer(Sheets.cutoutBlockSheet());

		for(PoseStack.Pose posestack$pose = p_114454_.last(); f3 > 0.0F; ++i) {
			TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
			float f6 = textureatlassprite2.getU0();
			float f7 = textureatlassprite2.getV0();
			float f8 = textureatlassprite2.getU1();
			float f9 = textureatlassprite2.getV1();
			if (i / 2 % 2 == 0) {
				float f10 = f8;
				f8 = f6;
				f6 = f10;
			}

			fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
			fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
			fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f6, f7);
			fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f8, f7);
			f3 -= 0.45F;
			f4 -= 0.45F;
			f1 *= 0.9F;
			f5 += 0.03F;
		}

		p_114454_.popPose();
	}

	private static void fireVertex(PoseStack.Pose p_114415_, VertexConsumer p_114416_, float p_114417_, float p_114418_, float p_114419_, float p_114420_, float p_114421_) {
		p_114416_.vertex(p_114415_.pose(), p_114417_, p_114418_, p_114419_).color(255, 255, 255, 255).uv(p_114420_, p_114421_).overlayCoords(0, 10).uv2(240).normal(p_114415_.normal(), 0.0F, 1.0F, 0.0F).endVertex();
	}

}
