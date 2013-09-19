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

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GPath;
import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.object.anchor.AnchoredToResizable;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Creates a closed {@link Path}. 
 * When the polygon is created, a {@link GText} object is anchored to the polygon.
 * 
 * @author Jan Venstermans
 * 
 */
public class CreatePolygonWithTextSlaveController extends CreatePathController {

	public CreatePolygonWithTextSlaveController(GraphicsService graphicsService) {
		super(graphicsService, true);
		// TODO Auto-generated constructor stub
	}

	protected void addObject(GPath path) {
		Coordinate screenPos = transform(path.getPosition(), Space.USER, Space.SCREEN);
		Coordinate anchorPos = transform(new Coordinate(screenPos.getX() + 100, screenPos.getY() + 100), Space.SCREEN,
				Space.USER);

		GText text = CreateTextController.createTextDefault("Anchored To",
				new Coordinate(anchorPos.getX(), anchorPos.getY()));
		text.addRole(new AnchoredToResizable(path));
		execute(new AddOperation(path));
		execute(new AddOperation(text));
	}
}
