package org.vaadin.gwtgraphics.client;

import org.vaadin.gwtgraphics.client.animation.Animatable;

/**
 * Image represents a raster image that can be embedded into DrawingArea.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Image extends VectorObject implements Positionable, Animatable {

	private double userX;

	private double userY;

	private double userWidth;

	private double userHeight;

	/**
	 * Create a new Image with the given properties.
	 * 
	 * @param x the x-coordinate position of the top-left corner of the image in pixels
	 * @param y the y-coordinate position of the top-left corner of the image in pixels
	 * @param width the width of the image in pixels
	 * @param height the height of the image in pixels
	 * @param href URL to an image to be shown.
	 */
	public Image(int x, int y, int width, int height, String href) {
		this((double) x, (double) y, (double) width, (double) height, href);
	}

	/**
	 * Create a new Image with the given properties.
	 * 
	 * @param userX the x-coordinate position of the top-left corner of the image
	 * @param userY the y-coordinate position of the top-left corner of the image
	 * @param userWidth the width of the image
	 * @param userHeight the height of the image
	 * @param href URL to an image to be shown.
	 */
	public Image(double userX, double userY, double userWidth, double userHeight, String href) {
		setUserX(userX);
		setUserY(userY);
		setWidth((int) userWidth);
		setHeight((int) userHeight);
		setHref(href);
		drawTransformed();
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

	public double getUserX() {
		return userX;
	}

	public void setUserX(double userX) {
		this.userX = userX;
		drawTransformed();
	}

	public double getUserY() {
		return userY;
	}

	public void setUserY(double userY) {
		this.userY = userY;
		drawTransformed();
	}

	public double getUserWidth() {
		return userWidth;
	}

	public void setUserWidth(double userWidth) {
		this.userWidth = userWidth;
		drawTransformed();
	}

	public double getUserHeight() {
		return userHeight;
	}

	public void setUserHeight(double userHeight) {
		this.userHeight = userHeight;
		drawTransformed();
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
	 * @param href URL of the image to be shown
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
	 * @param width the new width in pixels
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
	 * @param height the new height in pixels
	 */
	public void setHeight(int height) {
		getImpl().setHeight(getElement(), height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.animation.Animatable#setPropertyDouble( java.lang.String, double)
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

	protected void drawTransformed() {
		getImpl().setX(getElement(), roundToInt(getUserX() * getScaleX() + getDeltaX()), isAttached());
		getImpl().setY(getElement(), roundToInt(getUserY() * getScaleY() + getDeltaY()), isAttached());
		setWidth(scaleX(userWidth));
		setHeight(scaleY(userHeight));
	}

	protected int scaleX(double userX) {
		return roundToInt(userX * getScaleX());
	}

	protected int scaleY(double userY) {
		return roundToInt(userY * getScaleY());
	}

	protected int roundToInt(double d) {
		return (int) Math.round(d);
	}

}