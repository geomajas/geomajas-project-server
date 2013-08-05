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
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.MapModel;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.smartgwt.client.util.WidgetLayout;

/**
 * Clear the list of selected features.
 *
 * @author Joachim Van der Auwera
 */
public class DeselectAllAction extends MenuAction {

	private MapModel mapModel;

	public DeselectAllAction(MapModel mapModel) {
		super(I18nProvider.getMenu().deselectAll(), WidgetLayout.iconSelectedDelete);
		this.mapModel = mapModel;
	}

	public void onClick(MenuItemClickEvent menuItemClickEvent) {
		mapModel.clearSelectedFeatures();
	}
}
