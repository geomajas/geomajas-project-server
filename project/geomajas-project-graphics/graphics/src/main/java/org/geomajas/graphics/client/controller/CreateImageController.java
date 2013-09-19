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

import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GImage;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;

import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Controller that creates a {@link GImage}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CreateImageController extends AbstractGraphicsController implements MouseUpHandler {

	private boolean active;

	private HandlerRegistration registration;

	private String href;

	private int width;

	private int height;

	public CreateImageController(GraphicsService graphicsService) {
		super(graphicsService);
	}

	public CreateImageController(GraphicsService graphicsService, int width, int height, String href) {
		super(graphicsService);
		setHref(href);
		setHeight(height);
		setWidth(width);
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			registration = getObjectContainer().addMouseUpHandler(this);
		} else {
			if (registration != null) {
				registration.removeHandler();
				registration = null;
			}
		}
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		GImage result = new GImage(0, 0, width, height, href, "Image");
		result.getRole(Draggable.TYPE).setPosition(getUserCoordinate(event));
		execute(new AddOperation(result));
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}
}
