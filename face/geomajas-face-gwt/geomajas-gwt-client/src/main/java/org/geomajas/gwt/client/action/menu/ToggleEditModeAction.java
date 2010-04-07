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
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Toggles the general edit mode between {@link #EditController.DRAG_MODE} and {@link #EditController.INSERT_MODE}.
 *
 * @author Pieter De Graef
 */
public class ToggleEditModeAction extends MenuAction implements MenuItemIfFunction {

	private ParentEditController editController;

	/**
	 * @param editController
	 *            The parent edit controller currently active.
	 */
	public ToggleEditModeAction(ParentEditController editController) {
		super(I18nProvider.getMenu().toggleEditMode(), null);
		this.editController = editController;
		setCheckIfCondition(this);
	}

	/**
	 * Toggle a label that shows geometric information about the geometry being edited.
	 *
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		if (editController.getEditMode() == EditMode.DRAG_MODE) {
			editController.setEditMode(EditMode.INSERT_MODE);
		} else {
			editController.setEditMode(EditMode.DRAG_MODE);
		}
	}

	/**
	 * This menu item will be checked if the controller is in {@link #EditController.INSERT_MODE}.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		if (editController.getEditMode() == EditMode.DRAG_MODE) {
			return false;
		}
		return true;
	}
}
