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

package org.geomajas.gwt.client.controller.editing;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.ToolInfo;
import org.geomajas.configuration.ToolbarInfo;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.menu.DeleteFeatureAction;
import org.geomajas.gwt.client.action.menu.EditFeatureAction;
import org.geomajas.gwt.client.action.menu.NewFeatureAction;
import org.geomajas.gwt.client.action.menu.ToggleSelectionAction;
import org.geomajas.gwt.client.action.toolbar.SelectionModalAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;

/**
 * The parent editing controller. It delegates to the correct child editing controller if there is one. This controller
 * has a context menu that allows you to create, update or delete features on the map. By actually clicking the create
 * or update actions in the context menu, a child controller will be set on this parent that is perfectly suited for the
 * job. Each type of geometry (line, polygon, point, ...) has a specific child editing controller.
 * 
 * @author Pieter De Graef
 */
public class ParentEditController extends EditController {

	/**
	 * The real controller responsible for editing.
	 */
	private EditController controller;

	private TransactionGeomIndex geometryIndex;

	private int pixelTolerance = -1;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public ParentEditController(MapWidget mapWidget) {
		super(mapWidget, null);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	public void onActivate() {
		menu = getContextMenu();
		mapWidget.setContextMenu(menu);
	}

	public void onDeactivate() {
		if (menu != null) {
			menu.destroy();
			menu = null;
			mapWidget.setContextMenu(null);
		}
		if (controller != null) {
			controller.onDeactivate();
		}
	}

	// -------------------------------------------------------------------------
	// EditController implementation:
	// -------------------------------------------------------------------------

	public Menu getContextMenu() {
		if (menu == null) {
			menu = new Menu();
			menu.addItem(new NewFeatureAction(mapWidget, this));
			menu.addItem(new EditFeatureAction(mapWidget, this));
			menu.addItem(new DeleteFeatureAction(mapWidget, this));
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new ToggleSelectionAction(mapWidget, 5));
		}
		return menu;
	}

	public TransactionGeomIndex getGeometryIndex() {
		return geometryIndex;
	}

	public void setGeometryIndex(TransactionGeomIndex geometryIndex) {
		if (controller != null) {
			controller.setGeometryIndex(geometryIndex);
		}
		this.geometryIndex = geometryIndex;
	}

	public void cleanup() {
		if (controller != null) {
			controller.cleanup();
			setController(null);
		}
	}

	public void showGeometricInfo() {
		if (controller != null) {
			controller.showGeometricInfo();
		}
	}

	public void updateGeometricInfo() {
		if (controller != null) {
			controller.updateGeometricInfo();
		}
	}

	public void hideGeometricInfo() {
		if (controller != null) {
			controller.hideGeometricInfo();
		}
	}

	// -------------------------------------------------------------------------
	// Event handler functions delegating to child controller:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		if (controller != null) {
			controller.onMouseDown(event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if (controller != null) {
			controller.onMouseUp(event);
		} else if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			// Check if we can toggle selection on a feature:
			ToggleSelectionAction action = new ToggleSelectionAction(mapWidget, getPixelTolerance());
			action.toggle(getScreenPosition(event), true);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (controller != null) {
			controller.onMouseMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (controller != null) {
			controller.onMouseOut(event);
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (controller != null) {
			controller.onMouseOver(event);
		}
	}

	public void onMouseWheel(MouseWheelEvent event) {
		if (controller != null) {
			controller.onMouseWheel(event);
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (controller != null) {
			controller.onDoubleClick(event);
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setEditMode(EditMode currentMode) {
		super.setEditMode(currentMode);
		if (controller != null) {
			controller.setEditMode(currentMode);
		}
	}

	public EditController getController() {
		return controller;
	}

	/**
	 * Set a new child edit controller. If there was a previous child controller, then it's <code>onDeactivate</code>
	 * method will be called first. Likewise on the new controller, the <code>onActivate</code> will be called.
	 * 
	 * @param controller
	 *            The new child edit controller. A controller usually masters editing for one type of geometry. This
	 *            parameter can also be <code>null</code>, when no child edit controller is required anymore. If this
	 *            parameter is <code>null</code>, then this controller <code>onActivate</code> method is called.
	 */
	public void setController(EditController controller) {
		if (this.controller != null) {
			this.controller.onDeactivate();
		}
		this.controller = controller;
		if (controller != null) {
			controller.onActivate();
		} else {
			onActivate();
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Returns a pixel tolerance for selection. Tries to get it from the toolbar configuration for the selection
	 * controller. Otherwise returns a default of 5 pixels.
	 */
	private int getPixelTolerance() {
		if (pixelTolerance < 0) {
			pixelTolerance = 5;

			// First try and get the pixelTolerance value from the selection controller configuration:
			ToolbarInfo toolbarInfo = mapWidget.getMapModel().getDescription().getToolbar();
			for (ToolInfo tool : toolbarInfo.getTools()) {
				if ("SelectionMode".equals(tool.getId())) {
					ToolbarBaseAction action = ToolbarRegistry.getToolbarAction(tool.getId(), mapWidget);
					if (action instanceof SelectionModalAction) {
						for (Parameter parameter : tool.getParameters()) {
							((ConfigurableAction) action).configure(parameter.getName(), parameter.getValue());
						}
						pixelTolerance = ((SelectionModalAction) action).getPixelTolerance();
					}
				}
			}
		}
		return pixelTolerance;
	}
}
