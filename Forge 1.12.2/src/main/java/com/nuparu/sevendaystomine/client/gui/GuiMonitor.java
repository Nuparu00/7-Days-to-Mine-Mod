package com.nuparu.sevendaystomine.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.renderer.CameraFBO;
import com.nuparu.sevendaystomine.entity.EntityMountableBlock;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.tileentity.TileEntityCamera;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;
import com.nuparu.sevendaystomine.tileentity.TileEntityMonitor;
import com.nuparu.sevendaystomine.util.client.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class GuiMonitor extends GuiScreen {
	private TileEntityMonitor monitorTE;
	private Screen screen = new Screen(this);

	public CameraFBO fbo;
	public EntityPlayer player;
	
	public GuiMonitor(EntityPlayer player, TileEntityMonitor monitor) {
		this.monitorTE = monitor;
		this.player = player;
	}

	Entity pig;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		screen.mouseX = mouseX;
		screen.mouseY = mouseY;
		screen.render(partialTicks);/*
		if (TileEntityCamera.TEST != null) {
			RenderUtils.renderView(Minecraft.getMinecraft(), TileEntityCamera.TEST.getCameraView(player));
		}
		else {
			System.out.println("XXXXX");
		}*/
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		screen.update();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		screen.setScale(new ScaledResolution(Minecraft.getMinecraft()));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		screen.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		screen.keyTyped(typedChar, keyCode);
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		screen.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		screen.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	public TileEntityMonitor getMonitor() {
		return monitorTE;
	}

	public TileEntityComputer getComputer() {
		return monitorTE != null ? monitorTE.getComputer() : null;
	}

	public boolean shouldDisplayAnything() {
		return getComputer() != null && getComputer().isOn() && monitorTE != null && monitorTE.getState()
				&& getComputer().isCompleted();
	}
}
