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

package org.geomajas.test.client;

import org.geomajas.test.client.exporter.MapWidget;
import org.geomajas.test.client.exporter.MarkerMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	public Application() {
	}

	public void onModuleLoad() {
		GWT.create(MarkerMap.class);
		final MapWidget map = new MapWidget("mainMap", "app");
		map.setHtmlElement(DOM.getElementById("map"));
		map.setWidth(DOM.getElementById("map").getStyle().getWidth());
		map.setHeight(DOM.getElementById("map").getStyle().getHeight());
		map.draw();
		onLoadImpl();
	}

	private native void onLoadImpl() /*-{
		if ($wnd.geomajasOnLoad && typeof $wnd.geomajasOnLoad == 'function') $wnd.geomajasOnLoad();
	}-*/;

}
