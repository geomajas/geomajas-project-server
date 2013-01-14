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

package org.geomajas.plugin.jsapi.gwt.client.exporter.map.feature;

import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.geomajas.plugin.jsapi.client.map.feature.FeatureArrayCallback;
import org.geomajas.plugin.jsapi.client.map.feature.FeatureArrayCallback.FeatureArrayHolder;
import org.geomajas.plugin.jsapi.client.map.feature.FeatureSearchService;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.MapImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Service definition for searching for features. It defines a list of methods that do nothing but presenting different
 * ways of searching features.
 * 
 * @author Pieter De Graef
 */
@Export("FeatureSearchService")
@ExportPackage("org.geomajas.jsapi.map.feature")
public class FeatureSearchServiceImpl implements FeatureSearchService, Exportable {

	private MapImpl map;

	public FeatureSearchServiceImpl() {
	}

	public FeatureSearchServiceImpl(MapImpl map) {
		this.map = map;
	}

	/**
	 * Search features within a certain layer, using the feature IDs.
	 * 
	 * @param layer
	 *            The features supported layer wherein to search.
	 * @param ids
	 *            The unique IDs of the feature within the layer.
	 * @param callback
	 *            Call-back method executed on return (when the feature has been found).
	 */
	public void searchById(final FeaturesSupported layer, final String[] ids, final FeatureArrayCallback callback) {
		Layer<?> gwtLayer = map.getMapWidget().getMapModel().getLayer(layer.getId());
		if (gwtLayer != null && gwtLayer instanceof VectorLayer) {
			VectorLayer vLayer = (VectorLayer) gwtLayer;
			SearchCriterion[] criteria = new SearchCriterion[ids.length];
			for (int i = 0; i < ids.length; i++) {
				criteria[i] = new SearchCriterion(SearchFeatureRequest.ID_ATTRIBUTE, "=", ids[i]);
			}

			SearchFeatureRequest request = new SearchFeatureRequest();
			request.setBooleanOperator("OR");
			request.setCrs(map.getMapWidget().getMapModel().getCrs());
			request.setLayerId(vLayer.getServerLayerId());
			request.setMax(ids.length);
			request.setFilter(layer.getFilter());
			request.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_ALL);
			request.setCriteria(criteria);

			GwtCommand command = new GwtCommand(SearchFeatureRequest.COMMAND);
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SearchFeatureResponse>() {

				public void execute(SearchFeatureResponse response) {
					if (response.getFeatures() != null && response.getFeatures().length > 0) {
						Feature[] features = new Feature[response.getFeatures().length];
						for (int i = 0; i < response.getFeatures().length; i++) {
							features[i] = new FeatureImpl(response.getFeatures()[i], layer);
						}
						callback.execute(new FeatureArrayHolder(features));
					}
				}
			});
		}
	}

	/**
	 * Search all features within a certain layer that intersect a certain bounding box.
	 * 
	 * @param layer
	 *            The features supported layer wherein to search.
	 * @param bbox
	 *            The bounding box wherein to search.
	 * @param callback
	 *            Call-back method executed on return (when features have been found).
	 */
	public void searchInBounds(final FeaturesSupported layer, Bbox bbox, final FeatureArrayCallback callback) {
		MapModel mapModel = map.getMapWidget().getMapModel();
		Layer<?> gwtLayer = mapModel.getLayer(layer.getId());
		if (gwtLayer != null && gwtLayer instanceof VectorLayer) {
			final VectorLayer vLayer = (VectorLayer) gwtLayer;

			SearchByLocationRequest request = new SearchByLocationRequest();
			request.addLayerWithFilter(vLayer.getId(), vLayer.getServerLayerId(), layer.getFilter());

			GeometryFactory factory = new GeometryFactory(mapModel.getSrid(), GeometryFactory.PARAM_DEFAULT_PRECISION);
			org.geomajas.gwt.client.spatial.Bbox box = new org.geomajas.gwt.client.spatial.Bbox(bbox.getX(),
					bbox.getY(), bbox.getWidth(), bbox.getHeight());
			request.setLocation(GeometryConverter.toDto(factory.createPolygon(box)));

			request.setCrs(mapModel.getCrs());
			request.setSearchType(SearchByLocationRequest.QUERY_INTERSECTS);
			request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
			request.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_ALL);

			GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
			commandRequest.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(commandRequest,
					new AbstractCommandCallback<SearchByLocationResponse>() {

				public void execute(SearchByLocationResponse response) {
					Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
					List<org.geomajas.layer.feature.Feature> dtos = featureMap.get(vLayer.getId());
					Feature[] features = new Feature[dtos.size()];
					for (int i = 0; i < dtos.size(); i++) {
						features[i] = new FeatureImpl(dtos.get(i), layer);
					}
					callback.execute(new FeatureArrayHolder(features));
				}
			});
		}
	}
}