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
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Controller that creates a {@link GraphicsObject}.
 * 
 * @param <T> The type of GraphicsObject created by the controller.
 * 
 * @author Jan Venstermans
 * 
 */
public abstract class CreateController<T extends GraphicsObject> extends AbstractGraphicsController {

	private boolean active;
	
	public CreateController(GraphicsService graphicsService) {
		super(graphicsService);
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	protected void addObject(T result) {
		execute(new AddOperation(result));
	}
}
