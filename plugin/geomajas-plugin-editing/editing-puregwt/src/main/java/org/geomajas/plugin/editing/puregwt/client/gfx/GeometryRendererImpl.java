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

package org.geomajas.plugin.editing.puregwt.client.gfx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
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
import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.puregwt.client.controller.MapController;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortChangingEvent;
import org.geomajas.puregwt.client.event.ViewPortChangingHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortScalingEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatingEvent;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

/**
 * Renderer for geometries during the editing process.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class GeometryRendererImpl implements GeometryRenderer, GeometryEditStartHandler, GeometryEditStopHandler,
		GeometryIndexHighlightBeginHandler, GeometryIndexHighlightEndHandler, GeometryEditMoveHandler,
		GeometryEditShapeChangedHandler, GeometryEditChangeStateHandler, GeometryIndexSelectedHandler,
		GeometryIndexDeselectedHandler, GeometryIndexDisabledHandler, GeometryIndexEnabledHandler,
		GeometryIndexMarkForDeletionBeginHandler, GeometryIndexMarkForDeletionEndHandler,
		GeometryEditTentativeMoveHandler, ViewPortChangedHandler, ViewPortChangingHandler {

	private final MapPresenter mapPresenter;

	private final GeometryEditService editService;
	
	private GfxUtil gfxUtil;

	private final StyleProvider styleProvider;

	private final Map<GeometryIndex, VectorObject> shapes;

	private GeometryIndexShapeFactory shapeFactory;

	private GeometryIndexStyleFactory styleFactory;

	private GeometryIndexControllerFactory controllerFactory;

	private VectorContainer container;

	private Path tentativeMoveLine;

	private VectorObject nullShape;
	
	private double previousDx;
	
	private double previousDy;
	

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
	public GeometryRendererImpl(MapPresenter mapPresenter, GeometryEditService editService, GfxUtil gfxUtil) {
		this.mapPresenter = mapPresenter;
		this.gfxUtil = gfxUtil;
		this.editService = editService;
		this.styleProvider = new StyleProvider();
		this.shapes = new HashMap<GeometryIndex, VectorObject>();

		// Initialize default factories:
		this.shapeFactory = new DefaultGeometryIndexShapeFactory(mapPresenter, RenderSpace.SCREEN, gfxUtil);
		this.styleFactory = new DefaultGeometryIndexStyleFactory(styleProvider);
		this.controllerFactory = new DefaultGeometryIndexControllerFactory(mapPresenter, gfxUtil);

		// Add ViewPortChangedHandler:
		mapPresenter.getEventBus().addHandler(ViewPortChangedHandler.TYPE, this);
		mapPresenter.getEventBus().addHandler(ViewPortChangingHandler.TYPE, this);

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
		previousDx = mapPresenter.getViewPort().getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN).getDx();
		previousDy = mapPresenter.getViewPort().getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN).getDy();
		shapes.clear();
		if (container != null) {
			container.setTranslation(0, 0);
			container.clear();
			try {
				tentativeMoveLine = new Path(-5, -5);
				tentativeMoveLine.lineTo(-5, -5);
				FeatureStyleInfo style =  styleProvider.getEdgeTentativeMoveStyle();
				gfxUtil.applyStroke(tentativeMoveLine, style.getStrokeColor(), style.getStrokeOpacity(),
						style.getStrokeWidth(), style.getDashArray());
				container.add(tentativeMoveLine);

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

	@Override
	public void onViewPortChanging(ViewPortChangingEvent event) {
		// intermediate changes by translating/scaling screen container	
		
	}

	@Override
	public void onViewPortScaling(ViewPortScalingEvent event) {
		// intermediate changes by translating/scaling screen container			
	}

	@Override
	public void onViewPortTranslating(ViewPortTranslatingEvent event) {
		// reflect intermediate changes by translating screen container			
		if (container != null) {
			Matrix translation = event.getViewPort().getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			container.setTranslation(translation.getDx() - previousDx, translation.getDy() - previousDy);
		}
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

	/** Change the cursor while dragging. */
	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		switch (event.getEditingState()) {
			case DRAGGING:
				mapPresenter.setCursor("move");
				break;
			case IDLE:
			default:
				mapPresenter.setCursor("default");
				redraw();
		}
	}

	// ------------------------------------------------------------------------
	// Geometry shape change implementation:
	// ------------------------------------------------------------------------

	/** Redraw the geometry on the map. */
	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		redraw();
	}

	/**
	 * Figure out what's being moved and update only adjacent objects. This is far more performing than simply redrawing
	 * everything.
	 */
	public void onGeometryEditMove(GeometryEditMoveEvent event) {
		// Find the elements that need updating:
		Map<GeometryIndex, Boolean> indicesToUpdate = new HashMap<GeometryIndex, Boolean>();
		for (GeometryIndex index : event.getIndices()) {
			if (!indicesToUpdate.containsKey(index)) {
				indicesToUpdate.put(index, false);
				if (!Geometry.POINT.equals(editService.getGeometry().getGeometryType())
						&& !Geometry.MULTI_POINT.equals(editService.getGeometry().getGeometryType())) {
					try {
						List<GeometryIndex> neighbors = null;
						switch (editService.getIndexService().getType(index)) {
							case TYPE_VERTEX:
								// Move current vertex to the back. This helps the delete operation.
								indicesToUpdate.put(index, true);
								neighbors = editService.getIndexService().getAdjacentEdges(event.getGeometry(), index);
								if (neighbors != null) {
									for (GeometryIndex neighborIndex : neighbors) {
										if (!indicesToUpdate.containsKey(neighborIndex)) {
											indicesToUpdate.put(neighborIndex, false);
										}
									}
								}

								neighbors = editService.getIndexService().getAdjacentVertices(event.getGeometry(),
										index);
								if (neighbors != null) {
									for (GeometryIndex neighborIndex : neighbors) {
										if (!indicesToUpdate.containsKey(neighborIndex)) {
											indicesToUpdate.put(neighborIndex, false);
										}
									}
								}
								break;
							case TYPE_EDGE:
								neighbors = editService.getIndexService().getAdjacentVertices(event.getGeometry(),
										index);
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
		}

		// Check if we need to draw the background (nice, but slows down):
		if (styleProvider.getBackgroundStyle() != null && styleProvider.getBackgroundStyle().getFillOpacity() > 0) {
			if (event.getGeometry().getGeometryType().equals(Geometry.POLYGON)) {
				update(null, false);
			} else if (event.getGeometry().getGeometryType().equals(Geometry.MULTI_POLYGON)
					&& event.getGeometry().getGeometries() != null) {
				for (int i = 0; i < event.getGeometry().getGeometries().length; i++) {
					GeometryIndex index = editService.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY, i);
					indicesToUpdate.put(index, false);
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

			if (vertices != null
					&& (Geometry.LINE_STRING.equals(geometryType) || Geometry.LINEAR_RING.equals(geometryType))) {
				Coordinate temp1 = event.getOrigin();
				Coordinate temp2 = event.getCurrentPosition();
				Coordinate c1 = mapPresenter.getViewPort().transform(temp1, RenderSpace.WORLD, RenderSpace.SCREEN);
				Coordinate c2 = mapPresenter.getViewPort().transform(temp2, RenderSpace.WORLD, RenderSpace.SCREEN);

				tentativeMoveLine.setStep(0, new MoveTo(false, c1.getX(), c1.getY()));
				tentativeMoveLine.setStep(1, new LineTo(false, c2.getX(), c2.getY()));
			} else if (vertices != null && Geometry.LINEAR_RING.equals(geometryType)) {
				// Draw the second line (as an option...)
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
	
	public void setVisible(boolean visible) {
		if (container != null) {
			container.setVisible(visible);
		}
	}

	// ------------------------------------------------------------------------
	// Private methods for updating:
	// ------------------------------------------------------------------------

	private void update(GeometryIndex index, boolean moveToBack) {
		try {
			VectorObject shape = null;
			if (index == null) {
				shape = nullShape;
			} else {
				shape = shapes.get(index);
			}
			if (shape != null) {
				// We don't consider position at this point. Just style:
				FeatureStyleInfo style = styleFactory.create(editService, index);
				gfxUtil.applyStroke(shape, style.getStrokeColor(), style.getStrokeOpacity(), style.getStrokeWidth(),
						style.getDashArray());
				gfxUtil.applyFill(shape, style.getFillColor(), style.getFillOpacity());

				// Now update the location:
				shapeFactory.update(shape, editService, index);

				// Move to the front if requested:
				if (moveToBack) {
					container.moveToBack(shape);
				}
			}
		} catch (GeometryIndexNotFoundException e) {
			throw new IllegalStateException(e);
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
		} else if (Geometry.POLYGON.equals(editService.getGeometry().getGeometryType())) {
			drawPolygon(null);
		}
	}

	private void drawPolygon(GeometryIndex parentIndex) throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		if (parentIndex != null) {
			geometry = editService.getIndexService().getGeometry(editService.getGeometry(), parentIndex);
		}
		if (geometry.getGeometries() != null) {
			// First of all we check if the background needs to be drawn:
			if (styleProvider.getBackgroundStyle() != null && styleProvider.getBackgroundStyle().getFillOpacity() > 0) {
				drawIndex(parentIndex);
			}

			// Than, we draw all LinearRings one by one:
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				GeometryIndex index = editService.getIndexService().addChildren(parentIndex,
						GeometryIndexType.TYPE_GEOMETRY, i);
				drawLinearRing(index);
			}
		}
	}

	private void drawLinearRing(GeometryIndex parentIndex) throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		if (parentIndex != null) {
			geometry = editService.getIndexService().getGeometry(editService.getGeometry(), parentIndex);
		}
		int[] indices = null;
		if (geometry.getCoordinates() != null) {
			int max = geometry.getCoordinates().length - 1;
			if (editService.getEditingState().equals(GeometryEditState.INSERTING)) {
				max--;
			}
			// limit to maximum 50 visible indices if max > 50
			if (max > 50) {
				max = 50;
				indices = new int[50];
				Bbox bounds = mapPresenter.getViewPort().getBounds();
				int j = 0;
				for (int i = 0; i < geometry.getCoordinates().length; i++) {
					if (contains(bounds, geometry.getCoordinates()[i])) {
						indices[j++] = i;
						if (j == 50) {
							break;
						}
					}
				}
				// if less than 50 visible, only show these
				if (j < 50) {
					max = j;
				}
			}
			// Draw all edges:
			for (int i = 0; i < max; i++) {
				int ii = (indices == null ? i : indices[i]);
				GeometryIndex index = editService.getIndexService().addChildren(parentIndex,
						GeometryIndexType.TYPE_EDGE, ii);
				drawIndex(index);
			}

			// Then draw all vertices:
			for (int i = 0; i < max; i++) {
				int ii = (indices == null ? i : indices[i]);
				GeometryIndex index = editService.getIndexService().addChildren(parentIndex,
						GeometryIndexType.TYPE_VERTEX, ii);
				drawIndex(index);
			}
		}
	}

	private void drawLineString(GeometryIndex parentIndex) throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		if (parentIndex != null) {
			geometry = editService.getIndexService().getGeometry(editService.getGeometry(), parentIndex);
		}
		if (geometry.getCoordinates() != null) {
			// Draw all edges:
			for (int i = 0; i < geometry.getCoordinates().length - 1; i++) {
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
		VectorObject shape = shapeFactory.create(editService, index);
		if (shape == null) {
			return;
		}

		// Apply style:
		FeatureStyleInfo style = styleFactory.create(editService, index);
		gfxUtil.applyStroke(shape, style.getStrokeColor(), style.getStrokeOpacity(), style.getStrokeWidth(),
				style.getDashArray());
		gfxUtil.applyFill(shape, style.getFillColor(), style.getFillOpacity());

		// Apply controller:
		MapController controller = controllerFactory.create(editService, index);
		if (controller != null) {
			controller.onActivate(mapPresenter);
			gfxUtil.applyController(shape, controller);
		}

		container.add(shape);
		if (index == null) {
			nullShape = shape;
		} else {
			shapes.put(index, shape);
		}
	}
	
	boolean contains(Bbox box, Coordinate c) {
		if (c.getX() < box.getX()) {
			return false;
		}
		if (c.getY() < box.getY()) {
			return false;
		}
		if (c.getX() > box.getMaxX()) {
			return false;
		}
		if (c.getY() > box.getMaxY()) {
			return false;
		}
		return true;
	}
}