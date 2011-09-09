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

import org.geomajas.global.FutureApi;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.jsapi.map.Map;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.MapRegistryImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.user.client.DOM;



/**
 * Exportable extension of MapWidget, in fact a facade for {@link org.geomajas.gwt.client.widget.MapWidget}. 
 * Usage directly from javascript is:
 * 	<code><pre>
 *  var map = new org.geomajas.map.Map("map", "app");
 *	map.setHtmlElementId("map");
 *	map.setWidth("400px");
 *	map.setHeight("300px");
 *	map.draw();
 *	</pre></code>
 * 
 * 
 * Initialisation is also possible from GWT, after which this mapwidget facade is available from javascript.
 * <code><pre>
 * 	final Map mapWidget = new Map("map", "app");
 *	Map map = new Map(mapWidget);
 * </pre></code>
 * 
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 *
 */
@Export
@ExportPackage("org.geomajas.jsapi.map")
public class MapImpl implements Exportable, Map {

	
	private MapWidget mapWidget;
	/**
	 * Constructor for the MapWidget that is available trough JavaScript. The consructor takes care of registring the
	 * widget in the {@link org.geomajas.plugin.jsapi.smartgwt.client.exporter.MapRegistryImpl} so it can be retrieved 
	 * from JavaScript.
	 * 
	 * @param id
	 * @param applicationId
	 * @since 1.0.0
	 */
	@FutureApi
	public MapImpl(String id, String applicationId) {
		this(new MapWidget(id, applicationId));
	}
	
	/**
	 * Create a facade for the given {@link org.geomajas.gwt.client.widget.MapWidget}, available trough javascript.
	 * 
	 * @param mapWidget the {@link org.geomajas.gwt.client.widget.MapWidget} object.
	 * @since 1.0.0
	 */
	@FutureApi
	public MapImpl(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		MapRegistryImpl.getInstance().registerMap(mapWidget.getID(), mapWidget.getApplicationId(), this);
	}

	public void initialize(String applicationId, String mapId) {
		this.mapWidget = new MapWidget(mapId, applicationId);
		MapRegistryImpl.getInstance().registerMap(mapWidget.getID(), mapWidget.getApplicationId(), this);
	}

	public void setHtmlElementId(String id) {
		mapWidget.setHtmlElement(DOM.getElementById(id));
	}

	public LayersModelImpl getLayersModel() {
		return new LayersModelImpl(mapWidget.getMapModel());
	}

	public ViewPortImpl getViewPort() {
		return new ViewPortImpl(mapWidget.getMapModel().getMapView());
	}

	public void setSize(int width, int height) {
		mapWidget.setWidth(width);
		mapWidget.setHeight(height);
	}

}
