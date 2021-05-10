package nuparu.sevendaystomine.client.gui;

public enum EnumHudPosition {

	LEFT_BOTTOM(0,1,10,-20,false,true),
	LEFT_TOP(0,0,10,20,true,true),
	RIGHT_TOP(1,0,-90,20,true,false),
	RIGHT_BOTTOM(1,1,-90,-20,false,false);
	
	double x;
	double y;
	
	int xOffset;
	int yOffset;
	
	boolean top;
	boolean left;
	
	EnumHudPosition(double x, double y, int xOffset, int yOffset,boolean top,boolean left) {
		this.x = x;
		this.y = y;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.top = top;
		this.left = left;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public boolean isTop() {
		return top;
	}
	public boolean isBottom() {
		return !top;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return !left;
	}
}
