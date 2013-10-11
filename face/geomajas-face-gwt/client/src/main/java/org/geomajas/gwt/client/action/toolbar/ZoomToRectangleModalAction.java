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

package org.geomajas.gwt.client.action.toolbar;

import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.ZoomToRectangleController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Allow zooming to the selected rectangle.
 * 
 * @author Joachim Van der Auwera
 */
public class ZoomToRectangleModalAction extends ToolbarModalAction {

	private MapWidget mapWidget;

	private ZoomToRectangleController controller;

	public ZoomToRectangleModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconZoomSelection, I18nProvider.getToolbar().zoomToRectangleTitle(),
				I18nProvider.getToolbar().zoomToRectangleTooltip());
		this.mapWidget = mapWidget;
		controller = new ZoomToRectangleController(mapWidget);
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
