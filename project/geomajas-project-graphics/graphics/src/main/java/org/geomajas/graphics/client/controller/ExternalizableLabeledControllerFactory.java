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
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsControllerFactory;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Factory for the {@link ExternalizableLabeledController}.
 * 
 * @author Jan Venstermans
 * 
 */
public class ExternalizableLabeledControllerFactory implements GraphicsControllerFactory {
	
	@Override
	public boolean supports(GraphicsObject object) {
		return object.hasRole(ExternalizableLabeled.TYPE) 
				// this second condition only for now: ExternalizableLabeled extends Labeled
				// should change to a separate role
				&& object.getRole(ExternalizableLabeled.TYPE) instanceof ExternalizableLabeled;
	}

	@Override
	public GraphicsController createController(GraphicsService graphicsService, GraphicsObject object) {
		return new ExternalizableLabeledController(graphicsService, object);
	}

}
