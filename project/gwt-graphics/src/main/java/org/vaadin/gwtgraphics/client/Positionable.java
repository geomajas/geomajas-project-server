package org.vaadin.gwtgraphics.client;

/**
 * A class implementing this interface can be positioned by setting the x and y
 * coordinates.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public interface Positionable {

	/**
	 * Returns the x-coordinate position of the element.
	 * 
	 * @return the x-coordinate position in pixels
	 */
	public abstract int getX();

	/**
	 * Sets the x-coordinate position of the element.
	 * 
	 * @param x
	 *            the new x-coordinate position in pixels
	 */
	public abstract void setX(int x);

	/**
	 * Returns the y-coordinate position of the element.
	 * 
	 * @return the y-coordinate position in pixels
	 */
	public abstract int getY();

	/**
	 * Sets the y-coordinate position of the element.
	 * 
	 * @param y
	 *            the new y-coordinate position in pixels
	 */
	public abstract void setY(int y);
}