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
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Menu action that deletes a selected feature on the map.
 * 
 * @author Pieter De Graef
 */
public class DeleteFeatureAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private ParentEditController controller;

	private Feature feature;

	/**
	 * Constructor for the menu action to edit a selected feature on the map.
	 * 
	 * @param mapWidget
	 *            The <code>MapWidget</code> on which editing is in progress.
	 * @param controller
	 *            The current parent editing controller active on the map.
	 */
	public DeleteFeatureAction(MapWidget mapWidget, ParentEditController controller) {
		super(I18nProvider.getMenu().deleteFeature(), "[ISOMORPHIC]/geomajas/vector-remove.png");
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Prepare a FeatureTransaction for deletion of the selected feature, and ask if the user wants to continue. If
	 * he/she does, the feature will be deselected and then the FeatureTransaction will be committed.
	 */
	public void onClick(MenuItemClickEvent event) {
		if (feature != null && feature.isSelected()) {
			SC.confirm(
					I18nProvider.getGlobal().confirmDeleteFeature(feature.getLabel(), feature.getLayer().getLabel()),
					new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								feature.getLayer().deselectFeature(feature);
								mapWidget.getMapModel().getFeatureEditor()
										.startEditing(new Feature[] {feature}, null);
								SaveEditingAction action = new SaveEditingAction(mapWidget, controller);
								action.onClick(null);
							}
						}
					});
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled if a vector-layer is selected that allows the deleting of
	 * existing features.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		Object o = mapWidget.getGraphics().getRightButtonObject();
		if (o != null && o instanceof Feature) {
			Feature feature = (Feature) o;
			if (feature.isSelected()) {
				return feature.isDeletable();
			}
		}
		return false;
	}
}
