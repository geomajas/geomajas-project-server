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
import org.vaadin.gwtgraphics.client.Image;

/**
 * A non-scaling image that is anchored to its world space location on a specific pixel or anchor location (useful for
 * location markers).
 * 
 * @author Jan De Moerloose
 * 
 */
public class AnchoredImage extends Image {

	// value between 0 (left) and 1 (right)
	private double anchorX;

	// value between 0 (top) and 1 (bottom)
	private double anchorY;

	/**
	 * Creates an image at the specified world location with a specified size and anchor point. E.g., if
	 * (anchorX,anchorY)=(1/2, 1/2), the center of the image will be positioned the world location.
	 * 
	 * @param userX x-location in world coordinates
	 * @param userY y-location in world coordinates
	 * @param width width in pixels
	 * @param height height in pixels
	 * @param href URL of the image (use absolute URLs, not GWT-based !)
	 * @param anchorX x-location of the anchor point (image-relative)
	 * @param anchorY y-location of the anchor point (image-relative)
	 */
	public AnchoredImage(double userX, double userY, int width, int height, String href, 
			double anchorX, double anchorY) {
		super(userX, userY, width, height, href);
		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}

	public double getAnchorX() {
		return anchorX;
	}

	public void setAnchorX(double anchorX) {
		this.anchorX = anchorX;
		drawTransformed();
	}

	public double getAnchorY() {
		return anchorY;
	}

	@Override
	protected void drawTransformed() {
		getImpl().setX(getElement(), (int) Math.round(getUserX() * getScaleX() +
				getDeltaX() - anchorX * super.getUserWidth()), isAttached());
		getImpl().setY(getElement(), (int) Math.round(getUserY() * getScaleY() + 
				getDeltaY() - anchorY * super.getUserHeight()), isAttached());
		// don't scale, but have to set width/height here !
		setWidth((int) super.getUserWidth());
		setHeight((int) super.getUserHeight());
	}

	/**
	 * Get the bounds in user space.
	 * 
	 * @return the bounds in user space (takes anchor shift into account).
	 */
	public Bbox getUserBounds() {
		return new Bbox(getUserX() - getUserWidth() * anchorX, getUserY() - getUserHeight() * anchorY, getUserWidth(),
				getUserHeight());
	}

	/**
	 * Get the bounds in screen space.
	 * 
	 * @return the bounds in screen space (takes anchor shift into account).
	 */
	public Bbox getBounds() {
		return new Bbox(getX() - getWidth() * anchorX, getY() - getHeight() * anchorY, getWidth(), getHeight());
	}
	
	/**
	 * UserWidth in super class is just the pixelWidth.
	 * 
	 * @return the width in user scale.
	 */
	public double getUserWidth() {
		if (getScaleX() != 0.0) {
			return super.getUserWidth() / getScaleX();
		} else {
			return super.getUserWidth();
		}
	}
	
	/**
	 * UserHeigth in super class is just the pixelHeigth.
	 * 
	 * @return the width in user scale.
	 */
	public double getUserHeight() {
		if (getScaleX() != 0.0) {
			return super.getUserHeight() / Math.abs(getScaleY());
		} else {
			return super.getUserHeight();
		}
	}

}