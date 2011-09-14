package org.vaadin.gwtgraphics.client.shape.path;

public class Arc extends LineTo {

	private int rx;

	private int ry;

	private int xAxisRotation;
	
	private double userRx;
	
	private double userRy;

	private boolean largeArc;

	private boolean sweep;

	public Arc(boolean relativeCoords, int rx, int ry, int xAxisRotation, boolean largeArc, boolean sweep, int x, int y) {
		this(relativeCoords, (double) rx, (double) ry, xAxisRotation, largeArc, sweep, (double) x, (double) y);
	}

	public Arc(boolean relativeCoords, double userRx, double userRy, int xAxisRotation, boolean largeArc,
			boolean sweep, double userX, double userY) {
		super(relativeCoords, userX, userY);
		this.userRx = userRx;
		this.userRy = userRy;
		this.rx = (int) userRx;
		this.ry = (int) userRy;
		this.xAxisRotation = xAxisRotation;
		this.largeArc = largeArc;
		this.sweep = sweep;
	}

	public int getRx() {
		return rx;
	}

	public int getRy() {
		return ry;
	}

	
	protected void setRx(int rx) {
		this.rx = rx;
	}

	
	protected void setRy(int ry) {
		this.ry = ry;
	}

	public int getxAxisRotation() {
		return xAxisRotation;
	}
	
	protected double getUserRx() {
		return userRx;
	}

	
	protected void setUserRx(double userRx) {
		this.userRx = userRx;
	}

	
	protected double getUserRy() {
		return userRy;
	}

	
	protected void setUserRy(double userRy) {
		this.userRy = userRy;
	}

	
	protected void setxAxisRotation(int xAxisRotation) {
		this.xAxisRotation = xAxisRotation;
	}
	
	@Override
	public void scale(ScaleHelper scaleHelper) {
		super.scale(scaleHelper);
		setRx(scaleHelper.getScaledX(relativeCoords, rx));
		setRy(scaleHelper.getScaledY(relativeCoords, ry));
	}


	public boolean isLargeArc() {
		return largeArc;
	}

	public boolean isSweep() {
		return sweep;
	}

	@Override
	public String getSVGString() {
		return isRelativeCoords() ? "a" : "A" + getRx() + "," + getRy() + " "
				+ getxAxisRotation() + " " + (isLargeArc() ? "1" : "0") + ","
				+ (isSweep() ? "1" : "0") + " " + getX() + "," + getY();
	}
}