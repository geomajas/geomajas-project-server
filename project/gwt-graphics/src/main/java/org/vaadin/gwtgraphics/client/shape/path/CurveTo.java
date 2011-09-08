package org.vaadin.gwtgraphics.client.shape.path;

/**
 * This class represents Path's curveto step. Draws a cubic bézier curve from
 * the current point to (x, y). (x1, y1) is the control point for the beginning
 * of the curve. (x2, y2) is the control point for the end of the curve.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class CurveTo extends LineTo {

	private int x1;

	private int y1;

	private int x2;

	private int y2;

	public CurveTo(boolean relativeCoords, int x1, int y1, int x2, int y2,
			int x, int y) {
		super(relativeCoords, x, y);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	@Override
	public String getSVGString() {
		return isRelativeCoords() ? "c" : "C" + getX1() + " " + getY1() + " "
				+ getX2() + " " + getY2() + " " + getX() + " " + getY();
	}
}