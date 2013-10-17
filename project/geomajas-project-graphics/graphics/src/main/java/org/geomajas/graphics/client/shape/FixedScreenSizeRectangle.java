/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.shape;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.object.Cloneable;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * A non-scaling rectangle that is anchored to its world space location on a specific pixel or anchor location (useful
 * for location markers).
 * 
 * @author Jan Venstermans
 * @author Jan De Moerloose
 * 
 */
public class FixedScreenSizeRectangle extends Rectangle implements Cloneable {

	private double anchorX;

	private double anchorY;

	private int width;

	private int height;

	/**
	 * Creates an rectangle at the specified world location with a specified size and anchor point. E.g., if
	 * (anchorX,anchorY)=(width/2, height/2), the center of the rectangle will be positioned at the world location.
	 * 
	 * @param userX
	 *            x-location in world coordinates
	 * @param userY
	 *            y-location in world coordinates
	 * @param width
	 *            width in pixels
	 * @param height
	 *            height in pixels
	 * @param anchorX
	 *            x-location of the anchor point (rectangle-relative, screen coordinates)
	 * @param anchorY
	 *            y-location of the anchor point (rectangle-relative, screen coordinates)
	 */
	public FixedScreenSizeRectangle(double userX, double userY, int width, int height, double anchorX, double anchorY) {
		// user width and height are derived, passing 0 here !
		// userX and userY are the user coordinates of the anchor !
		super(userX, userY, 0, 0);
		this.width = width;
		this.height = height;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		drawTransformed();
	}

	public void setPosition(Coordinate position) {
		setUserX(position.getX());
		setUserY(position.getY());
		drawTransformed();
	}

	public void setUserBounds(Bbox userBounds) {
		Coordinate u1 = BboxService.getOrigin(userBounds);
		Coordinate u2 = BboxService.getEndPoint(userBounds);
		Bbox screenBounds = GraphicsUtil.toBbox(toScreenSpace(u1), toScreenSpace(u2));
		width = (int) Math.round(screenBounds.getWidth());
		height = (int) Math.round(screenBounds.getHeight());
		Coordinate screenPosition = BboxService.getOrigin(screenBounds);
		Coordinate screenAnchor = new Coordinate(screenPosition.getX() + anchorX * width, screenPosition.getY()
				+ anchorY * height);
		Coordinate position = toUserSpace(screenAnchor);
		setUserX(position.getX());
		setUserY(position.getY());
		drawTransformed();
	}

	public Bbox getBounds() {
		return new Bbox(getX(), getY(), getWidth(), getHeight());
	}

	public Bbox getUserBounds() {
		Coordinate s1 = new Coordinate(getX(), getY());
		Coordinate s2 = new Coordinate(getX() + getWidth(), getY() + getHeight());
		return GraphicsUtil.toBbox(toUserSpace(s1), toUserSpace(s2));
	}

	@Override
	public Object cloneObject() {
		return new FixedScreenSizeRectangle(getUserX(), getUserY(), getWidth(), getHeight(), getAnchorX(),
				getAnchorY());
	}

	public double getAnchorX() {
		return anchorX;
	}

	public double getAnchorY() {
		return anchorX;
	}

	protected Coordinate toScreenSpace(Coordinate userCoord) {
		double x = userCoord.getX() * getScaleX() + getDeltaX();
		double y = userCoord.getY() * getScaleY() + getDeltaY();
		return new Coordinate(x, y);
	}

	protected Coordinate toUserSpace(Coordinate screenCoord) {
		double x = (screenCoord.getX() - getDeltaX()) / getScaleX();
		double y = (screenCoord.getY() - getDeltaY()) / getScaleY();
		return new Coordinate(x, y);
	}
	

	@Override
	protected void drawTransformed() {
		getImpl().setX(getElement(), (int) Math.round(getUserX() * getScaleX() + getDeltaX() - anchorX * width),
				isAttached());
		getImpl().setY(getElement(), (int) Math.round(getUserY() * getScaleY() + getDeltaY() - anchorY * height),
				isAttached());
		// don't scale, but have to set width/height here !
		setWidth(width);
		setHeight(height);
	}

}