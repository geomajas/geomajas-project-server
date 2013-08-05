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

import org.geomajas.smartgwt.client.action.ConfigurableAction;
import org.geomajas.smartgwt.client.action.ToolbarModalAction;
import org.geomajas.smartgwt.client.controller.ZoomOnClickController;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Tool which allows zooming in on a position of the map by clicking it.
 * 
 * @author Joachim Van der Auwera
 */
public class ZoomInModalAction extends ToolbarModalAction implements ConfigurableAction {

	private MapWidget mapWidget;

	private ZoomOnClickController controller;

	public ZoomInModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconZoomIn, I18nProvider.getToolbar().zoomInTitle(), I18nProvider
				.getToolbar().zoomInTooltip());
		this.mapWidget = mapWidget;
		controller = new ZoomOnClickController(mapWidget, 2.0);
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
