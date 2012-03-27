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

package org.geomajas.plugin.editing.gwt.client.gfx;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class EdgeMarkerHandler implements MouseOutHandler, MouseOverHandler, MouseMoveHandler, MapDownHandler {

	private static final int MARKER_SIZE = 6;

	private Composite edgeMarkerGroup = new Composite("edge-marker");

	private ShapeStyle style = new ShapeStyle("#444444", 0f, "#444444", 1f, 1);

	private MapWidget mapWidget;

	private GeometryEditService service;

	private MapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected EdgeMarkerHandler(MapWidget mapWidget, GeometryEditService service, MapEventParser eventParser) {
		this.mapWidget = mapWidget;
		this.service = service;
		this.eventParser = eventParser;
		
		mapWidget.getMapModel().getMapView().addMapViewChangedHandler(new MapViewChangedHandler() {
			
			public void onMapViewChanged(MapViewChangedEvent event) {
				cleanup();
			}
		});
	}

	// ------------------------------------------------------------------------
	// MapEventParser implementation:
	// ------------------------------------------------------------------------

	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		return eventParser.getLocation(event, renderSpace);
	}

	public Element getTarget(HumanInputEvent<?> event) {
		return eventParser.getTarget(event);
	}

	// ------------------------------------------------------------------------
	// Handler implementations:
	// ------------------------------------------------------------------------

	public void onDown(HumanInputEvent<?> event) {
		cleanup();
	}

	public void onMouseOut(MouseOutEvent event) {
		cleanup();
	}

	public void onMouseOver(MouseOverEvent event) {
		cleanup();
	}

	public void onMouseMove(MouseMoveEvent event) {
		drawEdgeHighlightMarker(eventParser.getLocation(event, RenderSpace.SCREEN));
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void cleanup() {
		mapWidget.getVectorContext().deleteGroup(edgeMarkerGroup);
	}

	private void drawEdgeHighlightMarker(Coordinate location) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			cleanup();
			Coordinate tl = new Coordinate(location.getX() - MARKER_SIZE, location.getY() + MARKER_SIZE);
			Coordinate tr = new Coordinate(location.getX() + MARKER_SIZE, location.getY() + MARKER_SIZE);
			Coordinate bl = new Coordinate(location.getX() - MARKER_SIZE, location.getY() - MARKER_SIZE);
			Coordinate br = new Coordinate(location.getX() + MARKER_SIZE, location.getY() - MARKER_SIZE);

			GeometryFactory factory = mapWidget.getMapModel().getGeometryFactory();
			LineString top = factory.createLineString(new Coordinate[] { tl, tr });
			LineString right = factory.createLineString(new Coordinate[] { tr, br });
			LineString bottom = factory.createLineString(new Coordinate[] { bl, br });
			LineString left = factory.createLineString(new Coordinate[] { bl, tl });

			mapWidget.getVectorContext().drawGroup(mapWidget.getGroup(RenderGroup.SCREEN), edgeMarkerGroup);
			mapWidget.getVectorContext().drawLine(edgeMarkerGroup, "top", top, style);
			mapWidget.getVectorContext().drawLine(edgeMarkerGroup, "right", right, style);
			mapWidget.getVectorContext().drawLine(edgeMarkerGroup, "bottom", bottom, style);
			mapWidget.getVectorContext().drawLine(edgeMarkerGroup, "left", left, style);
		}
	}
}