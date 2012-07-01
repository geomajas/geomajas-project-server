/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.google.gwt.client;

import org.geomajas.annotation.Api;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.MapAddon;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.types.VerticalAlignment;

/**
 * <p>
 * Map add-on that displays the Google attribution (copyright + terms of use) on the map. It can also show the actual
 * Google map as opposed to the tiles that are calculated on the server. For normal Geomajas use this map should be
 * hidden as it does not automatically scale to continuous zoom levels. This add-on requires the Google maps library !
 * </p>
 * <p>
 * This MapAddon does not allow center positioning. Also it takes in the whole width of the map, so setting the
 * horizontal alignment is of little use. By default, this MapAddon will be placed 20 pixels from the bottom of the map.
 * </p>
 *
 * @author Jan De Moerloose
 * @since 1.8.0
 */
@Api
public class GoogleAddon extends MapAddon implements MapViewChangedHandler {

	private static final String EPSG_900913 = "EPSG:900913";
	private static final String EPSG_3857 = "EPSG:3857";
	private static final String EPSG_4326 = "EPSG:4326";
	
	private static final double HALF_CIRCLE = 180.0;
	private static final double MERCATOR_WIDTH = Math.PI * 6378137.0;
	private static final int VERTICAL_MARGIN = 20;

	private final MapWidget map;

	private JavaScriptObject googleMap;

	private final MapType type;

	private final boolean showMap;

	/**
	 * Google map types as defined by the API.
	 */
	public enum MapType {
		NORMAL, SATELLITE, HYBRID, PHYSICAL
	}

	// Constructor:
	/**
	 * Create the Google addon to assure the copyright details and ToS are displayed on the map. The map itself is
	 * displayed in the background, any other raster layers will be displayed in front of it.
	 * 
	 * @param id element id
	 * @param map map widget to display copyright on
	 * @param type map type
	 * @since 1.9.0
	 */
	@Api
	public GoogleAddon(String id, MapWidget map, MapType type) {
		this(id, map, type, true);
	}

	/**
	 * Create the Google addon to assure the copyright details are displayed on the map. WARNING: deprecated because of
	 * change in <a href="https://developers.google.com/maps/terms">Google Maps Terms of Service</a>.
	 * 
	 * @param id element id
	 * @param map map widget to display copyright on
	 * @param type map type
	 * @param showMap should the map be visible?
	 * @deprecated use {{@link #GoogleAddon(String, MapWidget, MapType)}
	 */
	@Api
	@Deprecated
	public GoogleAddon(String id, MapWidget map, MapType type, boolean showMap) {
		super(id, 0, 0);
		this.map = map;
		this.type = type;
		this.showMap = showMap;
		this.map.getMapModel().getMapView().addMapViewChangedHandler(this);

		// Default placement:
		setVerticalAlignment(VerticalAlignment.BOTTOM);
		setVerticalMargin(VERTICAL_MARGIN);
	}

	// MapAddon implementation:

