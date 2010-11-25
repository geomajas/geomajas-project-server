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
