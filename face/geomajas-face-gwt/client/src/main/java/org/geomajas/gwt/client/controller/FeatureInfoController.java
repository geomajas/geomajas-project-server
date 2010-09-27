/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
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
		Layer<?> layer = mapWidget.getMapModel().getSelectedLayer();
		if (null != layer && layer instanceof VectorLayer) {
			request.setFilter(((VectorLayer) layer).getFilter());
		}

		GwtCommand commandRequest = new GwtCommand("command.feature.SearchByLocation");
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
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
		return layerIds.toArray(new String[] {});
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}
}
