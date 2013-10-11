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

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.ZoomOnClickController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Tool which allows zooming out at a position on the map by clicking it.
 * 
 * @author Joachim Van der Auwera
 */
public class ZoomOutModalAction extends ToolbarModalAction implements ConfigurableAction {

	private MapWidget mapWidget;

	private ZoomOnClickController controller;

	public ZoomOutModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconZoomOut, I18nProvider.getToolbar().zoomOutTitle(), I18nProvider
				.getToolbar().zoomOutTooltip());
		this.mapWidget = mapWidget;
		controller = new ZoomOnClickController(mapWidget, .5);
	}

	@Override
	public void onSelect(ClickEvent event) {
		mapWidget.setController(controller);
	}

	@Override
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}

	public void configure(String key, String value) {
		if ("delta".equals(key)) {
			double zoomFactor = Double.parseDouble(value);
			controller.setZoomFactor(zoomFactor);
		}
	}
}
