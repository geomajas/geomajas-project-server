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

import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Enumeration of standard Marker Shapes.
 * 
 * @author Jan Venstermans
 * 
 */
public enum MarkerShape {

	/**
	 * a square, side 8 pixels.
	 */
	SQUARE("Square"),
	
	/**
	 * a circle, diameter 8 pixels.
	 */
	CIRCLE("Circle"),
	
	/**
	 * a cross: diagonals of a 8x8 box.
	 */
	CROSS("Cross");
	
	private String title;

	/**
	 * Constructor.
	 * 
	 * @param code code to apply
	 */
	private MarkerShape(String title) {
		this.title = title;
	}

	public Shape getSimpleShape() {
		switch(this) {
			case SQUARE:
				return new Rectangle(2, 2, 8, 8);
			case CIRCLE:
				return new Circle(6, 6, 4);
			case CROSS:
				return CROSS.getMarkerShape();
		}
		return null;
	}
	
	public Shape getMarkerShape() {
		switch(this) {
			case SQUARE:
				return new AnchoredRectangle(0, 0,
						8, 8, 4, 4);
			case CIRCLE:
				return new AnchoredCircle(0, 0, 4, 0, 0);
			case CROSS:
				return new AnchoredCross(6, 6, 8);
		}
		return null;
	}
	
	public String getTitle() {
		return title;
	}

}

