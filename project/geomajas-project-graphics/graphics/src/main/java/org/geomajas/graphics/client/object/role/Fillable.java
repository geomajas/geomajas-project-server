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
package org.geomajas.graphics.client.object.role;


/**
 * Implemented by graphics objects that can be filled.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Fillable {
	
	RoleType<Fillable> TYPE = new RoleType<Fillable>("Fillable");
	
	/**
	 * Changeable style elements of a fillable object.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	public enum StyleElement {
		FILL_COLOR, FILL_OPACITY
	}

	void setFillColor(String fillColor);

	void setFillOpacity(double fillOpacity);

	String getFillColor();

	double getFillOpacity();
}
