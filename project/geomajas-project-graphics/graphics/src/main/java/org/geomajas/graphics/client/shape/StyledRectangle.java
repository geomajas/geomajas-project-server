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

import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Extension of {@link Rectangle} that adds margin,...
 * 
 * @author Jan De Moerloose
 * 
 */
public class StyledRectangle extends Rectangle {

	private int margin = 10;

	public StyledRectangle(double userX, double userY, double userWidth, double userHeight) {
		super(userX, userY, userWidth, userHeight);
		setRoundedCorners(10);
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	@Override
	protected void drawTransformed() {
		if (isFixedSize()) {
			// center on userX, userY
			setX((int) Math.round(getUserX() * getScaleX() + getDeltaX() - getUserWidth() / 2 - margin));
			setY((int) Math.round(getUserY() * getScaleY() + getDeltaY() - getUserHeight() / 2 - margin));
			setWidth((int) getUserWidth() + 2 * margin);
			setHeight((int) getUserHeight() + 2 * margin);
		} else {
			// transform all points and calculate new bounds
			double x1 = getUserX() * getScaleX() + getDeltaX();
			double y1 = getUserY() * getScaleY() + getDeltaY();
			double x2 = (getUserX() + getUserWidth()) * getScaleX() + getDeltaX();
			double y2 = (getUserY() + getUserHeight()) * getScaleY() + getDeltaY();
			setX((int) Math.round(Math.min(x1, x2)) - margin);
			setY((int) Math.round(Math.min(y1, y2)) - margin);
			setWidth((int) Math.abs(x1 - x2) + 2 * margin);
			setHeight((int) Math.abs(y1 - y2) + 2 * margin);
		}
	}
}
