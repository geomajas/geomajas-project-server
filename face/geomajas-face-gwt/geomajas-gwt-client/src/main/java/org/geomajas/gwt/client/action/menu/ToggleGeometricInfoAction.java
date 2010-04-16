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

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Toggle a label that shows geometric information about the geometry being edited.
 *
 * @author Pieter De Graef
 */
public class ToggleGeometricInfoAction extends MenuAction {

	private EditController editController;

	/**
	 * @param editController
	 *            The edit controller currently active.
	 */
	public ToggleGeometricInfoAction(EditController editController) {
		super(I18nProvider.getMenu().toggleGeometricInfo(), "[ISOMORPHIC]/geomajas/osgeo/info.png");
		this.editController = editController;
	}

	/**
	 * Toggle a label that shows geometric information about the geometry being edited.
	 *
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		if (editController.getInfoLabel() == null) {
			editController.showGeometricInfo();
		} else {
			editController.hideGeometricInfo();
		}
	}
}
