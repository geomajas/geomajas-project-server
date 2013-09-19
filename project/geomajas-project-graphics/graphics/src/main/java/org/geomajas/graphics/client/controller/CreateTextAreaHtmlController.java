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
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.BboxPosition;

/**
 * Controller that creates a {@link GTextAreaHtml} with an {@link Anchored} role.
 * 
 * @author Jan Venstermans
 * 
 */
public class CreateTextAreaHtmlController extends CreateTextController {
	
	private int textAreaWidth;
	
	private int textAreaHeight;

	public CreateTextAreaHtmlController(GraphicsService graphicsService, int textAreaWidth, int textAreaHeight) {
		super(graphicsService);
		this.textAreaWidth = textAreaWidth;
		this.textAreaHeight = textAreaHeight;
	}

	@Override
	protected void addObject(GText result) {
		GTextAreaHtml textArea = new GTextAreaHtml(result.getPosition().getX(), result.getPosition().getY(),
				textAreaWidth, textAreaHeight, result.getLabel(), BboxPosition.CORNER_LL);
		execute(new AddOperation(textArea));
	}
}