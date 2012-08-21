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
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
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
public class GoogleAddon extends MapAddon {

	private static final String EPSG_900913 = "EPSG:900913";

	private static final String EPSG_3857 = "EPSG:3857";

	private static final String EPSG_4326 = "EPSG:4326";

	private static final double HALF_CIRCLE = 180.0;

	private static final double MERCATOR_WIDTH = Math.PI * 6378137.0;

	private static final int VERTICAL_MARGIN = 20;

	private static final double LN2 = Math.log(2.0);

	private final MapWidget map;

	private JavaScriptObject googleMap;

	private MapType type;

	private final boolean showMap;
	
	private Element tosGroup;
	
	private boolean visible = true;

	/**
	 * Google map types as defined by the API.
	 * 
	 * @since 1.9.0
	 */
	@Api
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
		setRepaintOnMapViewChange(true);
		this.map = map;
		this.type = type;
		this.showMap = showMap;
		// this.map.getMapModel().getMapView().addMapViewChangedHandler(this);

		// Default placement:
		setVerticalAlignment(VerticalAlignment.BOTTOM);
		setVerticalMargin(VERTICAL_MARGIN);
	}

	// MapAddon implementation:

	/** {@inheritDoc} */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		if (googleMap != null) {
			String sourceCrs = map.getMapModel().getCrs();
			if (isGoogleProjection(sourceCrs)) {
				int zoomLevel = calcZoomLevel(map.getMapModel().getMapView().getCurrentScale());
				Coordinate latLon = convertToLatLon(bounds.getCenterPoint());
				fitGoogleMapBounds(googleMap, latLon, zoomLevel);
			} else {
				// transform on server
				TransformGeometryRequest request = new TransformGeometryRequest();
				request.setBounds(new org.geomajas.geometry.Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(),
						bounds.getHeight()));
				request.setSourceCrs(map.getMapModel().getCrs());
				request.setTargetCrs(EPSG_3857);
				GwtCommand command = new GwtCommand(TransformGeometryRequest.COMMAND);
				command.setCommandRequest(request);
				GwtCommandDispatcher.getInstance().execute(command,
						new AbstractCommandCallback<TransformGeometryResponse>() {

							public void execute(TransformGeometryResponse response) {
								Bbox google = new Bbox(response.getBounds());
								int zoomLevel = calcZoomLevelFromBounds(google);
								fitGoogleMapBounds(googleMap, convertToLatLon(google.getCenterPoint()), zoomLevel);
							}
						});
			}
		}
	}

	private int calcZoomLevel(double scale) {
		int calc = (int) Math.round(Math.log(scale * MERCATOR_WIDTH / 180) / LN2);
		return calc;
	}

	private int calcZoomLevelFromBounds(Bbox google) {
		return calcZoomLevel(map.getWidth() / google.getWidth());
	}

	private void fitGoogleMapBounds(JavaScriptObject object, Coordinate center, int zoomLevel) {
		doFitGoogleMapBounds(object, center.getX(), center.getY(), zoomLevel);
	}

	@Override
	public void setMapSize(int mapWidth, int mapHeight) {
		super.setMapSize(mapWidth, mapHeight);
		if (googleMap != null) {
			triggerResize(googleMap);
		}
	}

	/** {@inheritDoc} */
	public void onDraw() {
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
	
	private void moveTosCopyRight() {
		// move the ToS and copyright to the top
		// create a div group in the graphics context
		if (tosGroup == null) {
			String graphicsId = map.getVectorContext().getId();
			Element graphics = DOM.getElementById(graphicsId);
			tosGroup = DOM.createDiv();
			tosGroup.setId(map.getID() + "-googleAddon");
			tosGroup.getStyle().setBottom(VERTICAL_MARGIN, Unit.PX);
			graphics.appendChild(tosGroup);
			UIObject.setVisible(tosGroup, visible);
		}
		String mapsId = map.getRasterContext().getId(this);
		Element gmap = DOM.getElementById(mapsId);
		if (gmap.getChildCount() > 0) {
			Node baseMap = gmap.getChild(0);
			if (baseMap.getChildCount() > 2) {
				Node copyright = baseMap.getChild(1);
				Node tos = baseMap.getChild(2);
				tosGroup.appendChild(copyright);
				tosGroup.appendChild(tos);
			}
		}
	}
	
	/**
	 * Set the visibility of the Google map.
	 * 
	 * @param visible
	 * @since 1.9.0
	 */
	@Api
	public void setVisible(boolean visible) {
		this.visible = visible;
		if (googleMap != null) {
			String mapsId = map.getRasterContext().getId(this);
			Element gmap = DOM.getElementById(mapsId);
			UIObject.setVisible(gmap, visible);
			if (tosGroup != null) {
				UIObject.setVisible(tosGroup, visible);
			}
			if (visible) {
				triggerResize(googleMap);
			}
		}
	}
	
	/**
	 * Set the type of the Google map.
	 * 
	 * @param type the map type
	 * @since 1.9.0
	 */
	@Api
	public void setMapType(MapType type) {
		this.type = type;
		if (googleMap != null) {
			setMapType(googleMap, type.toString());
		}
	}
	
	/**
	 * Get the type of the Google map.
	 * 
	 * @return
	 * @since 1.9.0
	 */
	@Api
	public MapType getMapType() {
		return type;
	}	
	

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private native void setMapType(JavaScriptObject map, String mapType)
	/*-{
		if (mapType == "NORMAL") {
		 	map.setMapTypeId($wnd.google.maps.MapTypeId.ROADMAP);
		} else if (mapType == "SATELLITE") {
		 	map.setMapTypeId($wnd.google.maps.MapTypeId.SATELLITE);
		} else if (mapType == "HYBRID") {
		 	map.setMapTypeId($wnd.google.maps.MapTypeId.HYBRID);
		} else if (mapType == "PHYSICAL") {
		 	map.setMapTypeId($wnd.google.maps.MapTypeId.TERRAIN);
		}  
	}-*/;

	private boolean isGoogleProjection(String sourceCrs) {
		return EPSG_900913.equals(sourceCrs) || EPSG_3857.equals(sourceCrs);
	}

	private native void triggerResize(JavaScriptObject map)
	/*-{
		$wnd.google.maps.event.trigger(map, "resize");
	}-*/;

	private native JavaScriptObject createGoogleMap(String mapId, String graphicsId, String mapType, boolean showMap,
			int verticalMargin, int horizontalMargin, String verticalAlignment)
	/*-{
	 	var _me = this;
	 	var mapDiv = $doc.getElementById(mapId);
	 	var options = {disableDefaultUI: true};
		if (mapType == "NORMAL") {
			options.mapTypeId = $wnd.google.maps.MapTypeId.ROADMAP;
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		} else if (mapType == "SATELLITE") {
			options.mapTypeId = $wnd.google.maps.MapTypeId.SATELLITE;
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		} else if (mapType == "HYBRID") {
			options.mapTypeId = $wnd.google.maps.MapTypeId.HYBRID;
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		} else if (mapType == "PHYSICAL") {
			options.mapTypeId = $wnd.google.maps.MapTypeId.TERRAIN;
		 	var map = new $wnd.google.maps.Map(mapDiv, options)
		}  
		if(!showMap) {
			mapDiv.style.visibility = "hidden";
		}
		$wnd.google.maps.event.addListener(map, 'tilesloaded', 
			function(){
				_me.@org.geomajas.layer.google.gwt.client.GoogleAddon::moveTosCopyRight()();
			}
		); 

		return map;
	}-*/;

	private native void doFitGoogleMapBounds(JavaScriptObject map, double xCenter, double yCenter, int zoomLevel)
	/*-{
		var center = new $wnd.google.maps.LatLng(xCenter, yCenter);
		map.setZoom(zoomLevel);
		map.setCenter(center);
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
