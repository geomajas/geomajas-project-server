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
package org.geomajas.widget.searchandfilter.client.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.command.dto.GeometryUtilsRequest;
import org.geomajas.widget.searchandfilter.command.dto.GeometryUtilsResponse;

import com.google.gwt.core.client.GWT;

/**
 * Convenience class with helper methods for commands.
 * 
 * @author Kristof Heirwegh
 */
public final class CommService {

	/**
	 * Utility class
	 */
	private CommService() {
	}

	/**
	 * The returned result will contain an unbuffered as well as buffered
	 * result.
	 * 
	 * @param geoms
	 * @param buffer
	 * @param onFinished
	 */
	public static void mergeAndBufferGeometries(List<Geometry> geoms, double buffer,
			final DataCallback<Geometry> onFinished) {
		GeometryUtilsRequest request = new GeometryUtilsRequest();
		request.setActionFlags(GeometryUtilsRequest.ACTION_BUFFER | GeometryUtilsRequest.ACTION_MERGE);
		request.setIntermediateResults(true);
		request.setBuffer(buffer);
		request.setGeometries(toDtoGeometries(geoms));

		GwtCommand command = new GwtCommand("command.searchandfilter.GeometryUtils");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
			public void execute(CommandResponse response) {
				if (response instanceof GeometryUtilsResponse) {
					GeometryUtilsResponse resp = (GeometryUtilsResponse) response;
					if (onFinished != null) {
						onFinished.execute(GeometryConverter.toGwt(resp.getGeometries()[0]));
					}
				}
			}
		});
	}

	/**
	 * @param geoms
	 * @param onFinished
	 */
	public static void mergeGeometries(List<Geometry> geoms, final DataCallback<Geometry> onFinished) {
		GeometryUtilsRequest request = new GeometryUtilsRequest();
		request.setActionFlags(GeometryUtilsRequest.ACTION_MERGE);
		request.setGeometries(toDtoGeometries(geoms));

		GwtCommand command = new GwtCommand("command.searchandfilter.GeometryUtils");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
			public void execute(CommandResponse response) {
				if (response instanceof GeometryUtilsResponse) {
					GeometryUtilsResponse resp = (GeometryUtilsResponse) response;
					if (onFinished != null) {
						onFinished.execute(GeometryConverter.toGwt(resp.getGeometries()[0]));
					}
				}
			}
		});
	}

	public static org.geomajas.geometry.Geometry[] toDtoGeometries(List<Geometry> geoms) {
		org.geomajas.geometry.Geometry[] dtoGeoms = new org.geomajas.geometry.Geometry[geoms.size()];
		for (int i = 0; i < geoms.size(); i++) {
			dtoGeoms[i] = GeometryConverter.toDto(geoms.get(i));
		}
		return dtoGeoms;
	}

	public static SearchByLocationRequest getSearchByLocationRequest(final Geometry geometry, final MapWidget mapWidget)
	{
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLayerIds(getVisibleServerLayerIds(mapWidget.getMapModel()));
		request.setLocation(GeometryConverter.toDto(geometry));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		return request;
	}

	/**
	 * We return the request object used for this search, this can then be
	 * reused for the csv-export.
	 * 
	 * @param geometry
	 * @param buffer
	 * @param onFinished
	 * @param onError callback to execute in case of error, optional use null if you don't need it
	 */
	public static SearchByLocationRequest searchByLocation(final SearchByLocationRequest request,
			final MapWidget mapWidget, final DataCallback<Map<VectorLayer, List<Feature>>> onFinished,
			final Callback onError) {
		GwtCommand commandRequest = new GwtCommand("command.feature.SearchByLocation");
		commandRequest.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {
			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
					onFinished.execute(convertFromDto(response.getFeatureMap(), mapWidget.getMapModel()));
				}
			}
		});
		if (onError != null) {
			def.addErrorCallback(onError);
		}
		return request;
	}

	/**
	 * This also adds the features to their respective layers, so no need to do
	 * that anymore.
	 * 
	 * @param features
	 * @param model
	 * @return
	 */
	private static Map<VectorLayer, List<Feature>> convertFromDto(
			Map<String, List<org.geomajas.layer.feature.Feature>> dtoFeatures, MapModel model) {
		Map<VectorLayer, List<Feature>> result = new LinkedHashMap<VectorLayer, List<Feature>>();
		for (Entry<String, List<org.geomajas.layer.feature.Feature>> entry : dtoFeatures.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				List<Feature> convertedFeatures = new ArrayList<Feature>();
				VectorLayer layer = convertFromDto(entry.getKey(), entry.getValue(), convertedFeatures, model);
				if (layer != null) {
					result.put(layer, convertedFeatures);
				} else {
					// TODO couldn't find layer clientside ?? maybe should throw
					// an error here
					GWT.log("Couldn't find layer clientside ?? " + entry.getKey());
				}
			}
		}
		return result;
	}

	private static VectorLayer convertFromDto(String serverLayerId,
			List<org.geomajas.layer.feature.Feature> dtoFeatures, List<Feature> convertedFeatures, MapModel model) {
		List<VectorLayer> layers = model.getVectorLayersByServerId(serverLayerId);
		for (VectorLayer vectorLayer : layers) {
			for (org.geomajas.layer.feature.Feature dtoFeature : dtoFeatures) {
				org.geomajas.gwt.client.map.feature.Feature feature = new org.geomajas.gwt.client.map.feature.Feature(
						dtoFeature, vectorLayer);
				vectorLayer.getFeatureStore().addFeature(feature);
				convertedFeatures.add(feature);
			}
			return vectorLayer;
		}
		return null;
	}

	private static String[] getVisibleServerLayerIds(MapModel mapModel) {
		List<String> layerIds = new ArrayList<String>();
		for (VectorLayer layer : mapModel.getVectorLayers()) {
			if (layer.isShowing()) {
				layerIds.add(layer.getServerLayerId());
			}
		}
		return layerIds.toArray(new String[] {});
	}
}
