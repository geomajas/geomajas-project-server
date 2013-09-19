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
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.object.anchor.ResizableAnchorer;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Controller that creates a {@link GText} with an {@link Anchored} role.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CreateAnchoredTextController extends CreateTextController {

	public CreateAnchoredTextController(GraphicsService graphicsService) {
		super(graphicsService);
	}
	
	@Override
	protected GText createText(String text) {
		return createText(text, 0, 40);
	}

	/**
	 * Returns an anchored text with a specified position difference.
	 * 
	 * @param text text string
	 * @param xDelta difference in x postion between text and anchor in screen coordinates (pixels)
	 * @param yDelta difference in y postion between text and anchor in screen coordinates (pixels)
	 * @return GText Anchored GText Area, anchor on click position
	 */ 
	protected GText createText(String text, int xDelta, int yDelta) {
		Coordinate screenPos = transform(getClickPosition(), Space.USER, Space.SCREEN);
		Coordinate textPos = transform(new Coordinate(screenPos.getX() - xDelta, screenPos.getY() - yDelta),
				Space.SCREEN, Space.USER);
		
		GText result = super.createText(text);
		result.addRole(new ResizableAnchorer(getClickPosition(), null));
		result.getRole(Draggable.TYPE).setPosition(textPos);
		// result.getRole(Anchored.TYPE);
		return result;
	}
}
