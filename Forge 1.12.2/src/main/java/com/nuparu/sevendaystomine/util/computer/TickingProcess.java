package com.nuparu.sevendaystomine.util.computer;

import java.util.ArrayList;
import java.util.UUID;

import com.nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TickingProcess {

	protected TileEntityComputer computerTE;

	public boolean noTimeLimit = false;
	public long existedFor = 0;
	public long duration = 0;

	@SideOnly(Side.CLIENT)
	protected ArrayList<IScreenElement> elements = new ArrayList<IScreenElement>();
	@SideOnly(Side.CLIENT)
	protected Screen screen;
	@SideOnly(Side.CLIENT)
	public boolean clientInit;

	protected UUID id;

	public TickingProcess() {
		id = UUID.randomUUID();
	}

	@SideOnly(Side.CLIENT)
	public void clientInit() {
		clientInit = true;
	}
	
	@SideOnly(Side.CLIENT)
	public void clientTick() {
		for (IScreenElement element : elements) {
			element.update();
		}
	}

	public void tick() {
		existedFor++;
	}

	public void onOpen() {

	}

	public void onClose() {

	}
	
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		
	}

	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		for (IScreenElement element : elements) {
			GlStateManager.pushMatrix();
			element.render(partialTicks);
			GlStateManager.popMatrix();
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setString(ProcessRegistry.RES_KEY, ProcessRegistry.INSTANCE.getResByClass(this.getClass()).toString());
		nbt.setBoolean("noTimeLimit", noTimeLimit);
		nbt.setLong("existedFor", existedFor);
		nbt.setLong("duration", duration);
		nbt.setUniqueId("id", id);
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		noTimeLimit = nbt.getBoolean("noTimeLimit");
		existedFor = nbt.getLong("existedFor");
		duration = nbt.getLong("duration");
		id = nbt.getUniqueId("id");
	}

	public void setComputer(TileEntityComputer computerTE) {
		this.computerTE = computerTE;
	}

	@SideOnly(Side.CLIENT)
	public void addElement(IScreenElement element) {
		elements.add(element);
	}

	@SideOnly(Side.CLIENT)
	public void removeElement(IScreenElement element) {
		elements.remove(element);
	}

	public UUID getId() {
		return id;
	}

	@SideOnly(Side.CLIENT)
	public Screen getScreen() {
		return screen;
	}

	@SideOnly(Side.CLIENT)
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		for (IScreenElement element : elements) {
			element.keyTyped(typedChar, keyCode);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		for (IScreenElement element : elements) {
			element.mouseReleased(mouseX,mouseY,state);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for (IScreenElement element : elements) {
			element.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
		}
	}
	@SideOnly(Side.CLIENT)
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (IScreenElement element : elements) {
			element.mouseClicked(mouseX,mouseY,mouseButton);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		
	}
	
	public TileEntityComputer getTE() {
		return this.computerTE;
	}
}
