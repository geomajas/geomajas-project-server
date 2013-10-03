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
 * Implemented by templateLabeled graphics objects.
 * 
 * @author Jan Venstermans
 * 
 */
public interface TemplateLabeled extends Textable {
	
	RoleType<TemplateLabeled> TYPE = new RoleType<TemplateLabeled>("TemplateLabeled");
	
	String getLabelTemplateText();
	
	void setLabelTemplateText(String templateText);
	
	String getLabelRenderedText();
	
	void setLabelRenderedText(String renderedText);
	
	void renderTemplateText();
}
