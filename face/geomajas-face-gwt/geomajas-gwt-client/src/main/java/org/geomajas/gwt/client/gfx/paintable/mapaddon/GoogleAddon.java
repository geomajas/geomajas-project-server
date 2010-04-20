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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * <p>
 * Map add-on that displays the Google attribution (copyright + terms of use) on the map. It can also show the actual
 * Google map as opposed to the tiles that are calculated on the server. For normal Geomajas use this map should be
 * hidden as it does not automatically scale to continuous zoom levels. This add-on requires the Google maps library !
 * </p>
 *
 * @author Jan De Moerloose
 */
public class GoogleAddon extends MapAddon implements MapViewChangedHandler {

	private MapWidget map;

	private JavaScriptObject googleMap;
	
	private MapType type;

	private boolean showMap;
	
	private static final double MERCATOR_WIDTH = Math.PI * 6378137.0;

	/**
	 * Google map types as defined by the API.
	 */
	public enum MapType {
		NORMAL,
		SATELLITE,
		HYBRID,
		PHYSICAL
	}
	// Constructor:

	public GoogleAddon(String id, MapWidget map, MapType type, boolean showMap) {
		super(id, 0, 0);
		this.map = map;
		this.type = type;
		this.showMap = showMap;
		this.map.getMapModel().getMapView().addMapViewChangedHandler(this);
	}

	// MapAddon implementation:

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		if (googleMap == null) {
			// create as first child of raster group
			map.getRasterContext().drawGroup(null, this);
			String id = map.getRasterContext().getId(this);
			String graphicsId = map.getVectorContext().getId();
			googleMap = createGoogleMap(id, graphicsId, type.name(), showMap);
		}
	}
	public void onDraw() {
	}

	public void onRemove() {
	}

	public void onMapViewChanged(MapViewChangedEvent event) {
		// assume google coordinates here
		if (googleMap != null) {
			Bbox latLon = convertToLatLon(event.getBounds());
			fitGoogleMapBounds(googleMap, latLon.getX(), latLon.getY(), latLon.getMaxX(), latLon.getMaxY());
		}
	}
	
	private native JavaScriptObject createGoogleMap(String mapId, String graphicsId, String mapType, boolean showMap)
	/*-{
	 	var mapDiv = $doc.getElementById(mapId);
		var map = new $wnd.GMap2(mapDiv);		
		if (mapType == "NORMAL") {
			map.setMapType($wnd.G_NORMAL_MAP);
		} else if (mapType == "SATELLITE") {
			map.setMapType($wnd.G_SATELLITE_MAP);
		} else if (mapType == "HYBRID") {
			map.setMapType($wnd.G_HYBRID_MAP);
		} else if (mapType == "PHYSICAL") {
			map.setMapType($wnd.G_PHYSICAL_MAP);
		}  
		if(!showMap) {
			mapDiv.style.visibility = "hidden";
		}
		
		var graphics = $doc.getElementById(graphicsId);
		
		var poweredBy = mapDiv.lastChild;
		mapDiv.removeChild(poweredBy);
		graphics.appendChild(poweredBy);
		poweredBy.style.bottom = "20px";

		var termsOfUse = mapDiv.lastChild;
		mapDiv.removeChild(termsOfUse);
		graphics.appendChild(termsOfUse);
		termsOfUse.style.bottom = "20px";
		
		return map;
	}-*/;

	private native void fitGoogleMapBounds(JavaScriptObject map, double xmin, double ymin, double xmax, double ymax)
	/*-{
		var sw = new $wnd.GLatLng(xmin, ymin);
		var ne = new $wnd.GLatLng(xmax, ymax);
		var bounds = new $wnd.GLatLngBounds(sw,ne);
		map.setCenter(bounds.getCenter(), map.getBoundsZoomLevel(bounds)); 
	}-*/;


	private Bbox convertToLatLon(Bbox bounds) {
		// convert corners
		Coordinate orig = convertToLatLon(bounds.getOrigin());
		Coordinate end = convertToLatLon(bounds.getEndPoint());
		return new Bbox(orig.getX(), orig.getY(), end.getX() - orig.getX(), end.getY() - orig.getY());
	}

	private Coordinate convertToLatLon(Coordinate coordinate) {
		double lat = (coordinate.getY() / MERCATOR_WIDTH) * 180.0;
		double lon = (coordinate.getX() / MERCATOR_WIDTH) * 180.0;
		lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);
		return new Coordinate(lat, lon);
	}


}
