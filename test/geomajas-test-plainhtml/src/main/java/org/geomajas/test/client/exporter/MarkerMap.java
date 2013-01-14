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
package org.geomajas.test.client.exporter;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.test.client.util.GeoUtil;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Exportable extension of MapWidget that supports adding markers. It is assumed that the CRS of the map is the Google
 * CRS (EPSG:900913).
 * 
 * Usage is: <code><pre>
 *  var map = new org.geomajas.MarkerMap("exporterMap", "app");
 * 	map.setHtmlElementId("exportermap");
 * 	map.setWidth("400px");
 * 	map.setHeight("300px");
 * 	map.draw();
 * 	map.addOnLoad(function() {
 * 		map.setCenter(51.05, 3.716667);
 * 		map.setZoomLevel(10);
 * 		map.addMarker("some marker", 51, 3.7, "images/marker.png", 34, 34);
 * 	});
 * 	</pre></code>
 * 
 * 
 * @author Jan De Moerloose
 * 
 */
@Export
@ExportPackage("org.geomajas")
public class MarkerMap extends MapWidget {

	/**
	 * Constructs a map.
	 * @param id map id
	 * @param applicationId application id
	 */	
	public MarkerMap(String id, String applicationId) {
		super(id, applicationId);
		registerPainter(new MarkerPainter());
	}

	/**
	 * Sets the center of the map.
	 * @param lat latitude of the center
	 * @param lon longitude of the center
	 */
	public void setCenter(double lat, double lon) {
		getMapModel().getMapView().setCenterPosition(GeoUtil.convertToGoogle(new Coordinate(lat, lon)));
	}

	/**
	 * Sets the zoom level of the map.
	 * @param zoomLevel Google zoom level (0=256x256 pixels, 1=512x512 pixels, ...)
	 */
	public void setZoomLevel(int zoomLevel) {
		getMapModel().getMapView().setCurrentScale(GeoUtil.getScaleForZoomLevel(zoomLevel), ZoomOption.LEVEL_CLOSEST);
	}

	/**
	 * Adds a closure function to the map model change event.
	 * @param handler the closure function
	 */
	public void addOnLoad(final JsClosure handler) {
		getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				handler.execute();
			}
		});
	}

	/**
	 * Adds a marker to the map.
	 * @param id id of the marker
	 * @param lat latitude of the marker point
	 * @param lon longitude of the marker pint
	 * @param imageSrc src of the marker image
	 * @param width width of the marker image
	 * @param height height of the marker image
	 */
	public void addMarker(String id, double lat, double lon, String imageSrc, int width, int height) {
		registerWorldPaintable(new Marker(id, imageSrc, GeoUtil.convertToGoogle(new Coordinate(lat, lon)), width,
				height));
	}
}
