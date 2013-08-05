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
import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Pan tool, allows dragging the map too pan/scroll, or selecting an area to zoom to (by pressing shift or control).
 * 
 * @author Joachim Van der Auwera
 */
public class PanModalAction extends ToolbarModalAction {

	private MapWidget mapWidget;

	private PanController controller;

	public PanModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconPan, I18nProvider.getToolbar().panTitle(), I18nProvider.getToolbar()
				.panTooltip());
		this.mapWidget = mapWidget;
		controller = new PanController(mapWidget);
	}

	@Override
	public void onSelect(ClickEvent event) {
		mapWidget.setController(controller);
	}

	@Override
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}
}
