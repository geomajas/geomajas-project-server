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
package org.geomajas.graphics.client.action;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Single action to execute on an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Action {

	/**
	 * Does the action support this object ?
	 * 
	 * @param object
	 * @return true if supported, false otherwise
	 */
	boolean supports(GraphicsObject object);

	/**
	 * Set the {@link GraphicsService} to use.
	 * 
	 * @param service
	 */
	void setService(GraphicsService service);

	/**
	 * Execute the action on this object.
	 * 
	 * @param object
	 */
	void execute(GraphicsObject object);

	/**
	 * Returns a label for the action.
	 * @return
	 */
	String getLabel();
	
	/**
	 * Get the url of the icon that represents this action.
	 * 
	 * @return the url
	 */
	String getIconUrl();
	
	/**
	 * Set the url of the icon that represents this action.
	 * 
	 * @param url
	 */
	void setIconUrl(String url);
}
