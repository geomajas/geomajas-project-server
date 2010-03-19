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
import org.geomajas.gwt.client.controller.editing.EditController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Undo the last operation performed on the {@link FeatureTransaction}.
 *
 * @author Pieter De Graef
 */
public class UndoOperationAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private EditController controller;

	/**
	 * @param mapWidget
	 *            The map on which editing is going on.
	 * @param controller
	 *            The actual child edit controller, not the parent!
	 */
	public UndoOperationAction(MapWidget mapWidget, EditController controller) {
		super(I18nProvider.getMenu().undoOperation(), "[ISOMORPHIC]/geomajas/undo.png");
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Undo the last operation performed on the {@link FeatureTransaction}.
	 *
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		mapWidget.render(featureTransaction, RenderGroup.SCREEN, RenderStatus.DELETE);
		featureTransaction.undoLastOperation();
		mapWidget.render(featureTransaction, RenderGroup.SCREEN, RenderStatus.ALL);
		controller.cleanup();
	}

	// MenuItemIfFunction implementation:

	/**
	 * This function tries to find out whether or not this menu item should be enabled. Only if there are more then 1
	 * operations in the feature transaction operation queue, will this menu item be enabled.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			return featureTransaction.getOperationQueue().size() > 1; // the first addCoordinateOp may not be undone.
		}
		return false;
	}
}
