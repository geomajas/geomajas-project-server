/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripSeparator;

/**
 * A toolbar that supports two types of buttons:
 * <ul>
 * <li>modal (radiobutton-like) buttons.</li>
 * <li>non-modal or action buttons.</li>
 * </ul>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class Toolbar extends ToolStrip {

	public static final int BUTTON_SIZE_SMALL = 24;

	public static final int BUTTON_SIZE_BIG = 32;

	public static final String TOOLBAR_SEPARATOR = "ToolbarSeparator";

	private static final String CONTROLLER_RADIO_GROUP = "graphicsController";

	private MapWidget mapWidget;

	private int buttonSize;

	private List<HandlerRegistration> toolbarActionHandlers;

	private boolean initialized;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public Toolbar(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		toolbarActionHandlers = new ArrayList<HandlerRegistration>();
		setButtonSize(BUTTON_SIZE_SMALL);
		setPadding(2);
		setWidth100();
		mapWidget.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				initialize(Toolbar.this.mapWidget.getMapModel().getMapInfo());
			}
		});
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Initialize this widget.
	 * 
	 * @param mapInfo
	 */
	public void initialize(ClientMapInfo mapInfo) {
		if (!initialized) {
			ClientToolbarInfo toolbarInfo = mapInfo.getToolbar();
			if (toolbarInfo != null) {
				for (ClientToolInfo tool : toolbarInfo.getTools()) {
					String id = tool.getId();
					if (TOOLBAR_SEPARATOR.equals(id)) {
						addToolbarSeparator();
					} else {
						ToolbarBaseAction action = ToolbarRegistry.getToolbarAction(id, mapWidget);
						if (action instanceof ConfigurableAction) {
							for (Parameter parameter : tool.getParameters()) {
								((ConfigurableAction) action).configure(parameter.getName(), parameter.getValue());
							}
						}
						if (action instanceof ToolbarModalAction) {
							addModalButton((ToolbarModalAction) action);
						}
						if (action instanceof ToolbarAction) {
							addActionButton((ToolbarAction) action);
						}
					}
				}
			}
			initialized = true;
		}
	}

	/**
	 * Add a new action button to the toolbar. An action button is the kind of button that executes immediately when
	 * clicked upon. It can not be selected or deselected, it just executes every click.
	 * 
	 * @param action
	 *            The actual action to execute on click.
	 */
	public void addActionButton(final ToolbarAction action) {
		final IButton button = new IButton();
		button.setWidth(buttonSize);
		button.setHeight(buttonSize);
		button.setIconSize(buttonSize - 8);
		button.setIcon(action.getIcon());
		button.setActionType(SelectionType.BUTTON);
		button.addClickHandler(action);
		button.setShowRollOver(false);
		button.setShowFocused(false);
		button.setTooltip(action.getTooltip());
		button.setDisabled(action.isDisabled());

		if (getMembers() != null && getMembers().length > 0) {
			LayoutSpacer spacer = new LayoutSpacer();
			spacer.setWidth(2);
			addMember(spacer);
		}
		toolbarActionHandlers.add(action.addToolbarActionHandler(new ToolbarActionHandler() {

			public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
				button.setDisabled(true);
			}

			public void onToolbarActionEnabled(ToolbarActionEnabledEvent event) {
				button.setDisabled(false);
			}
		}));
		addMember(button);
	}

	/**
	 * Add a new modal button (checkbox) to the toolbar. This kind of button is selected and deselected as the user
	 * clicks upon it. By selecting and deselecting, a certain state will be activate or deactivated, determined by the
	 * given <code>ModalAction</code>.
	 * 
	 * @param modalAction
	 *            The actual action that determines what should happend when the button is selected or deselected.
	 */
	public void addModalButton(final ToolbarModalAction modalAction) {
		final IButton button = new IButton();
		button.setWidth(buttonSize);
		button.setHeight(buttonSize);
		button.setIconSize(buttonSize - 8);
		button.setIcon(modalAction.getIcon());
		button.setActionType(SelectionType.CHECKBOX);
		button.setRadioGroup(CONTROLLER_RADIO_GROUP);
		button.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (button.isSelected()) {
					modalAction.onSelect(event);
				} else {
					modalAction.onDeselect(event);
				}
			}
		});
		button.setShowRollOver(false);
		button.setShowFocused(true);
		button.setTooltip(modalAction.getTooltip());
		button.setDisabled(modalAction.isDisabled());

		if (getMembers() != null && getMembers().length > 0) {
			LayoutSpacer spacer = new LayoutSpacer();
			spacer.setWidth(2);
			addMember(spacer);
		}
		toolbarActionHandlers.add(modalAction.addToolbarActionHandler(new ToolbarActionHandler() {

			public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
				button.setDisabled(true);
			}

			public void onToolbarActionEnabled(ToolbarActionEnabledEvent event) {
				button.setDisabled(false);
			}
		}));
		addMember(button);
	}

	/**
	 * Add a vertical line to the toolbar.
	 */
	public void addToolbarSeparator() {
		ToolStripSeparator stripSeparator = new ToolStripSeparator();
		stripSeparator.setHeight(8);
		addMember(stripSeparator);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public int getButtonSize() {
		return buttonSize;
	}

	/**
	 * Set the size of the buttons. Use this before the toolbar is drawn, because afterwards, it won't work anymore.
	 * 
	 * @param buttonSize
	 */
	public void setButtonSize(int buttonSize) {
		this.buttonSize = buttonSize;
		// TODO: resize all buttons
		setHeight(buttonSize);
	}
}
