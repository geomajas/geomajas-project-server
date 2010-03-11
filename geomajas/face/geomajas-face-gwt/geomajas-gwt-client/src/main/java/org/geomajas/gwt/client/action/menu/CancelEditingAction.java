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
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Action that stops the editing and also removes any {@link org.geomajas.gwt.client.map.feature.FeatureTransaction}
 * from the {@link org.geomajas.gwt.client.map.feature.FeatureEditor}.
 *
 * @author Pieter De Graef
 */
public class CancelEditingAction extends MenuAction {

	private MapWidget mapWidget;

	private ParentEditController controller;

	/**
	 * Constructor for the cancel editing action.
	 *
	 * @param mapWidget
	 *            The <code>MapWidget</code> on which editing is in progress.
	 * @param controller
	 *            The current parent editing controller active on the map.
	 */
	public CancelEditingAction(MapWidget mapWidget, ParentEditController controller) {
		super(I18nProvider.getMenu().cancelEditing(), "[ISOMORPHIC]/geomajas/quit.png");
		this.mapWidget = mapWidget;
		this.controller = controller;
	}

	/**
	 * Cancel's editing, and also removes the FeatureTransaction object from the map.
	 *
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			controller.cleanup();
			mapWidget.render(featureTransaction, "delete");
			mapWidget.getMapModel().getFeatureEditor().stopEditing();
		}
	}
}
