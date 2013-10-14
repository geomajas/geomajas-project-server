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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.widget.CreateButtonToolbar;

/**
 * Controller that contains all registered create controllers.
 * The activation of these controllers is performed via buttons of the {@link CreateButtonToolbar}.
 * 
 * @author Jan Venstermans
 * 
 */
public class CreateMetaController extends AbstractGraphicsController implements GraphicsObjectContainerEvent.Handler {
	
	private boolean active;
	
	private List<CreateController<?>> createControllerList;
	
	private CreateButtonToolbar buttonToolbar;

	public CreateMetaController(GraphicsService service) {
		super(service);
		createControllerList = new ArrayList<CreateController<?>>();
		getService().getObjectContainer().addGraphicsObjectContainerHandler(this);
		buttonToolbar = new CreateButtonToolbar(this);
	}
	
	public void add(CreateController<?> createController, String buttonlabel) {
		createControllerList.add(createController);
		buttonToolbar.addCreateController(createController, buttonlabel);
	}

	public CreateButtonToolbar getToolbar() {
		return buttonToolbar;
	}

	@Override
	public void setActive(boolean active) {
		for (CreateController<?> controller : createControllerList) {
			controller.setActive(false);
		}
		buttonToolbar.setAllDown();
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void setVisible(boolean visible) {
	}
	
	public void onCreate(CreateController<?> createController, boolean create) {
		if (create && !active) {
			setActive(true);
			createController.setActive(active);
			buttonToolbar.setDown(createController);
			getService().getMetaController().setActive(false);
			return;
		}
		if (!create && active) {
			setActive(false);
			getService().getMetaController().setActive(true);
		}
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (active && event.getActionType().equals(ActionType.ADD)) {
			setActive(false);
		}
	}
}
