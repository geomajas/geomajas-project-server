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

	private double userX1;

	private double userY1;

	private double userX2;

	private double userY2;

	public CurveTo(boolean relativeCoords, int x1, int y1, int x2, int y2, int x, int y) {
		this(relativeCoords, (double) x1, (double) y1, (double) x2, (double) y2, (double) x, (double) y);
	}

	public CurveTo(boolean relativeCoords, double userX1, double userY1, double userX2, double userY2, double userX,
			double userY) {
		super(relativeCoords, userX, userY);
		this.userX1 = userX1;
		this.userY1 = userY1;
		this.userX2 = userX2;
		this.userY2 = userY2;
		this.x1 = (int) userX1;
		this.y1 = (int) userY1;
		this.x2 = (int) userX2;
		this.y2 = (int) userY2;
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
	
	protected void setX1(int x1) {
		this.x1 = x1;
	}

	
	protected void setY1(int y1) {
		this.y1 = y1;
	}

	
	protected void setX2(int x2) {
		this.x2 = x2;
	}

	
	protected void setY2(int y2) {
		this.y2 = y2;
	}

	@Override
	public void scale(ScaleHelper scaleHelper) {
		super.scale(scaleHelper);
		setX1(scaleHelper.getScaledX(relativeCoords, userX1));
		setY1(scaleHelper.getScaledY(relativeCoords, userY1));
		setX2(scaleHelper.getScaledX(relativeCoords, userX2));
		setY2(scaleHelper.getScaledY(relativeCoords, userY2));
	}

	@Override
	public String getSVGString() {
		return isRelativeCoords() ? "c" : "C" + getX1() + " " + getY1() + " "
				+ getX2() + " " + getY2() + " " + getX() + " " + getY();
	}
}