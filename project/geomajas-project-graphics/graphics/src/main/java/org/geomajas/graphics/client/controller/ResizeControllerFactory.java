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
package org.geomajas.graphics.client.controller;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsControllerFactory;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Factory for the {@link ResizeController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizeControllerFactory implements GraphicsControllerFactory {
	
	private boolean dragOnActivate;
	
	public ResizeControllerFactory() {
		this(true, true);
	}

	public ResizeControllerFactory(boolean dragOnActivate, boolean showOriginalObjectWhileDragging) {
		this.dragOnActivate = dragOnActivate;
	}

	@Override
	public boolean supports(GraphicsObject object) {
		return object.hasRole(Resizable.TYPE);
	}

	@Override
	public GraphicsController createController(GraphicsService graphicsService, GraphicsObject object) {
		return new ResizeController(object, graphicsService, dragOnActivate);
	}

}
