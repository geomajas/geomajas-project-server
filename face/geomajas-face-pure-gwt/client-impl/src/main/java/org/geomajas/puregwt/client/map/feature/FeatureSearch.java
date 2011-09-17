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

package org.geomajas.puregwt.client.map.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.puregwt.client.map.layer.FeaturesSupported;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.spatial.Geometry;

import com.google.inject.Inject;

/**
 * Utility class that helps in searching or retrieving features.
 * 
 * @author Pieter De Graef
 */
public final class FeatureSearch {

	/**
	 * Definition of geometric query types.
	 * 
	 * @author Pieter De Graef
	 */
	public enum QueryType {
		INTERSECTS(1), TOUCHES(2), WITHIN(3), CONTAINS(4);

		private int value;

		private QueryType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	};

	/**
	 * Search types that determines which layers to search.
	 * 
	 * @author Pieter De Graef
	 */
	public enum SearchType {
		/**
		 * Go over the layers in the given order, but stop searching the moment a layer returns a result. If 4 layers
		 * are passed to the search, and the second layer produces a result, then the third and fourth layer will not be
		 * searched anymore. This option will therefore return results for maximum 1 layer.
		 */
		SEARCH_FIRST_LAYER(1),

		/**
		 * Always search in all given layers. Some layers may produce results while others will not. This option does
		 * not care, it will always loop over all layers.
		 */
		SEARCH_ALL_LAYERS(2);

		private int value;

		private SearchType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();

	@Inject
	private GeometryConverter geometryConverter;

	private int resultSize = 100;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	@Inject
	private FeatureSearch() {
	}

	// ------------------------------------------------------------------------
	// Feature search methods:
	// ------------------------------------------------------------------------

	public void search(String crs, final Layer<?> layer, Geometry location, double buffer,
			final FeatureCallback callback) {
		if (layer instanceof FeaturesSupported) {
			FeaturesSupported fs = (FeaturesSupported) layer;
			SearchByLocationRequest request = new SearchByLocationRequest();
			request.setBuffer(buffer);
			request.setLayerIds(new String[] { layer.getServerLayerId() });
			request.setFilter(layer.getId(), fs.getFilter());
			request.setLocation(geometryConverter.toDto(location));
			request.setSearchType(SearchType.SEARCH_ALL_LAYERS.getValue());
			request.setCrs(crs);
			request.setFeatureIncludes(11);

			GwtCommand command = new GwtCommand(SearchByLocationRequest.COMMAND);
			command.setCommandRequest(request);
			dispatcher.execute(command, new AbstractCommandCallback<SearchByLocationResponse>() {

				public void execute(SearchByLocationResponse response) {
					for (List<org.geomajas.layer.feature.Feature> dtos : response.getFeatureMap().values()) {
						List<Feature> features = new ArrayList<Feature>(dtos.size());
						for (org.geomajas.layer.feature.Feature feature : dtos) {
							features.add(new FeatureImpl(feature, layer));
						}
						callback.execute(features);
					}
				}
			});
		}
	}

	public void search(String crs, final List<Layer<?>> layers, Geometry location, double buffer, QueryType queryType,
			SearchType searchType, float ratio, final FeatureCallback callback) {
		SearchByLocationRequest request = new SearchByLocationRequest();
		String[] layerIds = new String[layers.size()];
		for (int i = 0; i < layers.size(); i++) {
			Layer<?> layer = layers.get(i);
			layerIds[i] = layer.getServerLayerId();
			if (layer instanceof FeaturesSupported) {
				request.setFilter(layer.getId(), ((FeaturesSupported) layer).getFilter());
			}
		}

		request.setBuffer(buffer);
		request.setLayerIds(layerIds);
		request.setLocation(geometryConverter.toDto(location));
		request.setQueryType(queryType.getValue());
		request.setSearchType(searchType.getValue());
		request.setRatio(ratio);
		request.setCrs(crs);
		request.setFeatureIncludes(11);

		GwtCommand command = new GwtCommand(SearchByLocationRequest.COMMAND);
		command.setCommandRequest(request);
		dispatcher.execute(command, new AbstractCommandCallback<SearchByLocationResponse>() {

			public void execute(SearchByLocationResponse response) {
				for (Entry<String, List<org.geomajas.layer.feature.Feature>> entry : response.getFeatureMap()
						.entrySet()) {
					Layer<?> layer = searchLayer(layers, entry.getKey());
					List<Feature> features = new ArrayList<Feature>(entry.getValue().size());
					for (org.geomajas.layer.feature.Feature feature : entry.getValue()) {
						features.add(new FeatureImpl(feature, layer));
					}
					callback.execute(features);
				}
			}
		});
	}

	public void search(String crs, final Layer<?> layer, SearchCriterion[] criteria, final FeatureCallback callback) {
		if (layer instanceof FeaturesSupported) {
			FeaturesSupported fs = (FeaturesSupported) layer;
			SearchFeatureRequest request = new SearchFeatureRequest();
			request.setBooleanOperator("OR");
			request.setCriteria(criteria);
			request.setMax(resultSize);
			request.setLayerId(layer.getServerLayerId());
			request.setCrs(crs);
			request.setFilter(fs.getFilter());
			request.setFeatureIncludes(11);

			GwtCommand command = new GwtCommand(SearchFeatureRequest.COMMAND);
			command.setCommandRequest(request);
			dispatcher.execute(command, new AbstractCommandCallback<SearchFeatureResponse>() {

				public void execute(SearchFeatureResponse response) {
					List<Feature> features = new ArrayList<Feature>();
					for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
						features.add(new FeatureImpl(feature, layer));
					}
					callback.execute(features);
				}
			});
		}
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	private Layer<?> searchLayer(List<Layer<?>> layers, String layerId) {
		for (Layer<?> layer : layers) {
			if (layerId.equals(layer.getServerLayerId())) {
				return layer;
			}
		}
		return null;
	}
}