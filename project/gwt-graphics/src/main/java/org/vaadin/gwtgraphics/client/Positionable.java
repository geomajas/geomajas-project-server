package org.vaadin.gwtgraphics.client;

/**
 * A class implementing this interface can be positioned by setting the x and y coordinates. The
 * {@link #setUserX(double)} and {@link #setUserY(double)} methods allow to set these coordinates as doubles, which is
 * useful if variable scaling is applied on an element. These values will be left intact after scaling, while the
 * {@link #getX()} and {@link #getY()} methods will return the scaled coordinates.
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
	 * Sets the y-coordinate position of the element in user space.
	 * 
	 * @param y
	 *            the new y-coordinate position
	 */
	public abstract void setY(int y);
	
	/**
	 * Returns the x-coordinate position of the element in user space.
	 * 
	 * @return the x-coordinate position
	 */
	public abstract double getUserX();

	/**
	 * Sets the x-coordinate position of the element in user space.
	 * 
	 * @param x
	 *            the new x-coordinate position
	 */
	public abstract void setUserX(double x);

	/**
	 * Returns the y-coordinate position of the element in user space.
	 * 
	 * @return the y-coordinate position
	 */
	public abstract double getUserY();

	/**
	 * Sets the y-coordinate position of the element in user space.
	 * 
	 * @param y
	 *            the new y-coordinate position
	 */
	public abstract void setUserY(double y);
}