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

package org.geomajas.plugin.editing.client.gfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.plugin.editing.client.controller.CompositeGeometryIndexController;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditDeleteEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditDeselectedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightBeginEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightEndEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionBeginEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionEndEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditOperationHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditSelectedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditSelectionHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditWorkflowHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexDragSelectionHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexHighlightHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexInsertHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexSelectHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexSnapToDeleteHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Cursor;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GeometryRenderer implements GeometryEditWorkflowHandler, GeometryEditHighlightHandler,
		GeometryEditOperationHandler, GeometryEditChangeStateHandler, GeometryEditSelectionHandler,
		GeometryEditMarkForDeletionHandler, MapViewChangedHandler {

	private StyleService styleService = new DefaultStyleService();

	private MapWidget mapWidget;

	private GeometryEditingService editingService;

	private Map<String, Composite> groups = new HashMap<String, Composite>();

	private String baseName = "editing";

	private HandlerRegistration mapViewRegistration;

	public GeometryRenderer(MapWidget mapWidget, GeometryEditingService editingService, MapEventParser eventParser) {
		this.mapWidget = mapWidget;
		this.editingService = editingService;
		editingService.addGeometryEditWorkflowHandler(this);
		editingService.addGeometryEditHighlightHandler(this);
		editingService.addGeometryEditMarkForDeletionHandler(this);
		editingService.addGeometryEditSelectionHandler(this);
		editingService.addGeometryEditOperationHandler(this);
		editingService.addGeometryEditChangeStateHandler(this);
	}

	// ------------------------------------------------------------------------
	// MapViewChangedHandler:
	// ------------------------------------------------------------------------

	public void onMapViewChanged(MapViewChangedEvent event) {
		// Slow, but it works:
		groups.clear();
		mapWidget.getVectorContext().deleteGroup(editingService.getGeometry());
		draw(editingService.getGeometry());
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler:
	// ------------------------------------------------------------------------

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		// Also look at the map for changes in the MapView:
		mapViewRegistration = mapWidget.getMapModel().getMapView().addMapViewChangedHandler(this);

		// Render the geometry on the map:
		groups.clear();
		draw(event.getGeometry());
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		// Remove the handler that follows changes in the map view:
		mapViewRegistration.removeHandler();
		mapViewRegistration = null;

		// Cleanup the geometry from the map:
		groups.clear();
		mapWidget.getVectorContext().deleteGroup(event.getGeometry());
	}

	// ------------------------------------------------------------------------
	// GeometryEditSelectionHandler:
	// ------------------------------------------------------------------------

	public void onGeometryEditSelected(GeometryEditSelectedEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			redraw(event.getGeometry(), index);
		}
	}

	public void onGeometryEditDeselected(GeometryEditDeselectedEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			redraw(event.getGeometry(), index);
		}
	}

	// ------------------------------------------------------------------------
	// GeometryEditHighlightHandler:
	// ------------------------------------------------------------------------

	public void onGeometryEditHighlightBegin(GeometryEditHighlightBeginEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			redraw(event.getGeometry(), index);
		}
	}

	public void onGeometryEditHighlightEnd(GeometryEditHighlightEndEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			redraw(event.getGeometry(), index);
		}
	}

	// ------------------------------------------------------------------------
	// GeometryEditMarkForDeletionHandler:
	// ------------------------------------------------------------------------

	public void onMarkForDeletionBegin(GeometryEditMarkForDeletionBeginEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			redraw(event.getGeometry(), index);
		}
	}

	public void onMarkForDeletionEnd(GeometryEditMarkForDeletionEndEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			redraw(event.getGeometry(), index);
		}
	}

	// ------------------------------------------------------------------------
	// GeometryEditOperationHandler:
	// ------------------------------------------------------------------------

	public void onGeometryEditMove(GeometryEditMoveEvent event) {
		// First find out what exactly must change with the move event (indices + neighbors):
		List<GeometryIndex> toRedraw = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : event.getIndices()) {
			if (!toRedraw.contains(index)) {
				toRedraw.add(index);
				try {
					List<GeometryIndex> neighbors = null;
					switch (index.getType()) {
						case TYPE_VERTEX:
							neighbors = editingService.getIndexService().getAdjacentEdges(event.getGeometry(), index);
							break;
						case TYPE_EDGE:
							neighbors = editingService.getIndexService()
									.getAdjacentVertices(event.getGeometry(), index);
							break;
						default:
							neighbors = new ArrayList<GeometryIndex>();
					}
					if (neighbors != null) {
						toRedraw.addAll(neighbors);
					}
				} catch (GeometryIndexNotFoundException e) {
				}
			}
		}

		// Next, redraw the list:
		for (GeometryIndex index : toRedraw) {
			redraw(event.getGeometry(), index);
		}
	}

	public void onGeometryEditInsert(GeometryEditInsertEvent event) {
		// Slow, but it works:
		groups.clear();
		mapWidget.getVectorContext().deleteGroup(event.getGeometry());
		draw(event.getGeometry());
	}

	public void onGeometryEditDelete(GeometryEditDeleteEvent event) {
		// Slow, but it works:
		groups.clear();
		mapWidget.getVectorContext().deleteGroup(event.getGeometry());
		draw(event.getGeometry());
	}

	// ------------------------------------------------------------------------
	// GeometryEditChangeStateHandler:
	// ------------------------------------------------------------------------

	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		switch (event.getEditingState()) {
			case DRAGGING:
				mapWidget.setCursor(Cursor.MOVE);
				break;
			default:
				mapWidget.setCursor(Cursor.DEFAULT);
		}
	}

	// ------------------------------------------------------------------------
	// Geometry rendering methods:
	// ------------------------------------------------------------------------

	private void redraw(Geometry geometry, GeometryIndex index) {
		try {
			switch (index.getType()) {
				case TYPE_VERTEX:
					redrawVertex(geometry, index);
					break;
				case TYPE_EDGE:
					redrawEdge(geometry, index);
					break;
				default:
					// TODO Redraw sub geometry...
			}
		} catch (GeometryIndexNotFoundException e) {
		}
	}

	private void redrawVertex(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		// Some initialization:
		String identifier = baseName + "." + editingService.getIndexService().format(index);
		boolean selected = editingService.isSelected(index);
		boolean highlighted = editingService.isHightlighted(index);

		// Find current and previous parent groups:
		Composite parentGroup, deleteFromGroup;
		if (selected) {
			parentGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".vertices-selection");
			deleteFromGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".vertices");
		} else {
			deleteFromGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".vertices-selection");
			parentGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".vertices");
		}

		// Find the correct style:
		ShapeStyle style = null;
		if (selected) {
			if (highlighted) {
				style = styleService.getVertexSelectHoverStyle();
			} else {
				style = styleService.getVertexSelectStyle();
			}
		} else {
			if (highlighted) {
				style = styleService.getVertexHoverStyle();
			} else if (editingService.isMarkedForDeletion(index)) {
				style = styleService.getVertexMarkForDeletionStyle();
			} else {
				style = styleService.getVertexStyle();
			}
		}

		// Delete the old vertex:
		mapWidget.getVectorContext().deleteElement(deleteFromGroup, identifier);

		// Draw the new one:
		Coordinate temp = editingService.getIndexService().getVertex(geometry, index);
		Coordinate coordinate = mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToPan(temp);
		Bbox rectangle = new Bbox(coordinate.getX() - 6, coordinate.getY() - 6, 12, 12);
		mapWidget.getVectorContext().drawRectangle(parentGroup, identifier, rectangle, style);
		mapWidget.getVectorContext().setController(parentGroup, identifier, createVertexController(index));
	}

	private void redrawEdge(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		// Some initialization:
		String identifier = baseName + "." + editingService.getIndexService().format(index);
		boolean selected = editingService.isSelected(index);
		boolean highlighted = editingService.isHightlighted(index);

		// Find current and previous parent groups:
		Object parentGroup, deleteFromGroup;
		if (selected) {
			parentGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".edges-selection");
			deleteFromGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".edges");
		} else {
			deleteFromGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".edges-selection");
			parentGroup = groups.get(identifier.substring(0, identifier.lastIndexOf('.')) + ".edges");
		}

		// Find the correct style:
		ShapeStyle style = null;
		if (selected) {
			if (highlighted) {
				style = styleService.getEdgeSelectHoverStyle();
			} else {
				style = styleService.getEdgeSelectStyle();
			}
		} else {
			if (highlighted) {
				style = styleService.getEdgeHoverStyle();
			} else if (editingService.isMarkedForDeletion(index)) {
				style = styleService.getEdgeMarkForDeletionStyle();
			} else {
				style = styleService.getEdgeStyle();
			}
		}

		// Delete the old edge:
		mapWidget.getVectorContext().deleteElement(deleteFromGroup, identifier);

		// Draw the new one:
		Coordinate[] c = editingService.getIndexService().getEdge(geometry, index);
		LineString temp = mapWidget.getMapModel().getGeometryFactory().createLineString(c);
		LineString edge = (LineString) mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToPan(temp);
		mapWidget.getVectorContext().drawLine(parentGroup, identifier, edge, style);
		mapWidget.getVectorContext().setController(parentGroup, identifier, createEdgeController(index));
	}

	private void draw(Geometry geometry) {
		org.geomajas.gwt.client.spatial.geometry.Geometry transformed = mapWidget.getMapModel().getMapView()
				.getWorldViewTransformer().worldToPan(GeometryConverter.toGwt(geometry));

		GraphicsContext graphics = mapWidget.getVectorContext();
		graphics.drawGroup(mapWidget.getGroup(RenderGroup.VECTOR), geometry);

		if (transformed instanceof LineString) {
			draw(geometry, null, (LineString) transformed, graphics);
		} else if (transformed instanceof Point) {
			draw(geometry, null, (Point) transformed, graphics);
		}
	}

	private void draw(Object parentGroup, GeometryIndex parentIndex, LineString lineString, GraphicsContext graphics) {
		String groupName = baseName;
		if (parentIndex != null) {
			groupName += "." + editingService.getIndexService().format(parentIndex);
		}
		getOrCreateGroup(parentGroup, groupName + ".edges-selection");
		Composite edgeGroup = getOrCreateGroup(parentGroup, groupName + ".edges");
		getOrCreateGroup(parentGroup, groupName + ".vertices-selection");
		Composite vertexGroup = getOrCreateGroup(parentGroup, groupName + ".vertices");

		// Draw individual edges:
		Coordinate[] coordinates = lineString.getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			GeometryIndex edgeIndex = editingService.getIndexService().addChildren(parentIndex,
					GeometryIndexType.TYPE_EDGE, i - 1);
			String identifier = baseName + "." + editingService.getIndexService().format(edgeIndex);

			LineString edge = lineString.getGeometryFactory().createLineString(
					new Coordinate[] { coordinates[i - 1], coordinates[i] });
			graphics.drawLine(edgeGroup, identifier, edge, styleService.getEdgeStyle());
			graphics.setController(edgeGroup, identifier, createEdgeController(edgeIndex));
		}

		// Draw individual vertices:
		for (int i = 0; i < coordinates.length; i++) {
			GeometryIndex vertexIndex = editingService.getIndexService().addChildren(parentIndex,
					GeometryIndexType.TYPE_VERTEX, i);
			String identifier = baseName + "." + editingService.getIndexService().format(vertexIndex);

			Bbox rectangle = new Bbox(coordinates[i].getX() - 6, coordinates[i].getY() - 6, 12, 12);
			graphics.drawRectangle(vertexGroup, identifier, rectangle, styleService.getVertexStyle());
			graphics.setController(vertexGroup, identifier, createVertexController(vertexIndex));
		}
	}

	private void draw(Object parentGroup, GeometryIndex parentIndex, Point point, GraphicsContext graphics) {
		String groupName = baseName;
		if (parentIndex != null) {
			groupName += "." + editingService.getIndexService().format(parentIndex);
		}
		getOrCreateGroup(parentGroup, groupName + ".edges-selection");
		getOrCreateGroup(parentGroup, groupName + ".edges");
		getOrCreateGroup(parentGroup, groupName + ".vertices-selection");
		Composite vertexGroup = getOrCreateGroup(parentGroup, groupName + ".vertices");

		if (!point.isEmpty()) {
			GeometryIndex vertexIndex = editingService.getIndexService().addChildren(parentIndex,
					GeometryIndexType.TYPE_VERTEX, 0);
			String identifier = baseName + "." + editingService.getIndexService().format(vertexIndex);

			Bbox rectangle = new Bbox(point.getCoordinate().getX() - 6, point.getCoordinate().getY() - 6, 12, 12);
			graphics.drawRectangle(vertexGroup, identifier, rectangle, styleService.getVertexStyle());
			graphics.setController(vertexGroup, identifier, createVertexController(vertexIndex));
		}
	}

	/**
	 * Used in creating the "edges", "selection" and "vertices" groups for LineStrings and LinearRings.
	 */
	private Composite getOrCreateGroup(Object parent, String name) {
		if (groups.containsKey(name)) {
			return groups.get(name);
		}
		Composite group = new Composite(name);
		mapWidget.getVectorContext().drawGroup(parent, group);
		groups.put(name, group);
		return group;
	}

	private GraphicsController createVertexController(GeometryIndex index) {
		CompositeGeometryIndexController controller = new CompositeGeometryIndexController(mapWidget, editingService,
				index, editingService.getEditingState() == GeometryEditingState.DRAGGING);
		controller.addMapHandler(new GeometryIndexHighlightHandler());
		controller.addMapHandler(new GeometryIndexSelectHandler());
		controller.addMapHandler(new GeometryIndexDragSelectionHandler());
		controller.addMapHandler(new GeometryIndexSnapToDeleteHandler());
		return controller;
	}

	private GraphicsController createEdgeController(GeometryIndex index) {
		CompositeGeometryIndexController controller = new CompositeGeometryIndexController(mapWidget, editingService,
				index, editingService.getEditingState() == GeometryEditingState.DRAGGING);
		controller.addMapHandler(new GeometryIndexHighlightHandler());
		controller.addMapHandler(new GeometryIndexInsertHandler());
		controller.addMapHandler(new GeometryIndexSnapToDeleteHandler());

		// TODO revisit this edge marker:
		EdgeMarkerHandler edgeMarkerHandler = new EdgeMarkerHandler(mapWidget, editingService,
				controller);
		controller.addMouseOutHandler(edgeMarkerHandler);
		controller.addMouseMoveHandler(edgeMarkerHandler);
		controller.addMapDownHandler(edgeMarkerHandler);
		return controller;
	}
}