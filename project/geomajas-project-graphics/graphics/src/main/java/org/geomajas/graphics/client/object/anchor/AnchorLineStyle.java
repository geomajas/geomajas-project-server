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
package org.geomajas.graphics.client.object.anchor;


/**
 * Interface with style methods for an anchor line between two objects (points).
 * 
 * @author Jan Venstermans
 * 
 */
public interface AnchorLineStyle {

	void setAnchorLineWidth(int width);

	int getAnchorLineWidth();
	
	void setAnchorLineColor(String color);

	String getAnchorLineColor();
	
	void setAnchorLineOpacity(double opacity);

	double getAnchorLineOpacity();
	
	void setAnchorLineDashStyle(AnchorLineDashStyle dashStyle);
	
	/**
	 * Different dashing possibilities of Anchor lines.
	 * This is based on the dashArray string attribute of Strokeable Shape objects.
	 * for use and result of a dashed shapes: 
	 * https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-dasharray
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	public enum AnchorLineDashStyle {
		STRAIGHT(""), // straight line
		DASH_EQUAL_5("5 5"); // dashed line, equidistant line and space
		
		private String dashArray;
	
		/**
		 * Constructor.
		 * 
		 * @param code
		 *            code to apply
		 */
		private AnchorLineDashStyle(String dashArray) {
			this.dashArray = dashArray;
		}
		
		public String toString() {
			return dashArray;
		}
	}
	
}
