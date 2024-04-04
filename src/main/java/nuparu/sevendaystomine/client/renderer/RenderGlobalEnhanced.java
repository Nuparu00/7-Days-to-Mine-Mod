package nuparu.sevendaystomine.client.renderer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakSavedData;

@SideOnly(Side.CLIENT)
public class RenderGlobalEnhanced extends RenderGlobal {

	@SideOnly(Side.CLIENT)
	private static Field f_icons;

	public RenderGlobalEnhanced(Minecraft minecraft) {
		super(minecraft);
		f_icons = ObfuscationReflectionHelper.findField(RenderGlobal.class, "field_94141_F");
	}

	public void drawBlockDamageTexture(Tessellator tessellatorIn, BufferBuilder worldRendererIn, Entity entityIn,
			float partialTicks) {
		double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;

		World world = entityIn.world;
		Minecraft minecraft = Minecraft.getMinecraft();
		ArrayList<BreakData> list = BreakSavedData.get(world).getList();
		GlStateManager.pushMatrix();
		if (list != null) {

			GlStateManager.pushMatrix();

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR,
					GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
			GlStateManager.doPolygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableAlpha();

			worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
			worldRendererIn.setTranslation(-d0, -d1, -d2);
			worldRendererIn.noColor();
			Iterator<BreakData> it = list.iterator();
			TextureAtlasSprite[] textures;
			try {
				textures = ((TextureAtlasSprite[]) (f_icons.get((RenderGlobal) this)));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
			while (it.hasNext()) {
				BreakData data = (BreakData) it.next();
				BlockPos blockpos = BlockPos.fromLong(data.getPos());

				if (!world.isBlockLoaded(blockpos)) {
					continue;
				}

				double d3 = (double) blockpos.getX() - d0;
				double d4 = (double) blockpos.getY() - d1;
				double d5 = (double) blockpos.getZ() - d2;
				Block block = world.getBlockState(blockpos).getBlock();
				TileEntity te = world.getTileEntity(blockpos);
				boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest
						|| block instanceof BlockSign || block instanceof BlockSkull;

				if (!hasBreak)
					hasBreak = te != null && te.canRenderBreaking();
				if (!hasBreak) {
					if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D) {
						continue;
					} else {
						IBlockState iblockstate = world.getBlockState(blockpos);
						if (iblockstate.getMaterial() != Material.AIR) {
							int i = MathHelper.clamp((int) Math.floor(data.getState() * 10), 0, 9);
							BlockRendererDispatcher blockrendererdispatcher = minecraft.getBlockRendererDispatcher();
							blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textures[i], world);

						}
					}
				}
			}
			tessellatorIn.draw();
			worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableAlpha();
			GlStateManager.doPolygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.enableAlpha();
			GlStateManager.depthMask(true);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO,
					GlStateManager.DestFactor.ONE);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
