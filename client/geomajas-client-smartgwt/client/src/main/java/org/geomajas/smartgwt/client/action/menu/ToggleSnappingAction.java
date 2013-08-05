/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.smartgwt.client.action.menu;

import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.smartgwt.client.controller.AbstractSnappingController;
import org.geomajas.smartgwt.client.i18n.MenuMessages;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.smartgwt.client.spatial.snapping.Snapper;

/**
 * <p>
 * Context menu action that toggles snapping for the given {@link org.geomajas.smartgwt.client.controller
 * .AbstractSnappingController} (usually the active controller). Snapping always occurs,
 * using the snapping rules of the selected layer.
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
			controller.activateSnapping(layer.getLayerInfo().getSnappingRules(), Snapper.SnapMode.ALL_GEOMETRIES_EQUAL);
		}
	}
}
