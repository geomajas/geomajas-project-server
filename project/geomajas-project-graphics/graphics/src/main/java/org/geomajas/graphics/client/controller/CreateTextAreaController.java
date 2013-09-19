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

import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.object.GTextAreaHtml;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Controller that creates a {@link GTextAreaHtml}.
 * 
 * @author Jan De Moerloose
 * 
 */

public class CreateTextAreaController extends CreateAnchoredTextAreaController {

	public CreateTextAreaController(GraphicsService graphicsService, int textAreaWidth, int textAreaHeight) {
		super(graphicsService, textAreaWidth, textAreaHeight);
	}
	
	@Override
	protected GTextAreaHtml createTextArea(GText text) {
		GTextAreaHtml textArea = super.createTextArea(text);
		textArea.removeRole(Anchored.TYPE);
		textArea.setPosition(text.getRole(Anchored.TYPE).getAnchorPosition());
		return textArea;
	}
}