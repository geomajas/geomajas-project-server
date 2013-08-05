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
import org.geomajas.smartgwt.client.controller.editing.EditController;
import org.geomajas.smartgwt.client.i18n.I18nProvider;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.smartgwt.client.util.WidgetLayout;

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
		super(I18nProvider.getMenu().toggleGeometricInfo(), WidgetLayout.iconInfo);
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
