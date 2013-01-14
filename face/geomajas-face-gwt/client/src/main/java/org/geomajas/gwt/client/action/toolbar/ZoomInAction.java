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
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Tool which allows zooming in directly.
 * 
 * @author Pieter De Graef
 */
public class ZoomInAction extends ToolbarAction implements ConfigurableAction {

	private MapWidget mapWidget;

	private double zoomFactor = 2.0;

	public ZoomInAction(MapWidget mapWidget) {
		super(WidgetLayout.iconZoomIn, I18nProvider.getToolbar().zoomInTitle(), I18nProvider
				.getToolbar().zoomInTooltip());
		this.mapWidget = mapWidget;
	}

	public void onClick(ClickEvent event) {
		mapWidget.getMapModel().getMapView().scale(zoomFactor, ZoomOption.LEVEL_CHANGE);
	}

	public void configure(String key, String value) {
		if ("delta".equals(key)) {
			zoomFactor = Double.parseDouble(value);
		}
	}
}
