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

package org.geomajas.plugin.editing.puregwt.client.gfx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.puregwt.client.GeomajasGinjector;
import org.geomajas.puregwt.client.controller.MapController;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;

import com.google.gwt.core.client.GWT;

/**
 * Renderer for geometries during the editing process.
 * 
 * @author Pieter De Graef
 */
public class GeometryRenderer implements GeometryEditStartHandler, GeometryEditStopHandler,
		GeometryIndexHighlightBeginHandler, GeometryIndexHighlightEndHandler, GeometryEditMoveHandler,
		GeometryEditShapeChangedHandler, GeometryEditChangeStateHandler, GeometryIndexSelectedHandler,
		GeometryIndexDeselectedHandler, GeometryIndexDisabledHandler, GeometryIndexEnabledHandler,
		GeometryIndexMarkForDeletionBeginHandler, GeometryIndexMarkForDeletionEndHandler,
		GeometryEditTentativeMoveHandler, ViewPortChangedHandler {

	private static final GeomajasGinjector INJECTOR = GWT.create(GeomajasGinjector.class);

	private final MapPresenter mapPresenter;

	private final GeometryEditService editService;

	private final StyleProvider styleProvider;

	private final Map<GeometryIndex, Shape> shapes;

	private GeometryIndexShapeFactory shapeFactory;

	private GeometryIndexStyleFactory styleFactory;

	private GeometryIndexControllerFactory controllerFactory;

	private VectorContainer container;

	private Path tentativeMoveLine;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	/**
	 * Create a new renderer instance that renders on the given map and listens to the given editing service.
	 * 
	 * @param mapPresenter
	 *            The map to render on.
	 * @param editService
	 *            The geometry editing service to listen to.
	 */
	public GeometryRenderer(MapPresenter mapPresenter, GeometryEditService editService) {
		this.mapPresenter = mapPresenter;
		this.editService = editService;
		this.styleProvider = new StyleProvider();
		this.shapes = new HashMap<GeometryIndex, Shape>();

		// Initialize default factories:
		this.shapeFactory = new DefaultGeometryIndexShapeFactory(mapPresenter, RenderSpace.SCREEN);
		this.styleFactory = new DefaultGeometryIndexStyleFactory(styleProvider);
		this.controllerFactory = new DefaultGeometryIndexControllerFactory(mapPresenter);

		// Add ViewPortChangedHandler:
		mapPresenter.getEventBus().addHandler(ViewPortChangedHandler.TYPE, this);

		// Add edit base handlers:
		editService.addGeometryEditChangeStateHandler(this);
		editService.addGeometryEditMoveHandler(this);
		editService.addGeometryEditShapeChangedHandler(this);
		editService.addGeometryEditStartHandler(this);
		editService.addGeometryEditStopHandler(this);
		editService.addGeometryEditTentativeMoveHandler(this);

		// Add GeometryIndex state handlers:
		editService.getIndexStateService().addGeometryIndexDeselectedHandler(this);
		editService.getIndexStateService().addGeometryIndexSelectedHandler(this);
		editService.getIndexStateService().addGeometryIndexDisabledHandler(this);
		editService.getIndexStateService().addGeometryIndexEnabledHandler(this);
		editService.getIndexStateService().addGeometryIndexHighlightBeginHandler(this);
		editService.getIndexStateService().addGeometryIndexHighlightEndHandler(this);
		editService.getIndexStateService().addGeometryIndexMarkForDeletionBeginHandler(this);
		editService.getIndexStateService().addGeometryIndexMarkForDeletionEndHandler(this);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/** Clear everything and completely redraw the edited geometry. */
	public void redraw() {
		shapes.clear();
		if (container != null) {
			container.clear();
			try {
				draw();
			} catch (GeometryIndexNotFoundException e) {
				// Happens when creating new geometries...can't render points that don't exist yet.
			}
		}
	}

	// ------------------------------------------------------------------------
	// ViewPortChangedHandler implementation:
	// ------------------------------------------------------------------------

	/** Redraw the geometry on the map. */
	public void onViewPortChanged(ViewPortChangedEvent event) {
		redraw();
	}

	/** Redraw the geometry on the map. */
	public void onViewPortScaled(ViewPortScaledEvent event) {
		redraw();
	}

	/** Redraw the geometry on the map. */
	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		redraw();
	}

	// ------------------------------------------------------------------------
	// Start & stop handler implementations:
	// ------------------------------------------------------------------------

	/** Clean up the previous state, create a container to draw in and then draw the geometry. */
	public void onGeometryEditStart(GeometryEditStartEvent event) {
		if (container != null) {
			mapPresenter.removeVectorContainer(container);
		}
		container = mapPresenter.addScreenContainer();
		redraw();
	}

	/** Clean up all rendering. */
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		// Remove the vector container from the map:
		mapPresenter.removeVectorContainer(container);
		container = null;
		shapes.clear();
	}

	// ------------------------------------------------------------------------
	// GeometryIndex state change implementations:
	// ------------------------------------------------------------------------

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexMarkForDeletionEnd(GeometryIndexMarkForDeletionEndEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexMarkForDeletionBegin(GeometryIndexMarkForDeletionBeginEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexEnabled(GeometryIndexEnabledEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexDisabled(GeometryIndexDisabledEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexDeselected(GeometryIndexDeselectedEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexSelected(GeometryIndexSelectedEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexHighlightEnd(GeometryIndexHighlightEndEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	/** Update the styles of the geometry indices in the event. */
	public void onGeometryIndexHighlightBegin(GeometryIndexHighlightBeginEvent event) {
		for (GeometryIndex index : event.getIndices()) {
			update(index, false);
		}
	}

	// ------------------------------------------------------------------------
	// GeometryEditChangeStateHandler implementation:
	// ------------------------------------------------------------------------

	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		// Later dude...
	}

	// ------------------------------------------------------------------------
	// Geometry shape change implementation:
	// ------------------------------------------------------------------------

	/** Redraw the geometry on the map. */
	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		// Check if there is a tentative move line (left over) and remove if so.
		if (tentativeMoveLine != null) {
			container.remove(tentativeMoveLine);
			tentativeMoveLine = null;
		}
		redraw();
	}

	/**
	 * Figure out what's being moved and update only adjacent objects. This is far more performing than simply redrawing
	 * everything.
	 */
	public void onGeometryEditMove(GeometryEditMoveEvent event) {
		// Check if we need to draw the background (nice, but slows down):
		if (styleProvider.getBackgroundStyle() != null && styleProvider.getBackgroundStyle().getFillOpacity() > 0) {
			try {
				if (event.getGeometry().getGeometryType().equals(Geometry.POLYGON)) {
					GeometryIndex index = editService.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY, 0);
					drawIndex(index);
				} else if (event.getGeometry().getGeometryType().equals(Geometry.MULTI_POLYGON)
						&& event.getGeometry().getGeometries() != null) {
					for (int i = 0; i < event.getGeometry().getGeometries().length; i++) {
						GeometryIndex index = editService.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY, i);
						drawIndex(index);
					}
				}
			} catch (GeometryIndexNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}

		// Find the elements that need updating:
		Map<GeometryIndex, Boolean> indicesToUpdate = new HashMap<GeometryIndex, Boolean>();
		for (GeometryIndex index : event.getIndices()) {
			if (!indicesToUpdate.containsKey(index)) {
				indicesToUpdate.put(index, false);
				try {
					List<GeometryIndex> neighbors = null;
					switch (editService.getIndexService().getType(index)) {
						case TYPE_VERTEX:
							neighbors = editService.getIndexService().getAdjacentEdges(event.getGeometry(), index);
							if (neighbors != null) {
								for (GeometryIndex neighborIndex : neighbors) {
									if (!indicesToUpdate.containsKey(neighborIndex)) {
										indicesToUpdate.put(neighborIndex, false);
									}
								}
							}
							
							// Bring neighboring vertices to the front. This helps the delete operation.
							neighbors = editService.getIndexService().getAdjacentVertices(event.getGeometry(), index);
							if (neighbors != null) {
								for (GeometryIndex neighborIndex : neighbors) {
									if (!indicesToUpdate.containsKey(neighborIndex)) {
										indicesToUpdate.put(neighborIndex, true);
									}
								}
							}
							break;
						case TYPE_EDGE:
							neighbors = editService.getIndexService().getAdjacentVertices(event.getGeometry(), index);
							if (neighbors != null) {
								for (GeometryIndex neighborIndex : neighbors) {
									if (!indicesToUpdate.containsKey(neighborIndex)) {
										indicesToUpdate.put(neighborIndex, false);
									}
								}
							}
							break;
						default:
					}
				} catch (GeometryIndexNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
		}

		// Next, redraw the list:
		for (GeometryIndex index : indicesToUpdate.keySet()) {
			update(index, indicesToUpdate.get(index));
		}
	}

	/**
	 * Renders a line from the last inserted point to the current mouse position, indicating what the new situation
	 * would look like if a vertex where to be inserted at the mouse location.
	 */
	public void onTentativeMove(GeometryEditTentativeMoveEvent event) {
		try {
			Coordinate[] vertices = editService.getIndexService().getSiblingVertices(editService.getGeometry(),
					editService.getInsertIndex());
			String geometryType = editService.getIndexService().getGeometryType(editService.getGeometry(),
					editService.getInsertIndex());

			if (vertices != null && Geometry.LINE_STRING.equals(geometryType)) {
				Coordinate temp1 = event.getOrigin();
				Coordinate temp2 = event.getCurrentPosition();
				Coordinate c1 = mapPresenter.getViewPort().transform(temp1, RenderSpace.WORLD, RenderSpace.SCREEN);
				Coordinate c2 = mapPresenter.getViewPort().transform(temp2, RenderSpace.WORLD, RenderSpace.SCREEN);

				if (tentativeMoveLine == null) {
					tentativeMoveLine = new Path(c1.getX(), c1.getY());
					tentativeMoveLine.lineTo(c2.getX(), c2.getY());
					INJECTOR.getGfxUtil().applyStyle(tentativeMoveLine, styleProvider.getEdgeTentativeMoveStyle());
					container.add(tentativeMoveLine);
				} else {
					tentativeMoveLine.setStep(1, new LineTo(false, c2.getX(), c2.getY()));
				}
			} else if (vertices != null && Geometry.LINEAR_RING.equals(geometryType)) {
				// Later dude...
			}
		} catch (GeometryIndexNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the style provider. This object is used by the default style factory for providing styles for the geometry
	 * rendering. As long as the default style factory is being used, this object can be used to change the styles.
	 */
	public StyleProvider getStyleProvider() {
		return styleProvider;
	}

	// ------------------------------------------------------------------------
	// Private methods for updating:
	// ------------------------------------------------------------------------

	private void update(GeometryIndex index, boolean bringToFront) {
		try {
			switch (editService.getIndexService().getType(index)) {
				case TYPE_VERTEX:
				case TYPE_EDGE:
					Shape shape = shapes.get(index);
					if (shape != null) {
						// We don't consider position at this point. Just style:
						FeatureStyleInfo style = styleFactory.create(editService, index);
						INJECTOR.getGfxUtil().applyStyle(shape, style);

						// Now update the location:
						shapeFactory.update(shape, editService, index);
						
						// Bring to the front if requested:
						if (bringToFront) {
							container.bringToFront(shape);
						}
					}
					break;
				case TYPE_GEOMETRY:
				default:
					// updateGeometry(geometry, index);
			}
		} catch (GeometryIndexNotFoundException e) {
		}
	}

	// ------------------------------------------------------------------------
	// Private methods for re-drawing:
	// ------------------------------------------------------------------------

	private void draw() throws GeometryIndexNotFoundException {
		if (Geometry.POINT.equals(editService.getGeometry().getGeometryType())) {
			drawPoint(null);
		} else if (Geometry.LINE_STRING.equals(editService.getGeometry().getGeometryType())
				|| Geometry.LINEAR_RING.equals(editService.getGeometry().getGeometryType())) {
			drawLineString(null);
		}
	}

	private void drawLineString(GeometryIndex parentIndex) throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		if (geometry.getCoordinates() != null) {
			// Draw all edges:
			for (int i = 0; i < geometry.getCoordinates().length - 1; i++) { // Line is not closed!
				GeometryIndex index = editService.getIndexService().addChildren(parentIndex,
						GeometryIndexType.TYPE_EDGE, i);
				drawIndex(index);
			}

			// Then draw all vertices:
			for (int i = 0; i < geometry.getCoordinates().length; i++) {
				GeometryIndex index = editService.getIndexService().addChildren(parentIndex,
						GeometryIndexType.TYPE_VERTEX, i);
				drawIndex(index);
			}
		}
	}

	private void drawPoint(GeometryIndex parentIndex) throws GeometryIndexNotFoundException {
		GeometryIndex index = editService.getIndexService().addChildren(parentIndex, GeometryIndexType.TYPE_VERTEX, 0);
		drawIndex(index);
	}

	private void drawIndex(GeometryIndex index) throws GeometryIndexNotFoundException {
		Shape shape = shapeFactory.create(editService, index);
		FeatureStyleInfo style = styleFactory.create(editService, index);
		MapController controller = controllerFactory.create(editService, index);
		controller.onActivate(mapPresenter);

		INJECTOR.getGfxUtil().applyStyle(shape, style);
		INJECTOR.getGfxUtil().applyController(shape, controller);

		container.add(shape);
		shapes.put(index, shape);
	}
}