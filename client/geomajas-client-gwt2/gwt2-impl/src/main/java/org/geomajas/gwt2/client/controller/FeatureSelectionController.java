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

package org.geomajas.gwt2.client.controller;

import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.MathService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.FeatureService.QueryType;
import org.geomajas.gwt2.client.map.feature.FeatureService.SearchLayerType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * <p>
 * Controller for selecting and deselecting features on the map. This controller extends the
 * {@link NavigationController} and will therefore allow some forms of navigation, depending on the chosen
 * {@link SelectionMethod}.
 * </p>
 * <p>
 * Zooming through the mouse wheel or by double clicking as supported through the {@link NavigationController}. Other
 * forms of navigation are dependent upon the {@link SelectionMethod}. The following modes are supported:
 * <ul>
 * <li><b>CLICK_ONLY</b>: Click will select or deselect features. Dragging will pan the map. By pressing the SHIFT
 * button additional features can be selected.</li>
 * <li><b>CLICK_AND_DRAG</b>: This mode will allow the user to also draw a rectangle on the map (by dragging). When the
 * user lets go of the mouse, all features with a ratio of 50% (can be changed) within the rectangle will be selected,
 * all other deselected. If the SHIFT button is pressed, the features within the rectangle will be selected upon the
 * current selection.</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class FeatureSelectionController extends NavigationController {

	/**
	 * Enumeration that decides how selection should behave.
	 * 
	 * @author Pieter De Graef
	 */
	public enum SelectionMethod {
		/**
		 * The user can select features only by clicking on the map. Selection by dragging a rectangle is not supported.
		 * Instead when dragging, the map will pan.
		 */
		CLICK_ONLY,

		/**
		 * Support both clicking an selection by dragging a rectangle. Dragging will no longer pan the map.
		 */
		CLICK_AND_DRAG
	}

	private final SelectionRectangleController selectionRectangleController;

	// Selection options:

	private SelectionMethod selectionMethod = SelectionMethod.CLICK_ONLY;

	private SearchLayerType searchLayerType = SearchLayerType.TOP_LAYER_ONLY;

	private float intersectionRatio = 0.5f;

	private int pixelTolerance = 5;

	// Keeping track of panning versus clicking:

	private double clickDelta = 2;

	private Coordinate onDownLocation;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	/**
	 * Create a controller.
	 */
	public FeatureSelectionController() {
		super();
		selectionRectangleController = new SelectionRectangleController();
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		// Activate all 3 controllers:
		super.onActivate(mapPresenter);
		this.mapPresenter = mapPresenter;
		selectionRectangleController.onActivate(mapPresenter);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		switch (selectionMethod) {
			case CLICK_AND_DRAG:
				onDownLocation = getLocation(event, RenderSpace.SCREEN);
				selectionRectangleController.onMouseDown(event);
				break;
			default:
				super.onMouseDown(event);
		}
	}

	@Override
	public void onDown(HumanInputEvent<?> event) {
		switch (selectionMethod) {
			case CLICK_AND_DRAG:
				selectionRectangleController.onDown(event);
				break;
			default:
				onDownLocation = getLocation(event, RenderSpace.SCREEN);
				if (!event.isShiftKeyDown() && !event.isControlKeyDown()) {
					super.onDown(event);
				}
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		switch (selectionMethod) {
			case CLICK_AND_DRAG:
				selectionRectangleController.onMouseMove(event);
				break;
			default:
				if (!isDownPosition(event)) {
					super.onMouseMove(event);
				}
		}
	}

	@Override
	public void onDrag(HumanInputEvent<?> event) {
		switch (selectionMethod) {
			case CLICK_AND_DRAG:
				selectionRectangleController.onDrag(event);
				break;
			default:
				super.onDrag(event);
		}
	}

	@Override
	public void onUp(HumanInputEvent<?> event) {
		switch (selectionMethod) {
			case CLICK_AND_DRAG:
				if (isDownPosition(event)) {
					searchAtLocation(getLocation(event, RenderSpace.WORLD), event.isShiftKeyDown());
					selectionRectangleController.cleanup();
				} else {
					selectionRectangleController.onUp(event);
				}
				break;
			default:
				stopPanning(null);
				if (!event.isShiftKeyDown() && !event.isControlKeyDown()) {
					super.onUp(event);
				}
				if (isDownPosition(event)) {
					searchAtLocation(getLocation(event, RenderSpace.WORLD), event.isShiftKeyDown());
				}
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		super.onMouseOut(event);
		if (selectionMethod == SelectionMethod.CLICK_AND_DRAG) {
			selectionRectangleController.onMouseOut(event);
		}
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the current selection method. This determines how the user can select features on the map.
	 * 
	 * @return The current selection method.
	 */
	public SelectionMethod getSelectionMethod() {
		return selectionMethod;
	}

	/**
	 * Change the way selection should occur.
	 * 
	 * @param selectionMethod
	 *            The new selection method to apply.
	 */
	public void setSelectionMethod(SelectionMethod selectionMethod) {
		this.selectionMethod = selectionMethod;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Is the event at the same location as the "down" event?
	 * 
	 * @param event
	 *            The event to check.
	 * @return true or false.
	 */
	private boolean isDownPosition(HumanInputEvent<?> event) {
		if (onDownLocation != null) {
			Coordinate location = getLocation(event, RenderSpace.SCREEN);
			if (MathService.distance(onDownLocation, location) < clickDelta) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Search for features at a certain location.
	 * 
	 * @param location
	 *            The location to check.
	 * @param isShift
	 *            Is the shift button pressed down?
	 */
	private void searchAtLocation(Coordinate location, boolean isShift) {
		Geometry point = new Geometry(Geometry.POINT, 0, -1);
		point.setCoordinates(new Coordinate[] { location });

		mapPresenter.getFeatureService().search(point, pixelsToUnits(pixelTolerance), QueryType.INTERSECTS,
				searchLayerType, -1, new SelectionCallback(isShift, false));
	}

	/**
	 * Transform a pixel-length into a real-life distance expressed in map CRS. This depends on the current map scale.
	 * 
	 * @param pixels
	 *            The number of pixels to calculate the distance for.
	 * @return The distance the given number of pixels entails.
	 */
	private double pixelsToUnits(int pixels) {
		Coordinate c1 = mapPresenter.getViewPort().transform(new Coordinate(0, 0), RenderSpace.SCREEN,
				RenderSpace.WORLD);
		Coordinate c2 = mapPresenter.getViewPort().transform(new Coordinate(pixels, 0), RenderSpace.SCREEN,
				RenderSpace.WORLD);
		return MathService.distance(c1, c2);
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Internal selection by rectangle controller.
	 * 
	 * @author Pieter De Graef
	 */
	private class SelectionRectangleController extends AbstractRectangleController {

		public void execute(Bbox worldBounds) {
			mapPresenter.getFeatureService().search(GeometryService.toPolygon(worldBounds), 0, QueryType.INTERSECTS,
					searchLayerType, intersectionRatio, new SelectionCallback(shift, true));
		}

		public void cleanup() {
			if (dragging) {
				dragging = false;
				if (container != null) {
					container.remove(rectangle);
				}
			}
		}
	}

	/**
	 * Callback for feature searches that actually selects or deselects the features involved.
	 * 
	 * @author Pieter De Graef
	 */
	private class SelectionCallback implements FeatureMapFunction {

		private final boolean isShift;

		private final boolean bulk;

		public SelectionCallback(boolean isShift, boolean bulk) {
			this.isShift = isShift;
			this.bulk = bulk;
		}

		public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {
			if (bulk) {
				for (FeaturesSupported layer : featureMap.keySet()) {
					List<Feature> features = featureMap.get(layer);
					if (features != null) {
						if (!isShift) {
							layer.clearSelectedFeatures();
						}
						for (Feature feature : features) {
							if (!layer.isFeatureSelected(feature.getId())) {
								layer.selectFeature(feature);
							}
						}
					}
				}
			} else {
				for (FeaturesSupported layer : featureMap.keySet()) {
					List<Feature> features = featureMap.get(layer);
					if (features != null) {
						if (isShift) {
							// Add to selection, unless already selected:
							if (layer.isFeatureSelected(features.get(0).getId())) {
								layer.deselectFeature(features.get(0));
							} else {
								layer.selectFeature(features.get(0));
							}
						} else {
							// No shift: if selected deselect, otherwise make sure it's the only selection:
							if (layer.isFeatureSelected(features.get(0).getId())) {
								layer.clearSelectedFeatures();
							} else {
								layer.clearSelectedFeatures();
								layer.selectFeature(features.get(0));
							}
						}
					}
				}
			}
		}
	}
}