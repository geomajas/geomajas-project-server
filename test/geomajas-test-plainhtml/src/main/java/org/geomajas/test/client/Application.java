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
