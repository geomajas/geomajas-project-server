/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.jsapi.map.LayersModel;
import org.geomajas.jsapi.map.Map;
import org.geomajas.jsapi.map.ViewPort;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.user.client.DOM;

/**
 * Exportable extension of MapWidget, in fact a facade for {@link org.geomajas.gwt.client.widget.MapWidget}. Usage
 * directly from javascript is: <code><pre>
 *  var map = new org.geomajas.map.Map("map", "app");
 * 	map.setHtmlElementId("map");
 * 	map.setWidth("400px");
 * 	map.setHeight("300px");
 * 	map.draw();
 * 	</pre></code>
 * 
 * 
 * Initialization is also possible from GWT, after which this mapwidget facade is available from javascript. <code><pre>
 * 	final Map mapWidget = new Map("map", "app");
 * 	Map map = new Map(mapWidget);
 * </pre></code>
 * 
 * 
 * @author Pieter De Graef
 * @author Oliver May
 */
@Export
@ExportPackage("org.geomajas.jsapi.map")
public class MapImpl implements Exportable, Map {

	private MapWidget mapWidget;

	private ViewPort viewPort;

	private LayersModel layersModel;

	// If this is removed, we get errors from the GWT exporter...
	public MapImpl() {
	}

	/**
	 * Create a facade for the given {@link org.geomajas.gwt.client.widget.MapWidget}, available trough javascript.
	 * 
	 * @param mapWidget
	 *            the {@link org.geomajas.gwt.client.widget.MapWidget} object.
	 * @since 1.0.0
	 */
	@Api
	public MapImpl(String applicationId, String mapId) {
		this.mapWidget = new MapWidget(mapId, applicationId);
		viewPort = new ViewPortImpl(mapWidget.getMapModel().getMapView());
		layersModel = new LayersModelImpl(mapWidget.getMapModel());
	}

	// ------------------------------------------------------------------------
	// Map implementation:
	// ------------------------------------------------------------------------

	public void setHtmlElementId(String id) {
		mapWidget.setHtmlElement(DOM.getElementById(id));
		mapWidget.draw();
	}

	public LayersModel getLayersModel() {
		return layersModel;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

	public void setSize(int width, int height) {
		mapWidget.setWidth(width);
		mapWidget.setHeight(height);
	}

	// ------------------------------------------------------------------------
	// Other public metho:ds
	// ------------------------------------------------------------------------

	@NoExport
	public MapWidget getMapWidget() {
		return mapWidget;
	}
}