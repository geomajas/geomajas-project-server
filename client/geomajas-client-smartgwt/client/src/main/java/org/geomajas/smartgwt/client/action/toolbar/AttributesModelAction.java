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
package org.geomajas.smartgwt.client.action.toolbar;

import org.geomajas.smartgwt.client.action.ToolbarModalAction;
import org.geomajas.smartgwt.client.controller.editing.ParentEditController;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * This action opens the feature attribute editor.
 * 
 * @author Jan De Moerloose
 */
public class AttributesModelAction extends ToolbarModalAction {

	private MapWidget mapWidget;

	public AttributesModelAction(MapWidget mapWidget) {
		super("edit.png", I18nProvider.getToolbar().editingSelectTitle(), I18nProvider.getToolbar()
				.editingSelectTooltip());
		this.mapWidget = mapWidget;
	}

	public void onSelect(ClickEvent event) {
		mapWidget.setController(new ParentEditController(mapWidget));
	}

	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}
}
