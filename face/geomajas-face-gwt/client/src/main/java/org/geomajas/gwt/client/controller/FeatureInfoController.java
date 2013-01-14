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

package org.geomajas.gwt.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.FeatureAttributeWindow;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Shows information about the clicked feature.
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class FeatureInfoController extends AbstractGraphicsController {

	/** Number of pixels that describes the tolerance allowed when trying to select features. */
	private int pixelTolerance;

	public FeatureInfoController(MapWidget mapWidget, int pixelTolerance) {
		super(mapWidget);
		this.pixelTolerance = pixelTolerance;
	}

	/**
	 * On mouse up, execute the search by location, and display a
	 * {@link org.geomajas.gwt.client.widget.FeatureAttributeWindow} if a result is found.
	 */
	public void onMouseUp(MouseUpEvent event) {
		Coordinate worldPosition = getWorldPosition(event);
		Point point = mapWidget.getMapModel().getGeometryFactory().createPoint(worldPosition);

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_FIRST_LAYER);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		request.setLayerIds(getServerLayerIds(mapWidget.getMapModel()));
		for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
			if (layer.isShowing() && layer instanceof VectorLayer) {
				request.setFilter(layer.getServerLayerId(), ((VectorLayer) layer).getFilter());
			}
		}

		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<SearchByLocationResponse>() {

			public void execute(SearchByLocationResponse response) {
				Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
				for (String serverLayerId : featureMap.keySet()) {
					List<VectorLayer> layers = mapWidget.getMapModel().getVectorLayersByServerId(serverLayerId);
					for (VectorLayer vectorLayer : layers) {
						List<org.geomajas.layer.feature.Feature> orgFeatures = featureMap.get(serverLayerId);
						if (orgFeatures.size() > 0) {
							Feature feature = new Feature(orgFeatures.get(0), vectorLayer);
							vectorLayer.getFeatureStore().addFeature(feature);
							FeatureAttributeWindow window = new FeatureAttributeWindow(feature, false);
							window.setPageTop(mapWidget.getAbsoluteTop() + 10);
							window.setPageLeft(mapWidget.getAbsoluteLeft() + 10);
							window.draw();
						}
					}
				}
			}
		});
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private String[] getServerLayerIds(MapModel mapModel) {
		List<String> layerIds = new ArrayList<String>();
		for (Layer<?> layer : mapModel.getLayers()) {
			if (layer.isShowing()) {
				layerIds.add(layer.getServerLayerId());
			}
		}
		return layerIds.toArray(new String[layerIds.size()]);
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}
}
