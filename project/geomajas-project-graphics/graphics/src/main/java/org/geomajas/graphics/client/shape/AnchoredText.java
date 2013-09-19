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

import org.vaadin.gwtgraphics.client.shape.Text;

/**
 * A non-scaling text that is anchored to its world space location on a specific pixel or anchor location (useful for
 * location markers).
 * 
 * @author Jan De Moerloose
 * 
 */
public class AnchoredText extends Text {

	private double anchorX;

	private double anchorY;

	/**
	 * Creates an text at the specified world location with a specified size and anchor point. E.g., if
	 * (anchorX,anchorY)=(0.5, 0.5), the center of the text will be positioned at the world location.
	 * 
	 * @param userX x-location in world coordinates
	 * @param userY y-location in world coordinates
	 * @param width width in pixels
	 * @param height height in pixels
	 * @param anchorX x-location of the anchor point (text-relative)
	 * @param anchorY y-location of the anchor point (text-relative)
	 */
	public AnchoredText(double userX, double userY, String text, double anchorX, double anchorY) {
		super(userX, userY, text);
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		drawTransformed();
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

	public void setAnchorY(double anchorY) {
		this.anchorY = anchorY;
		drawTransformed();
	}
	
	public void setText(String text) {
		super.setText(text);
		drawTransformed();
	}
	
	public double getUserWidth() {
		return Math.abs(getTextWidth() / getScaleX());
	}

	public double getUserHeight() {
		return Math.abs(getTextHeight() / getScaleY());
	}

	@Override
	protected void drawTransformed() {
		getImpl().setX(getElement(),
				(int) Math.round(getUserX() * getScaleX() + getDeltaX() - anchorX * getTextWidth()), isAttached());
		// y is lower-left coordinate (differs from image !!!)
		getImpl().setY(getElement(),
				(int) Math.round(getUserY() * getScaleY() + getDeltaY() + anchorY * getTextHeight()), isAttached());
	}
}