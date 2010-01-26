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
package org.geomajas.gwt.client.action.toolbar;

import com.smartgwt.client.widgets.events.ClickEvent;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.ZoomOnClickController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Tool which allows zooming in on a position of the map by clicking it.
 *
 * @author Joachim Van der Auwera
 */
public class ZoomInModalAction extends ToolbarModalAction implements ConfigurableAction {

	private MapWidget mapWidget;

	private ZoomOnClickController controller;

	public ZoomInModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/zoom-in.png", I18nProvider.getToolbar().zoomIn());
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
