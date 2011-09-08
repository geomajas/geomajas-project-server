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

	protected final int x;

	protected final int y;

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
		this.relativeCoords = relativeCoords;
		this.x = x;
		this.y = y;
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
	public String getSVGString() {
		return isRelativeCoords() ? "m" : "M" + getX() + " " + getY();
	}
}
