package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityHuman;
import nuparu.sevendaystomine.entity.EntityHuman.EnumSex;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class RenderHuman extends RenderLivingBase<EntityHuman> {
	private static final ModelPlayer modelSteve = new ModelPlayer(0.0F, false);
	private static final ModelPlayer modelAlex = new ModelPlayer(0.0F, true);

	public RenderHuman(RenderManager renderManager) {
		this(renderManager, false);
	}

	public RenderHuman(RenderManager renderManager, boolean useSmallArms) {
		super(renderManager, modelSteve, 0.5F);
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItemHuman(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
		this.addLayer(new LayerElytra(this));
	}

	public ModelPlayer getMainModel() {
		return (ModelPlayer) super.getMainModel();
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityHuman entity, double x, double y, double z, float entityYaw, float partialTicks) {
		double d0 = y;

		ModelPlayer model = getModelForEntity(entity);

		if (entity.isSneaking()) {
			d0 = y - 0.125D;
		}

		this.setModelVisibilities(entity);
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<EntityHuman>(entity, this, partialTicks,
						x, y, z)))
			return;
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		model.swingProgress = this.getSwingProgress(entity, partialTicks);
		boolean shouldSit = entity.isRiding()
				&& (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
		model.isRiding = shouldSit;
		model.isChild = entity.isChild();

		try {
			float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float f2 = f1 - f;

			if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
				f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset,
						partialTicks);
				f2 = f1 - f;
				float f3 = MathHelper.wrapDegrees(f2);

				if (f3 < -85.0F) {
					f3 = -85.0F;
				}

				if (f3 >= 85.0F) {
					f3 = 85.0F;
				}

				f = f1 - f3;

				if (f3 * f3 > 2500.0F) {
					f += f3 * 0.2F;
				}

				f2 = f1 - f;
			}

			float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			this.renderLivingAt(entity, x, y, z);
			float f8 = this.handleRotationFloat(entity, partialTicks);
			this.applyRotations(entity, f8, f, partialTicks);
			float f4 = this.prepareScale(entity, partialTicks);
			float f5 = 0.0F;
			float f6 = 0.0F;

			if (!entity.isRiding()) {
				f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
				f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

				if (entity.isChild()) {
					f6 *= 3.0F;
				}

				if (f5 > 1.0F) {
					f5 = 1.0F;
				}
				f2 = f1 - f; // Forge: Fix MC-1207
			}

			GlStateManager.enableAlpha();
			model.setLivingAnimations(entity, f6, f5, partialTicks);
			model.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

			if (this.renderOutlines) {
				boolean flag1 = this.setScoreTeamColor(entity);
				GlStateManager.enableColorMaterial();
				GlStateManager.enableOutlineMode(this.getTeamColor(entity));

				if (!this.renderMarker) {
					this.renderModel(entity, f6, f5, f8, f2, f7, f4);
				}

				this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);

				GlStateManager.disableOutlineMode();
				GlStateManager.disableColorMaterial();

				if (flag1) {
					this.unsetScoreTeamColor();
				}
			} else {
				boolean flag = this.setDoRenderBrightness(entity, partialTicks);
				this.renderModel(entity, f6, f5, f8, f2, f7, f4);

				if (flag) {
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);

				this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
			}

			GlStateManager.disableRescaleNormal();
		} catch (Exception exception) {
			Utils.getLogger().error("Couldn't render entity", (Throwable) exception);
		}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		if (!this.renderOutlines) {
			//this.renderName(entity, x, y, z);
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<EntityHuman>(entity, this,
						partialTicks, x, y, z));

		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	}

	@Override
	protected void renderModel(EntityHuman entity, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor) {
		boolean flag = this.isVisible(entity);
		boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);

		if (flag || flag1) {
			if (!this.bindEntityTexture(entity)) {
				return;
			}

			if (flag1) {
				GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
			ModelPlayer model = getModelForEntity(entity);
			model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

			if (flag1) {
				GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
		}
	}

	public static ModelPlayer getModelForEntity(EntityHuman entity) {
		// 
		return entity.getSex().equals(EnumSex.FEMALE.getName()) ? modelAlex : modelSteve;
	}

	private void setModelVisibilities(EntityHuman entityHuman) {
		ModelPlayer modelplayer = getModelForEntity(entityHuman);

		ItemStack itemstack = entityHuman.getHeldItemMainhand();
		ItemStack itemstack1 = entityHuman.getHeldItemOffhand();
		modelplayer.setVisible(true);
		modelplayer.isSneak = entityHuman.isSneaking();
		ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
		ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

		if (!itemstack.isEmpty()) {
			modelbiped$armpose = ModelBiped.ArmPose.ITEM;

			if (entityHuman.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.BLOCK) {
					modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
				} else if (enumaction == EnumAction.BOW) {
					modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			} else if (itemstack.getItem() instanceof ItemGun) {
				modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}

		if (!itemstack1.isEmpty()) {
			modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

			if (entityHuman.getItemInUseCount() > 0) {
				EnumAction enumaction1 = itemstack1.getItemUseAction();

				if (enumaction1 == EnumAction.BLOCK) {
					modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
				} else if (enumaction1 == EnumAction.BOW) {
					modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			} else if (itemstack1.getItem() instanceof ItemGun) {
				modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}

		if (entityHuman.getPrimaryHand() == EnumHandSide.RIGHT) {
			modelplayer.rightArmPose = modelbiped$armpose;
			modelplayer.leftArmPose = modelbiped$armpose1;
		} else {
			modelplayer.rightArmPose = modelbiped$armpose1;
			modelplayer.leftArmPose = modelbiped$armpose;
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	public ResourceLocation getEntityTexture(EntityHuman entity) {
		if (entity.getRes().toString() != entity.getTexture()) {
			entity.setRes(new ResourceLocation(entity.getTexture()));
		}
		return entity.getRes();
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	/**
	 * Allows the render to do state modifications necessary before the model is
	 * rendered.
	 */
	protected void preRenderCallback(EntityHuman entitylivingbaseIn, float partialTickTime) {
		float f = 0.9375F;
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}

	public void renderRightArm(EntityHuman entityHuman) {
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelPlayer modelplayer = getModelForEntity(entityHuman);
		this.setModelVisibilities(entityHuman);
		GlStateManager.enableBlend();
		modelplayer.swingProgress = 0.0F;
		modelplayer.isSneak = false;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityHuman);
		modelplayer.bipedRightArm.rotateAngleX = 0.0F;
		modelplayer.bipedRightArm.render(0.0625F);
		modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
		modelplayer.bipedRightArmwear.render(0.0625F);
		GlStateManager.disableBlend();
	}

	public void renderLeftArm(EntityHuman entityHuman) {
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelPlayer modelplayer = getModelForEntity(entityHuman);
		this.setModelVisibilities(entityHuman);
		GlStateManager.enableBlend();
		modelplayer.isSneak = false;
		modelplayer.swingProgress = 0.0F;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityHuman);
		modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
		modelplayer.bipedLeftArm.render(0.0625F);
		modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
		modelplayer.bipedLeftArmwear.render(0.0625F);
		GlStateManager.disableBlend();
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityHuman entityLivingBaseIn, double x, double y, double z) {
		if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
			super.renderLivingAt(entityLivingBaseIn, x + (double) entityLivingBaseIn.renderOffsetX,
					y + (double) entityLivingBaseIn.renderOffsetY, z + (double) entityLivingBaseIn.renderOffsetZ);
		} else {
			super.renderLivingAt(entityLivingBaseIn, x, y, z);
		}
	}

	protected void applyRotations(EntityHuman entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping()) {
			GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
		} else if (entityLiving.isElytraFlying()) {
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
			float f = (float) entityLiving.getTicksElytraFlying() + partialTicks;
			float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f1 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
			Vec3d vec3d = entityLiving.getLook(partialTicks);
			double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
			double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;

			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z)
						/ (Math.sqrt(d0) * Math.sqrt(d1));
				double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
				GlStateManager.rotate((float) (Math.signum(d3) * Math.acos(d2)) * 180.0F / (float) Math.PI, 0.0F, 1.0F,
						0.0F);
			}
		} else {
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
		}
	}
}
