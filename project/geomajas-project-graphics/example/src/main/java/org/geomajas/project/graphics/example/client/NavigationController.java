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
package org.geomajas.project.graphics.example.client;

import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.vaadin.gwtgraphics.client.Group;

import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Controller for panning/zooming.
 * 
 * @author Jan De Moerloose
 * 
 */
public class NavigationController implements GraphicsController, MouseWheelHandler {

	private boolean active;

	private Group rootContainer;

	private HandlerRegistration registration;

	private int scale = 1;

	private GraphicsService service;

	public NavigationController(GraphicsService service, Group rootContainer) {
		this.rootContainer = rootContainer;
		this.service = service;
	}

	@Override
	public void setActive(boolean active) {
		if (active != isActive()) {
			this.active = active;
			if (isActive()) {
				registration = rootContainer.addMouseWheelHandler(this);
			} else {
				registration.removeHandler();
				service.getMetaController().setActive(true);
			}
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		if (event.isNorth()) {
			scale *= 2;
			rootContainer.setScale(scale, scale);
		} else {
			if (scale > 1) {
				scale /= 2;
			}
			rootContainer.setScale(scale, scale);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

}
