package org.vaadin.gwtgraphics.client.shape.path;

/**
 * This class represents Path's moveTo step. The pen is lifted and moved to a
 * new location.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class MoveTo extends ClosePath {

	protected final boolean relativeCoords;

	protected int x;

	protected int y;

	protected double userX;

	protected double userY;

	/**
	 * Instantiates a new MoveTo step with given properties.
	 * 
	 * @param relativeCoords
	 *            true if given coordinates are relative
	 * @param x
	 *            the x-coordinate in pixels
	 * @param y
	 *            the y-coordinate in pixels
	 */
	public MoveTo(boolean relativeCoords, int x, int y) {
		this(relativeCoords, (double) x, (double) y);
	}

	/**
	 * Instantiates a new MoveTo step with given properties.
	 * 
	 * @param relativeCoords
	 *            true if given coordinates are relative
	 * @param x
	 *            the x-coordinate in pixels
	 * @param y
	 *            the y-coordinate in pixels
	 */
	public MoveTo(boolean relativeCoords, double userX, double userY) {
		this.relativeCoords = relativeCoords;
		this.userX = userX;
		this.userY = userY;
		this.x = (int) userX;
		this.y = (int) userY;
	}

	/**
	 * Returns true if coordinates relative. False means that coordinates are
	 * absolute.
	 * 
	 * @return true of coordinates are relative
	 */
	public boolean isRelativeCoords() {
		return relativeCoords;
	}

	/**
	 * Returns the x-coordinate in pixels.
	 * 
	 * @return the x-coordinate in pixels
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y-coordinate in pixels.
	 * 
	 * @return the y-coordinate in pixels
	 */
	public int getY() {
		return y;
	}
	
	
	
	@Override
	public void scale(ScaleHelper scaleHelper) {
		setX(scaleHelper.getScaledX(relativeCoords, userX));
		setY(scaleHelper.getScaledY(relativeCoords, userY));
		scaleHelper.moveTo(relativeCoords, userX, userY);
	}

	public double getUserX() {
		return userX;
	}

	
	public double getUserY() {
		return userY;
	}

	protected void setX(int x) {
		this.x = x;
	}

	
	protected void setY(int y) {
		this.y = y;
	}

	@Override
	public String getSVGString() {
		return isRelativeCoords() ? "m" : "M" + getX() + " " + getY();
	}
}
