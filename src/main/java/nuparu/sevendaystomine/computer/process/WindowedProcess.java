package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.IDraggable;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.KillProcessMessage;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public abstract class WindowedProcess extends TickingProcess implements IDraggable {

	protected double x;
	protected double y;

	protected double old_x;
	protected double old_y;

	protected double offsetX;
	protected double offsetY;

	protected double width;
	protected double height;

	protected int zLevel;
	// 0=first;1=second;...
	private int windowOrder = -1;
	protected Application application;

	protected boolean isFocused = false;
	protected boolean isDragged = false;

	protected boolean maximized;
	protected boolean minimized;
	protected double old_height;
	protected double old_width;

	public static double title_bar_height = 0.05;

	public Button close;
	public Button maximize;
	public Button minimize;

	private int maxRelativeZ = 0;

	public WindowedProcess() {
		this(0, 0, 0, 0);
	}

	public WindowedProcess(double x, double y, double width, double height) {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		
		if (x == -1) {
			x = Screen.screen.getRelativeX(0.5) - 50;
		}
		if (y == -1) {
			y = Screen.screen.localYToGlobal(0);
		}
		
		elements.clear();
		close = new Button(x + width - Screen.screen.ySize * title_bar_height, y,
				Screen.screen.ySize * title_bar_height, Screen.screen.ySize * title_bar_height, Screen.screen, "X", 0);
		close.textNormal = 0xffffff;
		close.normal = new ColorRGBA(0.8, 0.2, 0.1);
		close.setFontSize(0.7);
		close.setProcess(this);
		close.setZLevel(zLevel);
		close.border = false;
		elements.add(close);

		maximize = new Button(x + width - 2 * (Screen.screen.ySize * title_bar_height), y,
				Screen.screen.ySize * title_bar_height, Screen.screen.ySize * title_bar_height, Screen.screen,
				Character.toString((char) 0x25A1), -1);
		maximize.textNormal = 0xffffff;
		maximize.normal = new ColorRGBA(0.8, 0.2, 0.1);
		maximize.setFontSize(0.7);
		maximize.setProcess(this);
		maximize.setZLevel(zLevel);
		maximize.border = false;
		elements.add(maximize);

		minimize = new Button(x + width - 3 * (Screen.screen.ySize * title_bar_height), y,
				Screen.screen.ySize * title_bar_height, Screen.screen.ySize * title_bar_height, Screen.screen, "-", -2);
		minimize.textNormal = 0xffffff;
		minimize.normal = new ColorRGBA(0.8, 0.2, 0.1);
		minimize.setFontSize(0.7);
		minimize.setProcess(this);
		minimize.setZLevel(zLevel);
		minimize.border = false;
		elements.add(minimize);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("old_x", old_x);
		nbt.setDouble("old_y", old_y);
		nbt.setDouble("offsetX", offsetX);
		nbt.setDouble("offsetY", offsetY);
		nbt.setDouble("width", width);
		nbt.setDouble("height", height);
		nbt.setInteger("zLevel", zLevel);
		nbt.setInteger("windowOrder", windowOrder);
		nbt.setString("application", ApplicationRegistry.INSTANCE.getResByApp(application).toString());
		nbt.setBoolean("isFocused", isFocused);
		nbt.setBoolean("isDragged", isDragged);
		nbt.setBoolean("maximized", maximized);
		nbt.setBoolean("minimized", minimized);
		nbt.setDouble("old_width", old_width);
		nbt.setDouble("old_height", old_height);

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("x")) {
			this.x = nbt.getDouble("x");
		}
		if (nbt.hasKey("y")) {
			this.y = nbt.getDouble("y");
		}
		if (nbt.hasKey("width")) {
			this.width = nbt.getDouble("width");
		}
		if (nbt.hasKey("height")) {
			this.height = nbt.getDouble("height");
		}
		if (nbt.hasKey("zLevel")) {
			this.zLevel = nbt.getInteger("zLevel");
		}
		if (nbt.hasKey("windowOrder")) {
			this.windowOrder = nbt.getInteger("windowOrder");
		}
		if (nbt.hasKey("old_x")) {
			this.old_x = nbt.getDouble("old_x");
		}
		if (nbt.hasKey("old_y")) {
			this.old_y = nbt.getDouble("old_y");
		}
		if (nbt.hasKey("offsetX")) {
			this.offsetX = nbt.getDouble("offsetX");
		}
		if (nbt.hasKey("offsetY")) {
			this.offsetY = nbt.getDouble("offsetY");
		}
		if (nbt.hasKey("isDragged")) {
			this.isDragged = nbt.getBoolean("isDragged");
		}
		if (nbt.hasKey("isFocused")) {
			this.isFocused = nbt.getBoolean("isFocused");
		}
		if (nbt.hasKey("maximized")) {
			this.maximized = nbt.getBoolean("maximized");
		}
		if (nbt.hasKey("minimized")) {
			this.minimized = nbt.getBoolean("minimized");
		}
		if (nbt.hasKey("old_width")) {
			this.old_width = nbt.getDouble("old_width");
		}
		if (nbt.hasKey("old_height")) {
			this.old_height = nbt.getDouble("old_height");
		}

		if (nbt.hasKey("application")) {
			String s = nbt.getString("application");
			ResourceLocation res = new ResourceLocation(s);
			application = ApplicationRegistry.INSTANCE.getByRes(res);
		}

	}

	public String getTitle() {
		return this.application.getLocalizedName();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getZLevel() {
		return zLevel;
	}

	public Application getApp() {
		return application;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		this.maxRelativeZ = 0;
	}

	@SideOnly(Side.CLIENT)
	public void drawWindow(String title, ColorRGBA bgrColor, ColorRGBA titleBarColor) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, zLevel);
		RenderUtils.drawColoredRect(titleBarColor, x, y, width, (Screen.screen.ySize * title_bar_height), 0);
		RenderUtils.drawColoredRect(bgrColor, x, y + (Screen.screen.ySize * title_bar_height), width,
				height - (Screen.screen.ySize * title_bar_height), 0);
		RenderUtils.drawString(title, x+1, y+1, 0x000000);
		GlStateManager.translate(0, 0, -zLevel);
		GlStateManager.popMatrix();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {

		if (maximized && (this.width != Screen.screen.xSize || this.height != Screen.screen.ySize * 0.9
				|| this.x != Screen.screen.localXToGlobal(0) || this.y != Screen.screen.localYToGlobal(0))) {
			this.width = Screen.screen.xSize;
			this.height = Screen.screen.ySize * 0.9;
			this.x = Screen.screen.localXToGlobal(0);
			this.y = Screen.screen.localYToGlobal(0);
			
			if(this.computerTE.getSystem() == EnumSystem.MAC && y < Screen.screen.localYToGlobal(0)+screen.ySize*0.05) {
				y = Screen.screen.localYToGlobal(0)+screen.ySize*0.05;
			}
			
			sync("x", "y", "width", "height");
		}
		if (close == null)
			return;
		close.setX(x + width - Screen.screen.ySize * title_bar_height);
		close.setY(y);
		close.setWidth(Screen.screen.ySize * title_bar_height);
		close.setHeight(Screen.screen.ySize * title_bar_height);

		maximize.setX(x + width - 2 * (Screen.screen.ySize * title_bar_height));
		maximize.setY(y);
		maximize.setWidth(Screen.screen.ySize * title_bar_height);
		maximize.setHeight(Screen.screen.ySize * title_bar_height);

		minimize.setX(x + width - 3 * (Screen.screen.ySize * title_bar_height));
		minimize.setY(y);
		minimize.setWidth(Screen.screen.ySize * title_bar_height);
		minimize.setHeight(Screen.screen.ySize * title_bar_height);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {

		if (isMinimized())
			return;
		int buttonId = button.ID;
		if (buttonId == 0) {
			PacketManager.killProcess.sendToServer(new KillProcessMessage(computerTE.getPos(), this.getId()));
		} else if (buttonId == -1) {
			if (!maximized) {
				this.old_height = height;
				this.old_width = width;
				this.width = Screen.screen.xSize;
				this.height = Screen.screen.ySize * 0.9;
			} else {
				this.width = old_width;
				this.height = old_height;
			}
			this.x = Screen.screen.localXToGlobal(0);
			this.y = Screen.screen.localYToGlobal(0);
			this.maximized = !this.maximized;
			initWindow();
			sync("x", "y", "width", "height", "old_height", "old_width", "maximized", "minimized");

		} else if (buttonId == -2) {
			setMinimized(true);
			sync("x", "y", "width", "height", "old_height", "old_width", "maximized", "minimized");

		}
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (isMinimized())
			return;
		if (isDragged == true) {
			isDragged = false;
			if (mouseX < Screen.screen.getRelativeX(0) - 20 || mouseY < Screen.screen.getRelativeY(0) - 20
					|| mouseX > Screen.screen.getRelativeX(0) + Screen.screen.xSize + 20
					|| mouseY > Screen.screen.getRelativeY(0) + Screen.screen.ySize + 20) {
				x = old_x;
				y = old_y;
			}
			setOffsetX(0);
			setOffsetY(0);
			sync();
			onDragReleased();
			initWindow();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (isMinimized() || maximized)
			return;
		if (clickedMouseButton == 0 && isFocused() && Utils.isInArea(mouseX, mouseY, x, y,
				width - 3 * (Screen.screen.ySize * title_bar_height), Screen.screen.ySize * title_bar_height)) {
			if (isDragged == false) {
				isDragged = true;
			}
			x = MathUtils.clamp(mouseX - getOffsetX(), Screen.screen.getRelativeX(0),
					Screen.screen.getRelativeX(1) - width);
			y = MathUtils.clamp(mouseY - getOffsetY(), Screen.screen.getRelativeY(0),
					Screen.screen.getRelativeY(1) - height - (Screen.screen.ySize * 0.1));
			initWindow();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean focusPrev = isFocused;
		
		if (isNotHidden(mouseX, mouseY)) {

			if (Utils.isInArea(mouseX, mouseY, x, y, width - 3 * (Screen.screen.ySize * title_bar_height),
					Screen.screen.ySize * title_bar_height)) {
				if (mouseButton == 0) {
					isFocused = true;
					old_x = x;
					old_y = y;
					setOffsetX(mouseX - x);
					setOffsetY(mouseY - y);

				}
			} else if (Utils.isInArea(mouseX, mouseY, x, y + Screen.screen.ySize * title_bar_height,
					Screen.screen.ySize * width, Screen.screen.ySize * height)) {
				isFocused = true;
				super.mouseClicked(mouseX, mouseY, mouseButton);
			} else {
				for (IScreenElement element : elements) {
					element.setFocus(false);
				}
				isFocused = false;
				super.mouseClicked(mouseX, mouseY, mouseButton);
			}
			
			tryToPutOnTop();
		} else {
			for (IScreenElement element : elements) {
				element.setFocus(false);
			}
			isFocused = false;
		}
		if (focusPrev != isFocused) {
			sync();
		}
	}

	@Override
	public boolean isDragged() {
		return isDragged;
	}

	@Override
	public void setOffsetX(double offset) {
		this.offsetX = offset;
	}

	@Override
	public void setOffsetY(double offset) {
		this.offsetY = offset;
	}

	@Override
	public double getOffsetX() {
		return this.offsetX;
	}

	@Override
	public double getOffsetY() {
		return this.offsetY;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean state) {
		boolean focusedPrev = isFocused;
		this.isFocused = state;
		if (focusedPrev != isFocused) {
			sync("isFocused");
		}
	}

	public boolean isMinimized() {
		return minimized;
	}

	// Checks if the window is really visible at given coords
	public boolean isNotHidden(int mouseX, int mouseY) {
		if (!isMinimized() && isHovered(mouseX, mouseY)) {
			@SuppressWarnings("unchecked")
			ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) computerTE.getProcessesList().clone();
			for (int i = 0; i < processes.size(); i++) {
				TickingProcess tp = processes.get(i);
				if (tp instanceof WindowedProcess) {
					WindowedProcess wp = (WindowedProcess) tp;
					if (wp.getWindowOrder() <= this.getWindowOrder()) {
						continue;
					} else {
						if (Utils.isInArea(mouseX, mouseY, wp.x, wp.y, wp.width, wp.height)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void tryToPutOnTop() {

		int newOrder = this.getWindowOrder();

		@SuppressWarnings("unchecked")
		ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) computerTE.getProcessesList().clone();
		for (int i = 0; i < processes.size(); i++) {
			TickingProcess tp = processes.get(i);
			if (tp instanceof WindowedProcess) {
				WindowedProcess wp = (WindowedProcess) tp;
				if (wp.getWindowOrder() > newOrder) {
					newOrder = wp.getWindowOrder();
					wp.setWindowOrder(wp.getWindowOrder() - 1);
					continue;
				}
			}
		}
		this.setWindowOrder(newOrder);
	}

	@SideOnly(Side.CLIENT)
	public void onDragReleased() {

	}

	public int getWindowOrder() {
		return windowOrder;
	}

	@SideOnly(Side.CLIENT)
	public void setWindowOrder(int newWindowOrder) {

		boolean flag = false;
		if (this.windowOrder != newWindowOrder) {
			flag = true;
		}
		this.windowOrder = newWindowOrder;
		this.zLevel = (1 + windowOrder) * 3;
		if (flag && application != null && computerTE != null) {
			sync("windowOrder", "zLevel");
		}

	}

	public static boolean isDesktopVisible(int x, int y, TileEntityComputer te) {
		@SuppressWarnings("unchecked")
		ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) te.getProcessesList().clone();
		for (int i = 0; i < processes.size(); i++) {
			TickingProcess tp = processes.get(i);
			if (tp instanceof WindowedProcess) {
				WindowedProcess wp = (WindowedProcess) tp;
				if (!wp.isMinimized() && Utils.isInArea(x, y, wp.x, wp.y, wp.width, wp.height)) {
					return false;
				}

			}
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
	}

	public void setMinimized(boolean state) {
		minimized = state;
		if (minimized) {
			for (IScreenElement element : elements) {
				element.setFocus(false);
			}
			isFocused = false;
		}
	}

	/*
	 * TO-DO: Have to solve issue with some elemnts from hidden windows being
	 * renderer on top of other windows
	 */
	public int offsetRelativeZ(int offset) {
		int i = zLevel + offset;
		if (i > maxRelativeZ) {
			maxRelativeZ = i;
		}
		return i;
	}
	
	public boolean isMouseInside() {
		return Utils.isInArea(screen.mouseX, screen.mouseY, x, y + (Screen.screen.ySize * title_bar_height), width, height);
	}

}