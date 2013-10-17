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
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.BboxPosition;

/**
 * Controller that creates a {@link GTextAreaHtml} with an {@link Anchored} role.
 * 
 * @author Jan Venstermans
 * 
 */
public class CreateAnchoredTextAreaController extends CreateAnchoredTextController {

	private int textAreaWidth = 150;

	private int textAreaHeight = 100;
	
	/**
	 * @param graphicsService graphicsService
	 * @param textAreaWidth default width of the textArea in pixels
	 * @param textAreaHeight default height of the textArea in pixels
	 */
	public CreateAnchoredTextAreaController(GraphicsService graphicsService, int textAreaWidth, int textAreaHeight) {
		super(graphicsService);
		this.textAreaWidth = textAreaWidth;
		this.textAreaHeight = textAreaHeight;
	}

	@Override
	protected GText createText(String text) {
		return createText(text, textAreaWidth / 2, textAreaHeight + 40);
	}

	@Override
	protected void addObject(GText result) {
		if (result == null) {
			// TODO ? make object update?
			return;
		}
		execute(new AddOperation(createTextArea(result)));
	}

	protected GTextAreaHtml createTextArea(GText text) {
		BboxPosition screenUpperLeftPositionInUserSpace = transform(BboxPosition.CORNER_UL, Space.SCREEN, Space.USER);
		GTextAreaHtml textArea = new GTextAreaHtml(0.0, 0.0, textAreaWidth, textAreaHeight, text.getLabel());
		textArea.setPosition(text.getPosition());
//		textArea.addRole(new ResizableAnchorer());
//		textArea.getRole(Anchored.TYPE).setAnchorPosition(text.getRole(Anchored.TYPE).getAnchorPosition());
		return textArea;
	}
}