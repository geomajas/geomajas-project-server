package org.vaadin.gwtgraphics.client.shape.path;

/**
 * This class represents Path's lineTo step. Draws a straight line from the
 * current point to a new point.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class LineTo extends MoveTo {

	/**
	 * Instantiates a new LineTo step with given properties.
	 * 
	 * @param relativeCoords
	 *            true if given coordinates are relative
	 * @param x
	 *            the x-coordinate in pixels
	 * @param y
	 *            the y-coordinate in pixels
	 */
	public LineTo(boolean relativeCoords, int x, int y) {
		super(relativeCoords, x, y);
	}

	@Override
	public String getSVGString() {
		return isRelativeCoords() ? "l" : "L" + getX() + " " + getY();
	}
}
