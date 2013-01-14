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
