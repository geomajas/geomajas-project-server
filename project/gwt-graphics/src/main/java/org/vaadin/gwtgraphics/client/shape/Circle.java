package org.vaadin.gwtgraphics.client.shape;

import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Circle represents a circle.
 * 
 * @author Henri Kerola / IT Mill Ltd
 */
public class Circle extends Shape {

	/**
	 * Creates a new Circle with the given position and radius properties.
	 * 
	 * @param x
	 *            the x-coordinate position of the center of the circle in
	 *            pixels
	 * @param y
	 *            the y-coordinate position of the center of the circle in
	 *            pixels
	 * @param radius
	 *            the radius of the circle in pixels
	 */
	public Circle(int x, int y, int radius) {
		setRadius(radius);
		setX(x);
		setY(y);
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Circle.class;
	}

	/**
	 * Returns the radius of the circle in pixels.
	 * 
	 * @return the radius of the circle in pixels
	 */
	public int getRadius() {
		return getImpl().getCircleRadius(getElement());
	}

	/**
	 * Sets the radius of the circle in pixels.
	 * 
	 * @param radius
	 *            the radius of the circle in pixels
	 */
	public void setRadius(int radius) {
		getImpl().setCircleRadius(getElement(), radius);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.Shape#setPropertyDouble(java.lang.String,
	 * double)
	 */
	public void setPropertyDouble(String property, double value) {
		property = property.toLowerCase();
		if ("radius".equals(property)) {
			setRadius((int) value);
		} else {
			super.setPropertyDouble(property, value);
		}
	}
}
