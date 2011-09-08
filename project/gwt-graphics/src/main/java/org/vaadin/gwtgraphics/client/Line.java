package org.vaadin.gwtgraphics.client;

import org.vaadin.gwtgraphics.client.animation.Animatable;

/**
 * Line represents a straight line from one point to another. Line can be
 * stroked.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Line extends VectorObject implements Strokeable, Animatable {

	/**
	 * Creates a new instance of Line. This is a line from one given point to
	 * another. Default stroke width is 1px and stroke color is black.
	 * 
	 * @param x1
	 *            the x-coordinate of the starting point in pixels
	 * @param y1
	 *            the y-coordinate of the starting point in pixels
	 * @param x2
	 *            the x-coordinate of the end point in pixels
	 * @param y2
	 *            the y-coordinate of the end point in pixels
	 */
	public Line(int x1, int y1, int x2, int y2) {
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
		setStrokeWidth(1);
		setStrokeOpacity(1);
		setStrokeColor("black");
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Line.class;
	}

	/**
	 * Returns the x-coordinate position of the starting point of Line.
	 * 
	 * @return the x-coordinate in pixels
	 */
	public int getX1() {
		return getImpl().getX(getElement());
	}

	/**
	 * Sets the x-coordinate position of the starting point of Line.
	 * 
	 * @param x1
	 *            the new x-coordinate in pixels
	 */
	public void setX1(int x1) {
		getImpl().setX(getElement(), x1, isAttached());
	}

	/**
	 * Returns the y-coordinate position of the starting point of Line.
	 * 
	 * @return the y-coordinate in pixels
	 */
	public int getY1() {
		return getImpl().getY(getElement());
	}

	/**
	 * Sets the y-coordinate position of the starting point of Line.
	 * 
	 * @param y1
	 *            the new y-coordinate in pixels
	 */
	public void setY1(int y1) {
		getImpl().setY(getElement(), y1, isAttached());
	}

	/**
	 * Returns the x-coordinate position of the ending point of Line.
	 * 
	 * @return the x-coordinate in pixels
	 */
	public int getX2() {
		return getImpl().getLineX2(getElement());
	}

	/**
	 * Sets the y-coordinate position of the ending point of Line.
	 * 
	 * @param x2
	 *            the new x-coordinate in pixels
	 */
	public void setX2(int x2) {
		getImpl().setLineX2(getElement(), x2);
	}

	/**
	 * Returns the y-coordinate position of the ending point of Line.
	 * 
	 * @return the y-coordinate in pixels
	 */
	public int getY2() {
		return getImpl().getLineY2(getElement());
	}

	/**
	 * Sets the y-coordinate position of the ending point of Line.
	 * 
	 * @param y2
	 *            the new x-coordinate in pixels
	 */
	public void setY2(int y2) {
		getImpl().setLineY2(getElement(), y2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Strokeable#getStrokeColor()
	 */
	public String getStrokeColor() {
		return getImpl().getStrokeColor(getElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.Strokeable#setStrokeColor(java.lang.String)
	 */
	public void setStrokeColor(String color) {
		getImpl().setStrokeColor(getElement(), color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Strokeable#getStrokeWidth()
	 */
	public int getStrokeWidth() {
		return getImpl().getStrokeWidth(getElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Strokeable#setStrokeWidth(int)
	 */
	public void setStrokeWidth(int width) {
		getImpl().setStrokeWidth(getElement(), width, isAttached());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Strokeable#getStrokeOpacity()
	 */
	public double getStrokeOpacity() {
		return getImpl().getStrokeOpacity(getElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Strokeable#setStrokeOpacity(double)
	 */
	public void setStrokeOpacity(double opacity) {
		getImpl().setStrokeOpacity(getElement(), opacity);
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
		if ("x1".equals(property)) {
			setX1((int) value);
		} else if ("y1".equals(property)) {
			setY1((int) value);
		} else if ("x2".equals(property)) {
			setX2((int) value);
		} else if ("y2".equals(property)) {
			setY2((int) value);
		} else if ("strokeopacity".equals(property)) {
			setStrokeOpacity(value);
		} else if ("strokewidth".equals(property)) {
			setStrokeWidth((int) value);
		} else if ("rotation".equals(property)) {
			setRotation((int) value);
		}
	}
}
