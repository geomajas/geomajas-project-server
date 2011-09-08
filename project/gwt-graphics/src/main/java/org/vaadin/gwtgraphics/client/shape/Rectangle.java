package org.vaadin.gwtgraphics.client.shape;

import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Rectangle represents a rectangle.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Rectangle extends Shape {

	/**
	 * Creates a new Rectangle with the given position and size properties.
	 * 
	 * @param x
	 *            the x-coordinate position of the top-left corner of the
	 *            rectangle in pixels
	 * @param y
	 *            the y-coordinate position of the top-left corner of the
	 *            rectangle in pixels
	 * @param width
	 *            the width of the Rectangle in pixels
	 * @param height
	 *            the height of the Rectangle in pixels
	 */
	public Rectangle(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Rectangle.class;
	}

	/**
	 * Returns the width of the Rectangle in pixels.
	 * 
	 * @return the width of the Rectangle in pixels
	 */
	public int getWidth() {
		return getImpl().getWidth(getElement());
	}

	/**
	 * Sets the width of the Rectangle in pixels.
	 * 
	 * @param width
	 *            the new width in pixels
	 */
	public void setWidth(int width) {
		getImpl().setWidth(getElement(), width);
	}

	@Override
	public void setWidth(String width) {
		if (width != null && width.endsWith("px")) {
			setWidth(Integer.parseInt(width));
		} else {
			throw new UnsupportedOperationException(
					"Only pixel units are supported");
		}
	}

	/**
	 * Returns the height of the Rectangle in pixels.
	 * 
	 * @return the height of the Rectangle in pixels
	 */
	public int getHeight() {
		return getImpl().getHeight(getElement());
	}

	/**
	 * Sets the height of the Rectangle in pixels.
	 * 
	 * @param height
	 *            the new height in pixels
	 */
	public void setHeight(int height) {
		getImpl().setHeight(getElement(), height);
	}

	@Override
	public void setHeight(String height) {
		if (height != null && height.endsWith("px")) {
			setHeight(Integer.parseInt(height));
		} else {
			throw new UnsupportedOperationException(
					"Only pixel units are supported");
		}
	}

	/**
	 * Gets the radius of rounded corners in pixels.
	 * 
	 * @return radius of rounded corners in pixels
	 */
	public int getRoundedCorners() {
		return getImpl().getRectangleRoundedCorners(getElement());
	}

	/**
	 * Sets the radius of rounded corners in pixels. Value 0 disables rounded
	 * corners.
	 * 
	 * @param radius
	 *            radius of rounded corners in pixels
	 */
	public void setRoundedCorners(int radius) {
		if (radius < 0) {
			radius = 0;
		}
		getImpl().setRectangleRoundedCorners(getElement(), radius);
	}

	public void setPropertyDouble(String property, double value) {
		property = property.toLowerCase();
		if ("width".equals(property)) {
			setWidth((int) value);
		} else if ("height".equals(property)) {
			setHeight((int) value);
		} else if ("roundedcorners".equals(property)) {
			setRoundedCorners((int) value);
		} else {
			super.setPropertyDouble(property, value);
		}
	}
}