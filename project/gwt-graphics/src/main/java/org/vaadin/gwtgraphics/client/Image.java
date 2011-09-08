package org.vaadin.gwtgraphics.client;

import org.vaadin.gwtgraphics.client.animation.Animatable;

/**
 * Image represents a raster image that can be embedded into DrawingArea.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Image extends VectorObject implements Positionable, Animatable {

	/**
	 * Create a new Image with the given properties.
	 * 
	 * @param x
	 *            the x-coordinate position of the top-left corner of the image
	 *            in pixels
	 * @param y
	 *            the y-coordinate position of the top-left corner of the image
	 *            in pixels
	 * @param width
	 *            the width of the image in pixels
	 * @param height
	 *            the height of the image in pixels
	 * @param href
	 *            URL to an image to be shown.
	 */
	public Image(int x, int y, int width, int height, String href) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setHref(href);
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Image.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Positionable#getX()
	 */
	public int getX() {
		return getImpl().getX(getElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Positionable#setX(int)
	 */
	public void setX(int x) {
		getImpl().setX(getElement(), x, isAttached());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Positionable#getY()
	 */
	public int getY() {
		return getImpl().getY(getElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Positionable#setY(int)
	 */
	public void setY(int y) {
		getImpl().setY(getElement(), y, isAttached());
	}

	/**
	 * Returns the URL of the image currently shown.
	 * 
	 * @return URL of the image
	 */
	public String getHref() {
		return getImpl().getImageHref(getElement());
	}

	/**
	 * Sets the URL of the image to be shown.
	 * 
	 * @param href
	 *            URL of the image to be shown
	 */
	public void setHref(String href) {
		getImpl().setImageHref(getElement(), href);
	}

	/**
	 * Returns the width of the Image in pixels.
	 * 
	 * @return the width of the Image in pixels
	 */
	public int getWidth() {
		return getImpl().getWidth(getElement());
	}

	/**
	 * Sets the width of the Image in pixels.
	 * 
	 * @param width
	 *            the new width in pixels
	 */
	public void setWidth(int width) {
		getImpl().setWidth(getElement(), width);
	}

	/**
	 * Returns the height of the Image in pixels.
	 * 
	 * @return the height of the Image in pixels
	 */
	public int getHeight() {
		return getImpl().getHeight(getElement());
	}

	/**
	 * Sets the height of the Image in pixels.
	 * 
	 * @param height
	 *            the new height in pixels
	 */
	public void setHeight(int height) {
		getImpl().setHeight(getElement(), height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.animation.Animatable#setPropertyDouble(
	 * java.lang.String, double)
	 */
	public void setPropertyDouble(String property, double value) {
		property = property.toLowerCase();
		if ("x".equals(property)) {
			setX((int) value);
		} else if ("y".equals(property)) {
			setY((int) value);
		} else if ("width".equals(property)) {
			setWidth((int) value);
		} else if ("height".equals(property)) {
			setHeight((int) value);
		} else if ("rotation".equals(property)) {
			setRotation((int) value);
		}
	}
}