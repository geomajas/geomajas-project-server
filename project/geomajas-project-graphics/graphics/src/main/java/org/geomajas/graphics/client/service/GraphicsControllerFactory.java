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
package org.geomajas.graphics.client.service;

import org.geomajas.graphics.client.object.GraphicsObject;

/**
 * Factory for {@link GraphicsController} instances.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GraphicsControllerFactory {

	/**
	 * Does the factory support this object ?
	 * 
	 * @param object
	 * @return true if supported, false otherwise
	 */
	boolean supports(GraphicsObject object);

	/**
	 * Create a {@link GraphicsController} for this object and service.
	 * 
	 * @param graphicsService
	 * @param object
	 * @return the controller for this object
	 */
	GraphicsController createController(GraphicsService graphicsService, GraphicsObject object);
}
