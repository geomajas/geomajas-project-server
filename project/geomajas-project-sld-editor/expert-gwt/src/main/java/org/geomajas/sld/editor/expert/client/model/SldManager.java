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
package org.geomajas.sld.editor.expert.client.model;

/**
 * The main model class of the SLD editor.
 * 
 * @author Jan De Moerloose
 */
public interface SldManager {

	/**
	 * Fetch all the Templates from the server (asynchronously). Instead of passing a callback here, clients should use
	 * the model events to get registered of changes to the current list of SLD's.
	 */
	void fetchTemplateNames();

	/**
	 * Fetch the Template from the server (asynchronously). Instead of passing a callback here, clients should use the
	 * model events to get registered of changes to the current list of SLD's.
	 */
	void fetchTemplate(String name);

	/**
	 * Get the currently selected SLD.
	 * 
	 * @return the new SLD instance
	 */
	SldModel getModel();

	/**
	 * Validate the current SLD (asynchronously). Will throw Validated or SaveEvent.
	 */
	void validateCurrent(boolean saveAfterValidation);

}