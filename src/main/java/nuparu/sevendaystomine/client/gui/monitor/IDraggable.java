package nuparu.sevendaystomine.client.gui.monitor;

public interface IDraggable {

	public boolean isDragged();
	
	public void setOffsetX(double offset);
	public void setOffsetY(double offset);
	
	public double getOffsetX();
	public double getOffsetY();
	
}
