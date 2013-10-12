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

package org.geomajas.gwt2.widget.client.featureselectbox.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.FeatureService.QueryType;
import org.geomajas.gwt2.client.map.feature.FeatureService.SearchLayerType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.widget.client.featureselectbox.event.FeatureClickedEvent;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Listener and presenter of FeatureSelectBox.
 * 
 * @author Dosi Bingov
 * 
 */
public class FeatureClickedListener extends AbstractController implements MapController, FeatureSelectBoxPresenter,
		FeatureSelectBoxPresenter.Handler {

	private Coordinate clickedPosition;

	protected MapPresenter mapPresenter;

	private static final int MIN_PIXEL_DISTANCE = 120; // TODO: min height of the context menu

	private int pixelBuffer;

	private View featureSelectBoxView;

	private Map<String, org.geomajas.gwt2.client.map.feature.Feature> clickedFeatures;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public FeatureClickedListener() {
		// minimum distance between features to show FeatureSelectBox.
		this(32);
	}

	/**
	 *
	 * @param pixelBuffer minimum distance between features to show context menu with features labels.
	 */
	public FeatureClickedListener(int pixelBuffer) {
		super(false);
		clickedFeatures = new HashMap<String, org.geomajas.gwt2.client.map.feature.Feature>();
		featureSelectBoxView = new FeatureSelectBox();
		featureSelectBoxView.setHandler(this);
		setView(featureSelectBoxView);
		this.pixelBuffer = pixelBuffer;
	}

	// -------------------------------------------------------------------------
	// FeatureSelectedListener implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onActivate(MapPresenter presenter) {
		mapPresenter = presenter;
		eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onDeactivate(MapPresenter presenter) {
		featureSelectBoxView.hide();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (featureSelectBoxView.isVisible() && clickedPosition != null) {
			if (getLocation(event, RenderSpace.SCREEN).distance(clickedPosition) > MIN_PIXEL_DISTANCE) {
				featureSelectBoxView.hide();
			}
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		GWT.log("FeatureSelectedListener => mouse down fired");
		featureSelectBoxView.hide();
		featureSelectBoxView.setShowPosition(event.getClientX(), event.getClientY());
		clickedPosition = getLocation(event, RenderSpace.SCREEN);
		searchAtLocation(getLocation(event, RenderSpace.WORLD));
	}

	private void searchAtLocation(Coordinate location) {
		Geometry point = new Geometry(Geometry.POINT, 0, -1);
		point.setCoordinates(new Coordinate[] { location });

		mapPresenter.getFeatureService().search(point, calculateBufferFromPixelTolerance(), QueryType.INTERSECTS,
				SearchLayerType.SEARCH_ALL_LAYERS, -1, new FeatureMapFunction() {

					@Override
					public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {
						getData(featureMap);

					}
				});
	}

	private void getData(Map<FeaturesSupported, List<Feature>> featureMap) {
		clickedFeatures.clear(); // clear all stored features

		for (FeaturesSupported layer : featureMap.keySet()) {
			List<org.geomajas.gwt2.client.map.feature.Feature> features = featureMap.get(layer);

			if (features != null) {
				for (org.geomajas.gwt2.client.map.feature.Feature f : features) {
					GWT.log("Feature label =" + f.getLabel());
					GWT.log("Feature id =" + f.getId());

					// store features in a hashmap
					clickedFeatures.put(f.getLabel(), f);
				}

			}
		}

		showFeatureData();
	}

	/**
	 * shows context menu if more than 1 feature in the buffered area or fires feature selected event if 1 feature
	 */
	private void showFeatureData() {
		// when there is more than one feature in the buffered area
		if (clickedFeatures.size() >= 2) {
			featureSelectBoxView.clear();

			for (org.geomajas.gwt2.client.map.feature.Feature f : clickedFeatures.values()) {
				featureSelectBoxView.addLabel(f.getLabel());
			}

			featureSelectBoxView.show(false);
		} else if (clickedFeatures.size() == 1) {
			Feature clickedFeuture = (Feature) clickedFeatures.values().toArray()[0];

			mapPresenter.getEventBus().fireEvent(new FeatureClickedEvent(clickedFeuture));
		}
	}

	@Override
	public void onFeatureSelected(String label) {
		featureSelectBoxView.hide();
		Feature clickedFeuture = clickedFeatures.get(label);

		mapPresenter.getEventBus().fireEvent(new FeatureClickedEvent(clickedFeuture));

	}

	private double calculateBufferFromPixelTolerance() {
		Coordinate c1 = mapPresenter.getViewPort().transform(new Coordinate(0, 0), RenderSpace.SCREEN,
				RenderSpace.WORLD);
		Coordinate c2 = mapPresenter.getViewPort().transform(new Coordinate(pixelBuffer, 0), RenderSpace.SCREEN,
				RenderSpace.WORLD);
		return c1.distance(c2);
	}

	@Override
	public void setView(View view) {
		featureSelectBoxView = view;
	}

	@Override
	public View getView() {
		return featureSelectBoxView;
	}
}
