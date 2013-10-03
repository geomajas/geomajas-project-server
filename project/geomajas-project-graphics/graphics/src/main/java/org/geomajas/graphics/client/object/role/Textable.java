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
 * Implemented by Textable graphics objects.
 * It differs from labeled: Labeled role is an updateable aware role (same level as Renderable);
 * Textable is for the properties of the text itself.
 * 
 * @author Jan Venstermans
 * 
 */
public interface Textable {
	
	RoleType<Textable> TYPE = new RoleType<Textable>("Textable");

	void setLabel(String label);

	String getLabel();

	void setFontColor(String color);

	String getFontColor();
	
	void setFontSize(int size);

	int getFontSize();
	
	void setFontFamily(String font);

	String getFontFamily();
}
