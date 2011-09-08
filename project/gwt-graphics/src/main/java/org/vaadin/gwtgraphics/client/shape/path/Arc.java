package org.vaadin.gwtgraphics.client.shape.path;

public class Arc extends LineTo {

	private int rx;

	private int ry;

	private int xAxisRotation;

	private boolean largeArc;

	private boolean sweep;

	public Arc(boolean relativeCoords, int rx, int ry, int xAxisRotation,
			boolean largeArc, boolean sweep, int x, int y) {
		super(relativeCoords, x, y);
		this.rx = rx;
		this.ry = ry;
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

	public int getxAxisRotation() {
		return xAxisRotation;
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