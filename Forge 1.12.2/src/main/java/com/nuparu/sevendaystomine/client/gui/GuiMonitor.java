package com.nuparu.sevendaystomine.client.gui;

import java.io.IOException;

import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;
import com.nuparu.sevendaystomine.tileentity.TileEntityMonitor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class GuiMonitor extends GuiScreen{
	private TileEntityMonitor monitorTE;
	private Screen screen = new Screen(this);
	public GuiMonitor(EntityPlayer player, TileEntityMonitor monitor) {
		this.monitorTE = monitor;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		screen.mouseX = mouseX;
		screen.mouseY = mouseY;
		screen.render(partialTicks);
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
		screen.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
	}
	
	public TileEntityMonitor getMonitor() {
		return monitorTE;
	}
	
	public TileEntityComputer getComputer() {
		return monitorTE != null ? monitorTE.getComputer() : null;
	}
	
	public boolean shouldDisplayAnything() {
		return getComputer() != null && getComputer().isOn() && monitorTE != null && monitorTE.getState() && getComputer().isCompleted();
	}
}
