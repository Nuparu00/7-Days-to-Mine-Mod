package com.nuparu.sevendaystomine.client.gui.inventory;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.inventory.ContainerGasGenerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityGeneratorBase;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCombustionGenerator extends GuiContainer {
	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/generator_combustion.png");
	InventoryPlayer playerInventory;
	TileEntityGeneratorBase tileEntity;

	public GuiCombustionGenerator(InventoryPlayer playerInventory, IInventory tileEntity) {
		super(new ContainerGasGenerator(playerInventory, tileEntity));
		this.playerInventory = playerInventory;
		this.tileEntity = (TileEntityGeneratorBase) tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileEntity.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.format("gui.electricity.voltage") + tileEntity.getPowerPerUpdate() + "J", 55, 44, 4210752);
		fontRenderer.drawString(I18n.format("gui.electricity.stored")  + tileEntity.getVoltageStored() + "/" + tileEntity.getCapacity() + "J", 55, 54, 4210752);
		mc.getTextureManager().bindTexture(resourceLocation);
		drawTexturedModalRect(30 + (int) (tileEntity.getTemperature() * 138), 32, 190, 31, 6, 8);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		if (TileEntityGeneratorBase.isBurning(tileEntity)) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(marginHorizontal + 30, marginVertical + 45 + 12 - k, 176, 12 - k, 14, k + 1);
		}
	}

	private int getBurnLeftScaled(int pixels) {
		int i = this.tileEntity.getField(1);
		if (i == 0) {
			i = 200;
		}

		return this.tileEntity.getField(0) * pixels / i;
	}

}
