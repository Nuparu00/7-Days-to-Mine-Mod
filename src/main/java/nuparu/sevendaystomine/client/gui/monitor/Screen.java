package nuparu.sevendaystomine.client.gui.monitor;

import java.util.ArrayList;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.GuiMonitor;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class Screen {
	private ArrayList<IScreenElement> elements = new ArrayList<IScreenElement>();

	public int mouseX = 0;
	public int mouseY = 0;

	public double xSize = 1920;
	public double ySize = 1080;

	public static final Minecraft mc = Minecraft.getMinecraft();
	public final GuiMonitor gui;

	public static Screen screen;

	public Screen(GuiMonitor gui) {
		this.gui = gui;
		screen = this;
		gui.setFocused(true);
	}

	public void setScale(ScaledResolution sr) {
		xSize = sr.getScaledWidth() / 2;
		ySize = xSize * 0.5625d;

		if (ySize > (sr.getScaledHeight() / 2)) {
			ySize = sr.getScaledHeight() / 2;
			xSize = ySize * (1920d / 1080d);
		}
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.initWindow();
			}

		}
	}

	public void render(float partialTicks) {
		drawFrame();
		drawDefaultScreen();
		if (gui.shouldDisplayAnything()) {
			ArrayList<WindowedProcess> wps = new ArrayList<WindowedProcess>();
			if (gui.getComputer() == null)
				return;
			for (TickingProcess process : (ArrayList<TickingProcess>) gui.getComputer().getProcessesList().clone()) {
				if (process == null)
					continue;
				if (process.getScreen() == null || process.getScreen() != this) {
					process.setScreen(this);
				}
				if (process.clientInit == false) {
					process.clientInit();
				}

				if (process instanceof WindowedProcess) {
					wps.add((WindowedProcess) process);
				} else {
					process.render(partialTicks);
				}
			}

			wps.sort(Comparator.comparing(WindowedProcess::getWindowOrder));
			for (WindowedProcess wp : wps) {
				if (!wp.isMinimized()) {
					wp.render(partialTicks);
				}
			}

		}
	}

	public void update() {
		if (gui.shouldDisplayAnything() && gui.getComputer() != null) {
			ArrayList<TickingProcess> processesClone = new ArrayList<TickingProcess>(gui.getComputer().getProcessesList());
			for (TickingProcess process : processesClone) {
				if (process != null) {
					process.clientTick();
				}
			}
		}
	}

	public void drawFrame() {
		ScaledResolution sr = new ScaledResolution(mc);

		double width = (xSize * 1.05625d);
		double height = (ySize * 1.1d);

		double x = (sr.getScaledWidth() / 2) - (width / 2);
		double y = (sr.getScaledHeight() / 2) - (height / 2);

		RenderUtils.drawColoredRect(new ColorRGBA(0.056, 0.056, 0.056), x, y, width, height, -1);
	}

	public void drawDefaultScreen() {
		ScaledResolution sr = new ScaledResolution(mc);
		RenderUtils.drawColoredRect(new ColorRGBA(0d, 0d, 0d), (sr.getScaledWidth() / 2) - (xSize / 2),
				(sr.getScaledHeight() / 2) - (ySize / 2), xSize, ySize, 0);
	}

	public double getOffsetLeft(int w) {
		return (xSize - (w)) / 2;
	}

	public double getOffsetTop(int h) {
		return (ySize - (h)) / 2;
	}

	public double localXToGlobal(double x) {
		ScaledResolution sr = new ScaledResolution(mc);
		return (sr.getScaledWidth() / 2) - (xSize / 2) + x;
	}

	public double localYToGlobal(double y) {
		ScaledResolution sr = new ScaledResolution(mc);
		return (sr.getScaledHeight() / 2) - (ySize / 2) + y;
	}

	/*
	 * Takes in range 0.0-1.0 (both inclusive)
	 */
	public double getRelativeX(double x) {
		return localXToGlobal(x * xSize);
	}

	/*
	 * Takes in range 0.0-1.0 (both inclusive)
	 */
	public double getRelativeY(double y) {
		return localYToGlobal(y * ySize);
	}

	public void keyTyped(char typedChar, int keyCode) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.keyTyped(typedChar, keyCode);
			}
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.mouseReleased(mouseX, mouseY, state);
			}
		}
	}

	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
			}
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		ScaledResolution sr = new ScaledResolution(mc);

		double width = (xSize * 1.05625d);
		double height = (ySize * 1.1d);

		double x = (sr.getScaledWidth() / 2) - (width / 2);
		double y = (sr.getScaledHeight() / 2) - (height / 2);

		gui.setFocused(Utils.isInArea(mouseX, mouseY, x, y, width, height));

		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}

	}
}