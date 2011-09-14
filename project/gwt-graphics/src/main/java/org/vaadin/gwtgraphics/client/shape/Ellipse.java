package org.vaadin.gwtgraphics.client.shape;

import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Ellipse represents an ellipse.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Ellipse extends Shape {
	
	private double userRadiusX;
	
	private double userRadiusY;
	

	/**
	 * Creates a new Ellipse with the given position and radius properties.
	 * 
	 * @param x
	 *            the x-coordinate position of the center of the ellipse in
	 *            pixels
	 * @param y
	 *            the y-coordinate position of the center of the ellipse in
	 *            pixels
	 * @param radiusX
	 *            the x-axis radius of the ellipse in pixels
	 * @param radiusY
	 *            the y-axis radius of the ellipse in pixels
	 */
	public Ellipse(int x, int y, int radiusX, int radiusY) {
		this((double) x, (double) y, (double) radiusX, (double) radiusY);
	}

	/**
	 * Creates a new Ellipse with the given position and radius properties.
	 * 
	 * @param userX
	 *            the x-coordinate position of the center of the ellipse in
	 *            pixels
	 * @param userY
	 *            the y-coordinate position of the center of the ellipse in
	 *            pixels
	 * @param userRadiusX
	 *            the x-axis radius of the ellipse in pixels
	 * @param userRadiusY
	 *            the y-axis radius of the ellipse in pixels
	 */
	public Ellipse(double userX, double userY, double userRadiusX, double userRadiusY) {
		super(userX, userY);
		setUserRadiusX(userRadiusX);
		setUserRadiusY(userRadiusY);
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Ellipse.class;
	}

	/**
	 * Returns the x-axis radius of the ellipse in pixels.
	 * 
	 * @return the x-axis radius of the ellipse in pixels
	 */
	public int getRadiusX() {
		return getImpl().getEllipseRadiusX(getElement());
	}

	/**
	 * Sets the x-axis radius of the ellipse in pixels.
	 * 
	 * @param radiusX
	 *            the x-axis radius of the ellipse in pixels
	 */
	public void setRadiusX(int radiusX) {
		getImpl().setEllipseRadiusX(getElement(), radiusX);
	}

	/**
	 * Returns the y-axis radius of the ellipse in pixels.
	 * 
	 * @return the y-axis radius of the ellipse in pixels
	 */
	public int getRadiusY() {
		return getImpl().getEllipseRadiusY(getElement());
	}

	/**
	 * Sets the y-axis radius of the ellipse in pixels.
	 * 
	 * @param radiusY
	 *            the y-axis radius of the ellipse in pixels
	 */
	public void setRadiusY(int radiusY) {
		getImpl().setEllipseRadiusY(getElement(), radiusY);
	}
	
	protected double getUserRadiusX() {
		return userRadiusX;
	}

	
	protected void setUserRadiusX(double userRadiusX) {
		this.userRadiusX = userRadiusX;
		drawTransformed();
	}

	
	protected double getUserRadiusY() {
		return userRadiusY;
	}

	
	protected void setUserRadiusY(double userRadiusY) {
		this.userRadiusY = userRadiusY;
		drawTransformed();
	}

	protected void drawTransformed() {
		super.drawTransformed();
		setRadiusX(scaleX(userRadiusX));
		setRadiusY(scaleY(userRadiusY));
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
		if ("radiusx".equals(property)) {
			setRadiusX((int) value);
		} else if ("radiusy".equals(property)) {
			setRadiusY((int) value);
		} else {
			super.setPropertyDouble(property, value);
		}
	}


}
