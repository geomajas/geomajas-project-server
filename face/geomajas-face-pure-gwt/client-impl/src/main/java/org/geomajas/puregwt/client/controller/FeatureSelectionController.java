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

package org.geomajas.puregwt.client.controller;

import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.MathService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.feature.FeatureMapFunction;
import org.geomajas.puregwt.client.map.feature.FeatureService.QueryType;
import org.geomajas.puregwt.client.map.feature.FeatureService.SearchLayerType;
import org.geomajas.puregwt.client.map.layer.FeaturesSupported;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * Controller for selecting and deselecting features on the map.
 * 
 * @author Pieter De Graef
 */
public class FeatureSelectionController extends NavigationController {

	/**
	 * Enumeration that decides how selection should behave.
	 * 
	 * @author Pieter De Graef
	 */
	public enum SelectionMethod {
		CLICK_ONLY, CLICK_AND_DRAG
	}

	private final SelectionRectangleController selectionRectangleController;

	private SelectionMethod selectionMethod = SelectionMethod.CLICK_ONLY;

	private SearchLayerType searchLayerType = SearchLayerType.TOP_LAYER_ONLY;

	private float intersectionRatio = 0.5f;

	private int pixelTolerance = 5;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public FeatureSelectionController() {
		super();
		selectionRectangleController = new SelectionRectangleController();
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	public void onActivate(MapPresenter mapPresenter) {
		// Activate all 3 controllers:
		super.onActivate(mapPresenter);
		this.mapPresenter = mapPresenter;
		selectionRectangleController.onActivate(mapPresenter);
	}

	public void onDown(HumanInputEvent<?> event) {
		if (selectionMethod == SelectionMethod.CLICK_AND_DRAG) {
			selectionRectangleController.onDown(event);
		} else {
			super.onDown(event);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		stopPanning(null);

		switch (selectionMethod) {
			case CLICK_AND_DRAG:
				selectionRectangleController.onUp(event);
				break;
			default:
				super.onUp(event);
				searchAtLocation(getLocation(event, RenderSpace.WORLD), true);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (dragging && selectionMethod == SelectionMethod.CLICK_AND_DRAG) {
			selectionRectangleController.onMouseMove(event);
		} else {
			super.onMouseMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		super.onMouseOut(event);
		if (selectionMethod == SelectionMethod.CLICK_AND_DRAG) {
			selectionRectangleController.onMouseOut(event);
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void searchAtLocation(Coordinate location, boolean select) {
		Geometry point = new Geometry(Geometry.POINT, 0, -1);
		point.setCoordinates(new Coordinate[] { location });

		mapPresenter.getFeatureService().search(point, pixelsToUnits(pixelTolerance), QueryType.INTERSECTS,
				searchLayerType, intersectionRatio, new SelectionCallback(select));
	}

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

		protected void execute(Bbox worldBounds) {
			mapPresenter.getFeatureService().search(GeometryService.toPolygon(worldBounds), 0, QueryType.INTERSECTS,
					searchLayerType, intersectionRatio, new SelectionCallback(!shift));
		}
	}

	/**
	 * Callback for feature searches that actually selects or deselects the features involved.
	 * 
	 * @author Pieter De Graef
	 */
	private class SelectionCallback implements FeatureMapFunction {

		private boolean isShift;

		public SelectionCallback(boolean isShift) {
			this.isShift = isShift;
		}

		public void execute(Map<Layer<?>, List<Feature>> featureMap) {
			// TODO Auto-generated method stub

		}

		public void execute(List<Feature> features) {
			if (features != null && features.size() > 0) {
				for (Feature feature : features) {
					FeaturesSupported fs = (FeaturesSupported) feature.getLayer();
					fs.clearSelectedFeatures();
					if (isShift) {
						fs.selectFeature(feature);
					} else {
						fs.selectFeature(feature);
					}
				}
			}
		}
	}
}