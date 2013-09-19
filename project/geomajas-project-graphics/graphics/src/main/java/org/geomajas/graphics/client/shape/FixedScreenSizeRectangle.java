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
import org.geomajas.graphics.client.object.Cloneable;
import org.geomajas.graphics.client.util.BboxPosition;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * A non-scaling rectangle. 
 * This object holds a position (userX,userY) - this is the upper left position in screen space;
 * and a fixed pixel width and pixel height - saved as super.userX and super.userY).
 * 
 * You can obtain the user width and height (user lenghts) as userWidth and userHeight.
 * You can obtain the pixel width and height as pixelWidth and pixelHeight.
 * 
 * @author Jan Venstermans
 *  
 */
public class FixedScreenSizeRectangle extends Rectangle implements Cloneable {
	
	private BboxPosition userUlPosition;
	
	public FixedScreenSizeRectangle(double userX, double userY, int pixelWidth, int pixelHeight, 
			BboxPosition userUlPosition) {
		super(userX, userY, (double) pixelWidth, (double) pixelHeight);
		super.setFixedSize(true);
		super.setUserWidth(pixelWidth);
		super.setUserHeight(pixelHeight);
		this.userUlPosition = userUlPosition;
	}

	@Override
	public Object cloneObject() {
		return null;
	}

	public double getUserWidth() {
		return super.getUserWidth() / getScaleX();
	}
	
	public int getPixelWidth() {
		return (int) super.getUserWidth(); 
	}
	
	public int getPixelHeight() {
		return (int) super.getUserHeight(); 
	}

	public void setUserWidth(double userWidth) {
	}

	public double getUserHeight() {
		return super.getUserHeight() / getScaleY();
	}

	public void setUserHeight(double userHeight) {
	}
	
	@Override
	protected void drawTransformed() {
		// apparently, redraw not here?
//		double xPosition = getUserX() + getWidth();
//		double yPosition = getUserY();
//		setX((int) Math.round(xPosition));
//		setY((int) Math.round(yPosition));
	}
	
	//bbox in user lengths
	public Bbox getUserBounds() {
		Bbox userBox = new Bbox();
		userBox.setWidth(getUserWidth());
		userBox.setHeight(getUserHeight());
		userBox.setX(getUserX());
		userBox.setY(getUserY());
		if (userUlPosition.equals(BboxPosition.CORNER_LR) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
			// change x-axis values: x to other side
			userBox.setX(userBox.getX() - userBox.getWidth());
		}
		if (userUlPosition.equals(BboxPosition.CORNER_UL) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
			// change y-axis values: y to other side
			userBox.setY(userBox.getY() - userBox.getHeight());
		}
		return userBox;
	}

	//bbox in pixel lengths
	public Bbox getBounds() {
		Bbox pixelBox = new Bbox();
		pixelBox.setWidth(getPixelWidth());
		pixelBox.setHeight(getPixelHeight());
		pixelBox.setX(getX());
		pixelBox.setY(getY());
		if (userUlPosition.equals(BboxPosition.CORNER_LR) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
			// change x-axis values: x to other side
			pixelBox.setX(pixelBox.getX() - pixelBox.getWidth());
		}
		if (userUlPosition.equals(BboxPosition.CORNER_UL) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
			// change y-axis values: y to other side
			pixelBox.setY(pixelBox.getY() - pixelBox.getHeight());
		}
		return pixelBox;
	}
}