package com.nuparu.sevendaystomine.events;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityScreenProjectorRenderer;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.item.ItemGun.EnumWield;
import com.nuparu.sevendaystomine.item.ItemWire;
import com.nuparu.sevendaystomine.tileentity.TileEntityScreenProjector;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.ModConstants;
import com.nuparu.sevendaystomine.util.PrefabHelper;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEventHandler {

	private static enum EnumHandPos {
		NONE, PISTOL_ONE, PISTOL_DUAL, LONG_ONE;
	}

	private EnumHandPos handPos = EnumHandPos.NONE;
	private boolean aiming = false;
	private ItemGun gun = null;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderBlockDamage(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		GlStateManager.pushAttrib();

		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		SevenDaysToMine.renderGlobalEnhanced.drawBlockDamageTexture(tessellator, worldrenderer,
				mc.getRenderViewEntity(), event.getPartialTicks());
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.popAttrib();

	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderHandEvent(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		AbstractClientPlayer abstractclientplayer = mc.player;
		if (player == null) {
			return;
		}
		if (handPos != EnumHandPos.NONE) {
			boolean flag = mc.getRenderViewEntity() instanceof EntityLivingBase
					&& ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();
			flag = false;
			if (mc.gameSettings.thirdPersonView == 0 && !flag && !mc.gameSettings.hideGUI
					&& !mc.playerController.isSpectator()) {
				net.minecraftforge.client.ForgeHooksClient.setRenderPass(-10);
				float f1 = abstractclientplayer.getSwingProgress(event.getPartialTicks());
				float f2 = abstractclientplayer.prevRotationPitch
						+ (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch)
								* event.getPartialTicks();

				int i = Minecraft.getMinecraft().world.getCombinedLight(new BlockPos(abstractclientplayer.posX,
						abstractclientplayer.posY + (double) abstractclientplayer.getEyeHeight(),
						abstractclientplayer.posZ), 0);
				float e = (float) (i & 65535);
				float e1 = (float) (i >> 16);

				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, e, e1);
				RenderHelper.enableStandardItemLighting();
				Minecraft.getMinecraft().entityRenderer.enableLightmap();

				GlStateManager.translate(0f, player.getEyeHeight() + 0.1F, 0f);
				GlStateManager.rotate(player.rotationYaw - 180, 0.0F, -1.0F, 0.0F);
				GlStateManager.rotate(player.rotationPitch, -1.0F, 0.0F, 0.0F);
				GlStateManager.pushMatrix();
				GlStateManager.scale(0.5f, 0.5f, 0.5f);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableBlend();
				GlStateManager.translate(0f, 0.05f, 0f);
				renderArms((AbstractClientPlayer) (player), f2, f1, 1f, event.getPartialTicks());
				GlStateManager.enableBlend();
				GlStateManager.popMatrix();

				GlStateManager.pushMatrix();
				GlStateManager.scale(0.5f, 0.5f, 0.5f);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableBlend();

				renderItems();

				GlStateManager.enableBlend();
				GL11.glDisable(GL11.GL_LIGHTING);
				GlStateManager.popMatrix();
				Minecraft.getMinecraft().entityRenderer.disableLightmap();
				RenderHelper.disableStandardItemLighting();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderItems() {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;

		ItemStack main = player.getHeldItemMainhand();
		ItemStack sec = player.getHeldItemOffhand();

		if (handPos == EnumHandPos.PISTOL_DUAL) {
			renderItem(EnumHandSide.RIGHT, 0, 0, 0, 0, 0, 0, main);
			renderItem(EnumHandSide.LEFT, -0.45f, 0, -0.48f, 0, 0, 0, sec);
		} else if (handPos == EnumHandPos.PISTOL_ONE) {

			if (main != null && !main.isEmpty() && main.getItem() instanceof ItemGun) {
				renderItem(EnumHandSide.RIGHT, 0, 0, 0, 0, 0, 0, main);
				return;
			} else if (sec != null && !sec.isEmpty() && sec.getItem() instanceof ItemGun) {
				renderItem(EnumHandSide.RIGHT, 0, 0, 0, 0, 0, 0, sec);
				return;
			}
		} else {
			if (main != null && !main.isEmpty() && main.getItem() instanceof ItemGun) {
				renderItem(EnumHandSide.RIGHT, 0, 0, 0, 0, 0, 0, main);
				return;
			} else if (sec != null && !sec.isEmpty() && sec.getItem() instanceof ItemGun) {
				renderItem(EnumHandSide.RIGHT, 0, 0, 0, 0, 0, 0, sec);
				return;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderItem(EnumHandSide hand, float x, float y, float z, float rotX, float rotY, float rotZ,
			ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		ItemCameraTransforms.TransformType transform = ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;
		if (hand == EnumHandSide.LEFT) {
			transform = ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.25f, -0.75F, -0.6f);
		GlStateManager.rotate(45, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(rotX, 1F, 0.0F, 0.0F);
		GlStateManager.rotate(rotY, 0F, 1.0F, 0.0F);
		GlStateManager.rotate(rotZ, 0F, 0.0F, 1.0F);

		if (aiming && gun != null) {
			/*
			if (this.handPos == EnumHandPos.PISTOL_ONE) {
				GlStateManager.translate(-0.2, 0.1,-0.25);
				GlStateManager.rotate(0, 0.0F, 1, 0.0F);
			}
			else {
				GlStateManager.translate(-0.43, 0.1, 0);
				GlStateManager.rotate(0, 0.0F, 1, 0.0F);
			}*/
			Vec3d pos = gun.getAimPosition();
			//Vec3d pos = new Vec3d(-0.025, -0.025, -0.4);
			GlStateManager.translate(pos.x, pos.y, pos.z);
		}

		// GlStateManager.disableDepth();
		// GlStateManager.enableCull();

		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		IBakedModel ibakedmodel = mc.getRenderItem().getItemModelWithOverrides(stack, player.world, player);
		ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, transform, false);
		mc.getRenderItem().renderItem(stack, ibakedmodel);
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();

		// GlStateManager.disableCull();
		// GlStateManager.enableDepth();

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderHandEvent(RenderHandEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		aiming = false;
		gun = null;
		if (player == null) {
			handPos = EnumHandPos.NONE;
			return;
		}
		ItemStack main = player.getHeldItemMainhand();
		ItemStack sec = player.getHeldItemOffhand();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			handPos = EnumHandPos.NONE;
			return;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			handPos = EnumHandPos.NONE;
			return;
		}

		ItemGun gun_main = null;
		ItemGun gun_sec = null;

		if (item_main instanceof ItemGun) {
			gun_main = (ItemGun) item_main;
		}

		if (item_sec instanceof ItemGun) {
			gun_sec = (ItemGun) item_sec;
		}

		if (gun_main == null && gun_sec == null) {
			handPos = EnumHandPos.NONE;
			return;
		}

		event.setCanceled(true);

		if (gun_main != null && gun_main.getWield() == EnumWield.DUAL) {
			if (gun_sec == null) {
				aiming = (mc.gameSettings.keyBindAttack.isKeyDown() && !gun_main.getScoped() && gun_main.getFOVFactor(main) != 1);
				gun = gun_main;
				handPos = EnumHandPos.PISTOL_ONE;
				return;
			}
			if (gun_sec.getWield() == EnumWield.DUAL) {
				handPos = EnumHandPos.PISTOL_DUAL;
				return;
			}
		}
		if (gun_sec != null && gun_sec.getWield() == EnumWield.DUAL) {
			aiming = (mc.gameSettings.keyBindAttack.isKeyDown() && !gun_sec.getScoped() && gun_sec.getFOVFactor(sec) != 1);
			gun = gun_sec;
			handPos = EnumHandPos.PISTOL_ONE;
			return;
		}
		if (gun_main != null) {
			EnumWield wield = gun_main.getWield();
			if (wield == EnumWield.ONE_HAND) {
				aiming = (mc.gameSettings.keyBindAttack.isKeyDown() && !gun_main.getScoped() && gun_main.getFOVFactor(main) != 1);
				gun = gun_main;
				handPos = EnumHandPos.LONG_ONE;
				return;
			}
			if (wield == EnumWield.TWO_HAND) {
				aiming = (mc.gameSettings.keyBindAttack.isKeyDown() && !gun_main.getScoped() && gun_main.getFOVFactor(main) != 1);
				gun = gun_main;
				handPos = EnumHandPos.LONG_ONE;
				return;
			}
		}

		if (gun_sec != null) {
			EnumWield wield = gun_sec.getWield();
			if (wield == EnumWield.ONE_HAND) {
				aiming = gun_main != null ? false : (mc.gameSettings.keyBindAttack.isKeyDown() && !gun_sec.getScoped() && gun_sec.getFOVFactor(sec) != 1);
				gun = gun_sec;
				handPos = EnumHandPos.LONG_ONE;
				return;
			}
			if (wield == EnumWield.TWO_HAND) {
				aiming = gun_main != null ? false : (mc.gameSettings.keyBindAttack.isKeyDown() && !gun_sec.getScoped() && gun_sec.getFOVFactor(sec) != 1);
				gun = gun_sec;
				handPos = EnumHandPos.LONG_ONE;
				return;
			}
		}

		handPos = EnumHandPos.NONE;

	}

	@SideOnly(Side.CLIENT)
	private void renderArms(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_,
			float ticks) {
		float f = -0.4F * MathHelper.sin(MathHelper.sqrt(p_178097_4_) * (float) Math.PI);
		float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(p_178097_4_) * (float) Math.PI * 2.0F);
		float f2 = -0.2F * MathHelper.sin(p_178097_4_ * (float) Math.PI);

		GlStateManager.translate(f, f1, f2);
		GlStateManager.translate(0.0F, 0.0F, 0F);
		GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(1.5f, 1.5f, 1.5f);
		this.renderPlayerArms(clientPlayer, ticks);
	}

	@SideOnly(Side.CLIENT)
	private void renderPlayerArms(AbstractClientPlayer clientPlayer, float ticks) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(clientPlayer.getLocationSkin());

		Render<AbstractClientPlayer> render = Minecraft.getMinecraft().getRenderManager()
				.<AbstractClientPlayer>getEntityRenderObject(Minecraft.getMinecraft().player);
		RenderPlayer renderplayer = (RenderPlayer) render;

		if (!clientPlayer.isInvisible()) {
			GlStateManager.translate(0.5f, 0.25F, 0f);
			GlStateManager.pushMatrix();
			this.renderRightArm(renderplayer);
			this.renderLeftArm(renderplayer);
			GlStateManager.popMatrix();
		}

	}

	@SideOnly(Side.CLIENT)
	private void renderRightArm(RenderPlayer renderPlayerIn) {

		GlStateManager.pushMatrix();
		if (handPos == EnumHandPos.PISTOL_ONE) {
			GlStateManager.translate(-0.6F, -1.15F, 0.95F);
			GlStateManager.rotate(-90.0F, 0.75F, 0.8F, 0.6F);
			if (aiming && gun != null) {
				GlStateManager.translate(-0.35F, 0.1, 0F);
			}
		} else if (handPos == EnumHandPos.PISTOL_DUAL) {
			GlStateManager.translate(-0.6F, -0.68F, -0F);
			// GlStateManager.rotate(-90.0F, 0.75F, 0.8F, 0.6F);
			GlStateManager.rotate(90.0F, 0.0F, 0F, -1.2F);
			GlStateManager.rotate(90.0F, 0.0F, 1F, 0F);
			GlStateManager.scale(1, 1, 1.5);
			GlStateManager.scale(0.5, 0.5, 0.5);
		} else if (handPos == EnumHandPos.LONG_ONE) {
			GlStateManager.translate(-0.6F, -1.15F, 0.95F);
			GlStateManager.rotate(-90.0F, 0.75F, 0.8F, 0.6F);
			if (aiming && gun != null) {
				GlStateManager.translate(-0.35F, 0.1, 0F);
			}
		}

		renderPlayerIn.renderRightArm(Minecraft.getMinecraft().player);
		GlStateManager.popMatrix();

	}

	@SideOnly(Side.CLIENT)
	private void renderLeftArm(RenderPlayer renderPlayerIn) {

		GlStateManager.pushMatrix();
		if (handPos == EnumHandPos.PISTOL_ONE) {
			GlStateManager.translate(0.1F, -0.2F, 0.5F);
			GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, 0.5F);
			GlStateManager.translate(-0.3F, -1.1F, 0.45F);
			GlStateManager.translate(-0.75F, 0.0F, -0.2F);
			if (aiming && gun != null) {
				GlStateManager.translate(0.20F, -0.18, 0F);
			}
		} else if (handPos == EnumHandPos.PISTOL_DUAL) {
			GlStateManager.translate(-0.6F, -0.68F, -0.01F);
			// GlStateManager.rotate(-90.0F, 0.75F, 0.8F, 0.6F);
			GlStateManager.rotate(90.0F, 0.0F, 0F, -1.2F);
			GlStateManager.rotate(90.0F, 0.0F, 1F, 0F);
			GlStateManager.scale(1, 1, 1.5);
			GlStateManager.scale(0.5, 0.5, 0.5);
		}
		if (handPos == EnumHandPos.LONG_ONE) {
			GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.3F, -1.1F, 0.45F);
			GlStateManager.translate(-0.5F, 0.5F, 0.5F);
			if (aiming && gun != null) {
				GlStateManager.translate(0.20F, -0.34, -0.3F);
			}
		}

		renderPlayerIn.renderLeftArm(Minecraft.getMinecraft().player);
		GlStateManager.popMatrix();

	}

	// Renders Reality Wand selection box
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void renderRealityWandSelection(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();

		EntityPlayer player = mc.player;
		if (player == null || player != mc.getRenderViewEntity())
			return;
		ItemStack stack = player.getHeldItemMainhand();
		if (stack != null && stack.getItem() == ModItems.REALITY_WAND) {
			PrefabHelper helper = PrefabHelper.getInstance();
			if (helper.posA != null && helper.posB != null) {
				BlockPos posA = helper.posA;
				BlockPos posB = helper.posB;

				double xMin = Math.min(posA.getX(), posB.getX()) - 0.01;
				double yMin = Math.min(posA.getY(), posB.getY()) - 0.01;
				double zMin = Math.min(posA.getZ(), posB.getZ()) - 0.01;

				double xMax = Math.max(posA.getX(), posB.getX()) + 0.01 + 1;
				double yMax = Math.max(posA.getY(), posB.getY()) + 0.01 + 1;
				double zMax = Math.max(posA.getZ(), posB.getZ()) + 0.01 + 1;

				double x = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
				double y = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
				double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GlStateManager.enableBlend();
				// GlStateManager.enableAlpha();
				GL11.glTranslated(-x, -y, -z);
				GL11.glBegin(GL11.GL_QUADS);
				float alpha = MathUtils.clamp((float) Math.sin(System.currentTimeMillis() / 1000d), 0.35f, 0.75f);
				renderQuad(xMin, yMax, zMin, xMax, yMax, zMax, alpha, false);
				renderQuad(xMax, yMin, zMin, xMin, yMin, zMax, alpha, false);

				renderQuad(xMin, yMax, zMax, xMax, yMin, zMax, alpha, false);
				renderQuad(xMin, yMin, zMin, xMax, yMax, zMin, alpha, false);

				renderQuad(xMax, yMax, zMin, xMax, yMin, zMax, alpha, true);
				renderQuad(xMin, yMin, zMin, xMin, yMax, zMax, alpha, true);
				GL11.glEnd();
				GlStateManager.disableBlend();
				// GlStateManager.disableAlpha();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderQuad(double x1, double y1, double z1, double x2, double y2, double z2, float alpha,
			boolean opposite) {
		GL11.glColor4f(0.0f, 1.0f, 0.0f, alpha);
		if (!opposite) {
			GL11.glVertex3d(x1, y2, z2);
			GL11.glVertex3d(x2, y2, z2);
			GL11.glVertex3d(x2, y1, z1);
			GL11.glVertex3d(x1, y1, z1);
		} else {
			GL11.glVertex3d(x1, y2, z2);
			GL11.glVertex3d(x1, y2, z1);
			GL11.glVertex3d(x2, y1, z1);
			GL11.glVertex3d(x2, y1, z2);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void renderPowerLines(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			return;
		World world = mc.world;
		Entity player = mc.getRenderViewEntity();
		if (player == null)
			return;
		double x = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
		double y = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
		double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

		GL11.glPushMatrix();
		GL11.glTranslated(-x, -y, -z);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.enableFog();
		GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		for (TileEntity te : world.loadedTileEntityList) {
			if (!(te instanceof IVoltage)) {
				continue;
			}
			IVoltage voltage = (IVoltage) te;

			List<ElectricConnection> outputs = voltage.getOutputs();
			if (outputs != null) {
				for (ElectricConnection connection : outputs) {
					renderConnection(connection, world);
				}
			}

			List<ElectricConnection> inputs = voltage.getInputs();
			if (inputs != null) {
				for (ElectricConnection connection : inputs) {
					if (!mc.world.isBlockLoaded(connection.getFrom())) {
						renderConnection(connection, world);
					}
				}
			}
		}

		GL11.glColor3f(1f, 1f, 1f);
		GlStateManager.disableFog();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public static void renderConnection(ElectricConnection connection, World world) {
		Vec3d start = connection.getRenderFrom(world);
		Vec3d end = connection.getRenderTo(world);

		GL11.glBegin(GL11.GL_LINE_STRIP);
		// GL11.glVertex3d(start.getX(),start.getY(),start.getZ());

		int distance = (int) Math.ceil(start.squareDistanceTo(end));

		for (int i = 0; i <= 64; i++) {
			Vec3d vec = MathUtils.lerp(start, end, (float) i / 64);
			double deltaY = Math.sin(((float) i / 64) * Math.PI);
			double distanceSqrt = Math.sqrt(distance);
			if (distanceSqrt < ModConstants.MAXIMAL_LENGTH) {
				deltaY /= (Math.abs(distanceSqrt - ModConstants.MAXIMAL_LENGTH) / 2d);
			}
			BlockPos pos = new BlockPos(vec.x, vec.y - deltaY, vec.z);
			IBlockState state = world.getBlockState(pos);
			if (state.isSideSolid(world, pos, EnumFacing.UP)) {
				deltaY = vec.y - pos.up().getY() - 0.00625d;
			}
			GL11.glVertex3d(vec.x, vec.y - deltaY, vec.z);
		}

		GL11.glVertex3d(end.x, end.y, end.z);
		GL11.glEnd();
	}

	@SideOnly(Side.CLIENT)
	public static void renderConnection(Vec3d start, Vec3d end, World world) {
		GL11.glBegin(GL11.GL_LINE_STRIP);
		// GL11.glVertex3d(start.getX(),start.getY(),start.getZ());

		int distance = (int) Math.ceil(start.squareDistanceTo(end));

		for (int i = 0; i <= 64; i++) {
			Vec3d vec = MathUtils.lerp(start, end, (float) i / 64);
			double deltaY = Math.sin(((float) i / 64) * Math.PI);
			double distanceSqrt = Math.sqrt(distance);
			if (distanceSqrt < ModConstants.MAXIMAL_LENGTH) {
				deltaY /= (Math.abs(distanceSqrt - ModConstants.MAXIMAL_LENGTH) / 2d);
			}
			BlockPos pos = new BlockPos(vec.x, vec.y - deltaY, vec.z);
			IBlockState state = world.getBlockState(pos);
			if (state.isSideSolid(world, pos, EnumFacing.UP)) {
				deltaY = vec.y - pos.up().getY() - 0.00625d;
			}

			GL11.glVertex3d(vec.x, vec.y - deltaY, vec.z);
		}

		GL11.glVertex3d(end.x, end.y, end.z);
		GL11.glEnd();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void renderPowerLineToEntityItems(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			return;
		World world = mc.world;
		Entity player = mc.getRenderViewEntity();
		if (player == null)
			return;

		double x = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
		double y = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
		double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

		for (Entity entity : world.loadedEntityList) {
			if (entity instanceof EntityItem) {
				ItemStack stack = ((EntityItem) entity).getItem();
				if (stack.isEmpty())
					continue;
				if (stack.getItem() instanceof ItemWire) {
					NBTTagCompound nbt = stack.getTagCompound();
					if (nbt == null)
						continue;
					if (nbt.hasKey("from", Constants.NBT.TAG_LONG)) {
						BlockPos from = BlockPos.fromLong(stack.getTagCompound().getLong("from"));

						double entityX = entity.prevPosX
								+ (entity.posX - entity.prevPosX) * (double) event.getPartialTicks();
						double entityY = entity.prevPosY
								+ (entity.posY - entity.prevPosY) * (double) event.getPartialTicks();
						double entityZ = entity.prevPosZ
								+ (entity.posZ - entity.prevPosZ) * (double) event.getPartialTicks();

						GL11.glPushMatrix();
						GL11.glTranslated(-x, -y, -z);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GlStateManager.enableFog();
						GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

						renderConnection(new Vec3d(from.getX() + 0.5d, from.getY() + 0.5d, from.getZ() + 0.5d),
								new Vec3d(entityX, entityY + entity.height, entityZ), world);

						GL11.glColor3f(1f, 1f, 1f);
						GlStateManager.disableFog();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glPopMatrix();

					} else if (nbt.hasKey("to", Constants.NBT.TAG_LONG)) {
						BlockPos to = BlockPos.fromLong(stack.getTagCompound().getLong("to"));

						double entityX = entity.prevPosX
								+ (entity.posX - entity.prevPosX) * (double) event.getPartialTicks();
						double entityY = entity.prevPosY
								+ (entity.posY - entity.prevPosY) * (double) event.getPartialTicks();
						double entityZ = entity.prevPosZ
								+ (entity.posZ - entity.prevPosZ) * (double) event.getPartialTicks();

						GL11.glPushMatrix();
						GL11.glTranslated(-x, -y, -z);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GlStateManager.enableFog();
						GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

						renderConnection(new Vec3d(to.getX() + 0.5d, to.getY() + 0.5d, to.getZ() + 0.5d),
								new Vec3d(entityX, entityY + entity.height, entityZ), world);

						GL11.glColor3f(1f, 1f, 1f);
						GlStateManager.disableFog();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glPopMatrix();
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void renderPowerLineToHand(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			return;
		World world = mc.world;
		EntityPlayer player = mc.player;
		if (player == null || player != mc.getRenderViewEntity())
			return;
		ItemStack mainStack = player.getHeldItemMainhand();
		ItemStack offStack = player.getHeldItemOffhand();

		if (mainStack.isEmpty() && offStack.isEmpty())
			return;

		Item mainItem = mainStack.getItem();
		Item offItem = offStack.getItem();

		double x = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
		double y = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
		double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

		if (mainItem instanceof ItemWire) {
			if (mainStack.getTagCompound() != null) {
				if (mainStack.getTagCompound().hasKey("to", Constants.NBT.TAG_LONG)) {
					BlockPos to = BlockPos.fromLong(mainStack.getTagCompound().getLong("to"));

					GL11.glPushMatrix();
					GL11.glTranslated(-x, -y, -z);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GlStateManager.enableFog();
					GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
					GL11.glEnable(GL11.GL_LINE_SMOOTH);
					GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
					renderLineHand(player, new Vec3d(to.getX() + 0.5, to.getY() + 0.5, to.getZ() + 0.5),
							event.getPartialTicks());
					GL11.glColor3f(1f, 1f, 1f);
					GlStateManager.disableFog();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glPopMatrix();

				} else if (mainStack.getTagCompound().hasKey("from", Constants.NBT.TAG_LONG)) {
					BlockPos from = BlockPos.fromLong(mainStack.getTagCompound().getLong("from"));

					GL11.glPushMatrix();
					GL11.glTranslated(-x, -y, -z);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GlStateManager.enableFog();
					GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
					GL11.glEnable(GL11.GL_LINE_SMOOTH);
					GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
					renderLineHand(player, new Vec3d(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5),
							event.getPartialTicks());
					GL11.glColor3f(1f, 1f, 1f);
					GlStateManager.disableFog();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glPopMatrix();
				}
			}
		}

		if (offItem instanceof ItemWire) {
			if (offStack.getTagCompound() != null) {
				if (offStack.getTagCompound().hasKey("to", Constants.NBT.TAG_LONG)) {
					BlockPos to = BlockPos.fromLong(offStack.getTagCompound().getLong("to"));

					GL11.glPushMatrix();
					GL11.glTranslated(-x, -y, -z);
					GlStateManager.enableFog();
					renderLineHand(player, new Vec3d(to.getX() + 0.5, to.getY() + 0.5, to.getZ() + 0.5),
							event.getPartialTicks());
					GlStateManager.disableFog();
					GL11.glPopMatrix();

				} else if (offStack.getTagCompound().hasKey("from", Constants.NBT.TAG_LONG)) {
					BlockPos from = BlockPos.fromLong(offStack.getTagCompound().getLong("from"));

					GL11.glPushMatrix();
					GL11.glTranslated(-x, -y, -z);
					GlStateManager.enableFog();

					renderLineHand(player, new Vec3d(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5),
							event.getPartialTicks());
					GlStateManager.disableFog();
					GL11.glPopMatrix();
				}
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public void renderLineHand(EntityPlayer entityplayer, Vec3d start, float partialTicks) {

		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		if (entityplayer != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) start.x, (float) start.y, (float) start.z);
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(
					(float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * -renderManager.playerViewX, 1.0F,
					0.0F, 0.0F);

			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			int k = entityplayer.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
			ItemStack itemstack = entityplayer.getHeldItemMainhand();

			float f7 = entityplayer.getSwingProgress(partialTicks);
			float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float) Math.PI);
			float f9 = (entityplayer.prevRenderYawOffset
					+ (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292F;
			double d0 = (double) MathHelper.sin(f9);
			double d1 = (double) MathHelper.cos(f9);
			double d2 = (double) k * 0.35D;
			double d4;
			double d5;
			double d6;
			double d7;

			if ((renderManager.options == null || renderManager.options.thirdPersonView <= 0)
					&& entityplayer == Minecraft.getMinecraft().player) {
				float f10 = renderManager.options.fovSetting;
				f10 = f10 / 100.0F;
				Vec3d vec3d = new Vec3d((double) k * -0.36D * (double) f10, -0.045D * (double) f10, 0.4D);
				vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch
						+ (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw
						+ (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(f8 * 0.5F);
				vec3d = vec3d.rotatePitch(-f8 * 0.7F);
				d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) partialTicks
						+ vec3d.x;
				d5 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double) partialTicks
						+ vec3d.y;
				d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) partialTicks
						+ vec3d.z;
				d7 = (double) entityplayer.getEyeHeight();
			} else {
				d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) partialTicks
						- d1 * d2 - d0 * 0.8D;
				d5 = entityplayer.prevPosY + (double) entityplayer.getEyeHeight()
						+ (entityplayer.posY - entityplayer.prevPosY) * (double) partialTicks - 0.45D;
				d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) partialTicks
						- d0 * d2 + d1 * 0.8D;
				d7 = entityplayer.isSneaking() ? -0.1875D : 0.0D;
			}

			if (renderManager.options.thirdPersonView == 0) {
				d4 += Math.cos(Math.toRadians(entityplayer.rotationYaw + 180)) * 0.4;
				d6 += Math.sin(Math.toRadians(entityplayer.rotationYaw + 180)) * 0.4;
			}

			double d13 = start.x;
			double d8 = start.y;
			double d9 = start.z;
			double d10 = (double) ((float) (d4 - d13));
			double d11 = (double) ((float) (d5 - d8)) + d7 - 0.5;
			double d12 = (double) ((float) (d6 - d9));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

			for (int i1 = 0; i1 <= 16; ++i1) {
				float f11 = (float) i1 / 16.0F;
				bufferbuilder.pos(start.x + d10 * (double) f11,
						start.y + d11 * (double) (f11 * f11 + f11) * 0.5D + 0.25D, start.z + d12 * (double) f11)
						.color(0, 0, 0, 255).endVertex();
			}

			tessellator.draw();

		}
	}

	private static final TileEntityScreenProjectorRenderer PROJECTOR_RENDERER = new TileEntityScreenProjectorRenderer();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void renderProjectors(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			return;
		World world = mc.world;
		Entity player = mc.getRenderViewEntity();
		if (player == null)
			return;
		double x = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
		double y = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
		double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

		for (TileEntity te : world.loadedTileEntityList) {
			if (te instanceof TileEntityScreenProjector) {
				GlStateManager.pushMatrix();
				PROJECTOR_RENDERER.render((TileEntityScreenProjector) te, te.getPos().getX() - x,
						te.getPos().getY() - y, te.getPos().getZ() - z, event.getPartialTicks(), 0, 0);
				GlStateManager.popMatrix();
			}
		}

	}

}
