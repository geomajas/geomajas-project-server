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

package org.geomajas.gwt.client.action.menu;

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.controller.AbstractSnappingController;
import org.geomajas.gwt.client.i18n.MenuMessages;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.snapping.Snapper.SnapMode;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * <p>
 * Context menu action that toggles snapping for the given {@link AbstractSnappingController} (usually the active
 * controller). Snapping always occurs, using the snapping rules of the selected layer.
 * </p>
 * <p>
 * This action is checked when snapping is already active, and unchecked when snapping is not active. Clicking the
 * action will effectively toggle this setting. Also this action is only enabled when the MapModel has a selected layer,
 * and that selected layer is a VectorLayer and that VectorLayer has at least 1 snapping rule.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ToggleSnappingAction extends MenuAction {

	/** Snapping occurs using the snapping rules of this layer. */
	private final VectorLayer layer;

	/** The controller onto whom to activate snapping. */
	private final AbstractSnappingController controller;

	// Constructors:

	public ToggleSnappingAction(final VectorLayer layer, final AbstractSnappingController controller) {
		super(((MenuMessages) GWT.create(MenuMessages.class)).toggleMeasureSnapping(), null);
		this.layer = layer;
		this.controller = controller;

		setCheckIfCondition(new MenuItemIfFunction() {

			public boolean execute(Canvas target, Menu menu, MenuItem item) {
				return controller.isSnappingActive();
			}
		});

		setEnableIfCondition(new MenuItemIfFunction() {

			public boolean execute(Canvas target, Menu menu, MenuItem item) {
				if (layer != null) {
					return layer.getLayerInfo().getSnappingRules() != null
							&& !layer.getLayerInfo().getSnappingRules().isEmpty();
				}
				return false;
			}
		});
	}

	// ClickHandler implementation:

	public void onClick(MenuItemClickEvent event) {
		if (controller.isSnappingActive()) {
			controller.deactivateSnapping();
		} else if (layer != null) {
			controller.activateSnapping(layer.getLayerInfo().getSnappingRules(), SnapMode.ALL_GEOMETRIES_EQUAL);
		}
	}
}
