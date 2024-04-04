package nuparu.sevendaystomine.client.gui.monitor;

import nuparu.sevendaystomine.computer.process.TickingProcess;

public interface IScreenElement {
	public void setZLevel(int zLevel);
	
	public int getZLevel();

	public double getX();

	public double getY();

	public double getWidth();

	public double getHeight();

	public void render(float partialTicks);
	
	public void update();
	
	public boolean isVisible();
	
	public boolean isFocused();
	
	public boolean isDisabled();
	
	public boolean isHovered(int mouseX, int mouseY);
	
	public void keyTyped(char typedChar, int keyCode);
	
	public void mouseReleased(int mouseX, int mouseY, int state);
	
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);
	
	public void setScreen(Screen screen);

	public void mouseClicked(int mouseX, int mouseY, int mouseButton);
	
	public void setX(double x);
	
	public void setY(double y);
	
	public void setWidth(double width);
	
	public void setHeight(double height);
	
	public void setProcess(TickingProcess process);
	
	public void setFocus(boolean focus);
}