	/** {@inheritDoc} */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		if (googleMap == null) {
			// create as first child of raster group
			map.getRasterContext().drawGroup(null, this);			
			String id = map.getRasterContext().getId(this);
			// move to first position
			Element mapDiv = DOM.getElementById(id);
			Element rasterGroup = DOM.getElementById(map.getRasterContext().getId(map.getGroup(RenderGroup.RASTER)));
			DOM.insertBefore(DOM.getParent(rasterGroup), mapDiv, rasterGroup);
				String graphicsId = map.getVectorContext().getId();
			googleMap = createGoogleMap(id, graphicsId, type.name(), showMap, getVerticalMargin(),
					getHorizontalMargin(), getVerticalAlignmentString());
		}
	}

	/** {@inheritDoc} */
	public void onDraw() {
	}

	/** {@inheritDoc} */
	public void onRemove() {
		String id = map.getRasterContext().getId(this);

		// Remove the terms of use:
		Element element = DOM.getElementById(id + "-googleAddon");
		if (element != null) {
			Element parent = DOM.getParent(element);
			parent.removeChild(element);
		}

		// Remove the Google map too:
		element = DOM.getElementById(id);
		if (element != null) {
			Element parent = DOM.getParent(element);
			parent.removeChild(element);
		}

		googleMap = null;
	}

	/** {@inheritDoc} */
	public void onMapViewChanged(MapViewChangedEvent event) {
		// assume google coordinates here
		if (googleMap != null) {
			String sourceCrs = map.getMapModel().getCrs();
			if (isGoogleProjection(sourceCrs)) {
				Bbox latLon = convertToLatLon(event.getBounds());
				fitGoogleMapBounds(googleMap, latLon.getX(), latLon.getY(), latLon.getMaxX(), latLon.getMaxY());
			} else {
				// transform on server
				Bbox latLon = convertToLatLon(event.getBounds());
				fitGoogleMapBounds(googleMap, latLon.getX(), latLon.getY(), latLon.getMaxX(), latLon.getMaxY());

				TransformGeometryRequest request = new TransformGeometryRequest();
				Bbox mapBounds = event.getBounds();
				request.setBounds(new org.geomajas.geometry.Bbox(mapBounds.getX(), mapBounds.getY(), mapBounds
						.getWidth(), mapBounds.getHeight()));
				request.setSourceCrs(map.getMapModel().getCrs());
				request.setTargetCrs(EPSG_4326);
				GwtCommand command = new GwtCommand(TransformGeometryRequest.COMMAND);
				command.setCommandRequest(request);
				GwtCommandDispatcher.getInstance().execute(command,
						new AbstractCommandCallback<TransformGeometryResponse>() {

							public void execute(TransformGeometryResponse response) {
								Bbox latLon = new Bbox(response.getBounds());
								fitGoogleMapBounds(googleMap, latLon.getY(), latLon.getX(), latLon.getMaxY(),
										latLon.getMaxX());
							}
						});
			}
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private boolean isGoogleProjection(String sourceCrs) {
		return EPSG_900913.equals(sourceCrs) || EPSG_3857.equals(sourceCrs);
	}

	private native JavaScriptObject createGoogleMap(String mapId, String graphicsId, String mapType, boolean showMap,
			int verticalMargin, int horizontalMargin, String verticalAlignment)
	/*-{
	 	var mapDiv = $doc.getElementById(mapId);
		if (mapType == "NORMAL") {
			var options = {mapTypeId: $wnd.google.maps.MapTypeId.ROADMAP};
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		} else if (mapType == "SATELLITE") {
			var options = {mapTypeId: $wnd.google.maps.MapTypeId.SATELLITE};
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		} else if (mapType == "HYBRID") {
			var options = {mapTypeId: $wnd.google.maps.MapTypeId.HYBRID};
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		} else if (mapType == "PHYSICAL") {
			var options = {mapTypeId: $wnd.google.maps.MapTypeId.TERRAIN};
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		}  
		if(!showMap) {
			mapDiv.style.visibility = "hidden";
		}

		return map;
	}-*/;

	private native void fitGoogleMapBounds(JavaScriptObject map, double xmin, double ymin, double xmax, double ymax)
	/*-{
		var sw = new $wnd.google.maps.LatLng(xmin, ymin);
		var ne = new $wnd.google.maps.LatLng(xmax, ymax);
		var bounds = new $wnd.google.maps.LatLngBounds(sw,ne);
		map.fitBounds(bounds); 
	}-*/;

	private Bbox convertToLatLon(Bbox bounds) {
		// convert corners
		Coordinate orig = convertToLatLon(bounds.getOrigin());
		Coordinate end = convertToLatLon(bounds.getEndPoint());
		return new Bbox(orig.getX(), orig.getY(), end.getX() - orig.getX(), end.getY() - orig.getY());
	}

	private Coordinate convertToLatLon(Coordinate coordinate) {
		double lat = (coordinate.getY() / MERCATOR_WIDTH) * HALF_CIRCLE;
		double lon = (coordinate.getX() / MERCATOR_WIDTH) * HALF_CIRCLE;
		lat = HALF_CIRCLE / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / HALF_CIRCLE)) - Math.PI / 2.0);
		return new Coordinate(lat, lon);
	}

	private String getVerticalAlignmentString() {
		// No center position, just top and bottom:
		VerticalAlignment align = getVerticalAlignment();
		if (align != null && align.equals(VerticalAlignment.TOP)) {
			return "top";
		}
		return "bottom";
	}
}
