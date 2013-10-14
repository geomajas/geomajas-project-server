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
package org.geomajas.graphics.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.graphics.client.controller.CreateController;
import org.geomajas.graphics.client.controller.CreateMetaController;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * This toolbar contains a number of toggle buttons.
 * When a button is clicked, a controller is activated.
 * Only one controller can be active at the time.
 * 
 * @author Jan Venstermans
 */
public class CreateButtonToolbar extends FlowPanel {
	
	private CreateMetaController handler;
	
	private List<CreateControllerButton> buttons;
	
	public CreateButtonToolbar(CreateMetaController handler) {
		this.handler = handler;
		buttons = new ArrayList<CreateControllerButton>();
	}

	public void addCreateController(CreateController<?> controller, String buttonLabel) {
		CreateControllerButton button = new CreateControllerButton(handler, controller, buttonLabel);
		buttons.add(button);
		add(button);
	}
	
	public void setDown(CreateController<?> controller) {
		for (CreateControllerButton button : buttons) {
			if (button.getController() == controller) {
				button.setDown(true);
			}
		}
	}
	
	public void setAllDown() {
		for (CreateControllerButton button : buttons) {
			button.setDown(false);
		}
	}
	
	/**
	 * {@ ToggleButton} that triggers a {@ CreateController}.
	 */
	public class CreateControllerButton extends ToggleButton {

		private CreateController<?> controller;
		
		private CreateMetaController handler;
		
		public CreateControllerButton(CreateMetaController handler, final 
				CreateController<?> controller, String buttonLabel) {
			super(buttonLabel);
			this.handler = handler;
			this.controller = controller;
			addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					boolean create = isDown();
					setDown(false);
					CreateControllerButton.this.handler.onCreate(
							getController(), create);
				}
			});
		}
		
		public CreateController<?> getController() {
			return controller;
		}

	}
	
}
