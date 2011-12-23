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

package org.geomajas.puregwt.client.map.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.feature.FeatureCallback;
import org.geomajas.puregwt.client.map.feature.FeatureSearch;
import org.geomajas.puregwt.client.map.feature.FeatureSearch.QueryType;
import org.geomajas.puregwt.client.map.feature.FeatureSearch.SearchType;
import org.geomajas.puregwt.client.map.layer.FeaturesSupported;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

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

	/**
	 * When trying to select features, which layers should be searched?
	 * 
	 * @author Pieter De Graef
	 */
	public enum SelectionType {
		SELECTED_LAYER, FIRST_LAYER, ALL_LAYERS
	}

	private final FeatureSearch featureSearch;

	private final SelectionRectangleController selectionRectangleController;

	private SelectionMethod selectionMethod = SelectionMethod.CLICK_AND_DRAG;

	private SelectionType selectionType = SelectionType.FIRST_LAYER;

	private float intersectionRatio = 0.5f;

	public FeatureSelectionController() {
		super();
		selectionRectangleController = new SelectionRectangleController();
		featureSearch = INJECTOR.getFeatureSearch();
	}

	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		selectionRectangleController.onActivate(mapPresenter);
	}

	public void onMouseDown(MouseDownEvent event) {
		super.onMouseDown(event);

		if (selectionMethod == SelectionMethod.CLICK_AND_DRAG) {
			selectionRectangleController.onMouseDown(event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		stopPanning(null);
		if (selectionMethod == SelectionMethod.CLICK_AND_DRAG) {
			selectionRectangleController.onMouseUp(event);
		} else {
			// Select item at mouse pointer...
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

	/**
	 * Internal selection by rectangle controller.
	 * 
	 * @author Pieter De Graef
	 */
	private class SelectionRectangleController extends AbstractRectangleController {

		protected void execute(Bbox worldBounds) {
			List<Layer<?>> layers = new ArrayList<Layer<?>>();
			SearchType searchType = SearchType.SEARCH_ALL_LAYERS;
			if (selectionType.equals(SelectionType.SELECTED_LAYER)) {
				if (mapPresenter.getLayersModel().getSelectedLayer() == null) {
					return;
				}
				layers.add(mapPresenter.getLayersModel().getSelectedLayer());
			} else {
				for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
					Layer<?> layer = mapPresenter.getLayersModel().getLayer(i);
					if (layer instanceof FeaturesSupported) {
						layers.add(layer);
					}
				}
				if (selectionType.equals(SelectionType.FIRST_LAYER)) {
					searchType = SearchType.SEARCH_FIRST_LAYER;
				}
			}
			featureSearch.search(mapPresenter.getViewPort().getCrs(), layers, GeometryService.toPolygon(worldBounds),
					0, QueryType.INTERSECTS, searchType, intersectionRatio, new SelectionCallback(true));
		}
	}

	/**
	 * Callback for feature searches that actually selects or deselects the features involved.
	 * 
	 * @author Pieter De Graef
	 */
	private class SelectionCallback implements FeatureCallback {

		private boolean select;

		public SelectionCallback(boolean select) {
			this.select = select;
		}

		public void execute(List<Feature> features) {
			if (features != null && features.size() > 0) {
				for (Feature feature : features) {
					FeaturesSupported fs = (FeaturesSupported) feature.getLayer();
					if (select) {
						fs.selectFeature(feature);
					} else {
						fs.deselectFeature(feature);
					}
				}
			}
		}
	}
}