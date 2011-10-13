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
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.layer;

import org.geomajas.annotation.FutureApi;
import org.geomajas.jsapi.map.feature.Feature;
import org.geomajas.jsapi.map.layer.FeaturesSupported;
import org.geomajas.jsapi.map.layer.Layer;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exportable facade for {@link org.geomajas.gwt.client.map.layer.VectorLayer} in javascript.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.map.layer")
public class VectorLayer extends LayerImpl implements Exportable, Layer, FeaturesSupported {

	// private int fetchType = GeomajasConstant.FEATURE_INCLUDE_ALL;

	/**
	 * TODO.
	 * 
	 * @param layer
	 * @since 1.0.0
	 */
	@FutureApi
	public VectorLayer(org.geomajas.gwt.client.map.layer.VectorLayer layer) {
		super(layer);
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	public void setFilter(String filter) {
		getLayer().setFilter(filter);
	}

	public String getFilter() {
		return getLayer().getFilter();
	}

	public boolean isFeatureSelected(String featureId) {
		return getLayer().isFeatureSelected(featureId);
	}

	public boolean selectFeature(Feature feature) {
		return getLayer().selectFeature(toGwt(feature));
	}

	public boolean deselectFeature(Feature feature) {
		return getLayer().deselectFeature(toGwt(feature));
	}

	public void clearSelectedFeatures() {
		getLayer().clearSelectedFeatures();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private org.geomajas.gwt.client.map.layer.VectorLayer getLayer() {
		return (org.geomajas.gwt.client.map.layer.VectorLayer) layer;
	}

	private org.geomajas.gwt.client.map.feature.Feature toGwt(Feature feature) {
		org.geomajas.gwt.client.map.feature.Feature gwt;
		gwt = new org.geomajas.gwt.client.map.feature.Feature(feature.getId(), getLayer());
		// gwt.setAttributes(feature.getAttributeValue(attributeName));
		return gwt;
	}

	// public String[] getSelectedFeatures() {
	// return (String[]) layer.getSelectedFeatures().toArray(new String[layer.getSelectedFeatures().size()]);
	// }
	//
	// public void setFilter(String filter) {
	// layer.setFilter(filter);
	// }
	//
	// public String getFilter() {
	// return layer.getFilter();
	// }
	//
	// public boolean isFeatureSelected(String featureId) {
	// return layer.isFeatureSelected(featureId);
	// }
	//
	// public void selectFeature(String featureId, final FeatureCallback callBack) {
	// layer.getFeatureStore().getFeature(featureId, fetchType, new LazyLoadCallback() {
	//
	// public void execute(List<org.geomajas.gwt.client.map.feature.Feature> response) {
	// if (response.size() > 0 && response.get(0).isAttributesLoaded()) {
	// layer.selectFeature(response.get(0));
	// Feature[] features = { fromGwtFeature(response.get(0)) };
	// callBack.execute(features[0]);
	// } else {
	// callBack.execute((Feature) null);
	// }
	// }
	// });
	// }
	//
	// public void deSelectFeature(String featureId, final FeatureCallback callBack) {
	// layer.getFeatureStore().getFeature(featureId, fetchType, new LazyLoadCallback() {
	//
	// public void execute(List<org.geomajas.gwt.client.map.feature.Feature> response) {
	// if (response.size() > 0 && response.get(0).isAttributesLoaded()) {
	// layer.deselectFeature(response.get(0));
	// Feature[] features = { fromGwtFeature(response.get(0)) };
	// callBack.execute(features[0]);
	// } else {
	// callBack.execute((Feature) null);
	// }
	// }
	// });
	// }
	//
	// public void clearSelectedFeatures() {
	// layer.clearSelectedFeatures();
	// }
	//
	// public String[] getSelectedFeatureIds() {
	// return layer.getSelectedFeatures().toArray(new String[layer.getSelectedFeatures().size()]);
	// }
	//
	// public void getFeature(String featureId, final FeatureCallback callBack) {
	// layer.getFeatureStore().getFeature(featureId, fetchType, new LazyLoadCallback() {
	//
	// public void execute(List<org.geomajas.gwt.client.map.feature.Feature> response) {
	// if (response.size() > 0 && response.get(0).isAttributesLoaded()) {
	// callBack.execute(fromGwtFeature(response.get(0)));
	// } else {
	// callBack.execute((Feature) null);
	// }
	// }
	// });
	// }
	//
	// public void getFeatures(Bbox bounds, final FeaturesCallback callBack) {
	// GeometryFactory gf = new GeometryFactory(layer.getMapModel().getSrid(),
	// GeometryFactory.PARAM_DEFAULT_PRECISION);
	//
	// SearchByLocationRequest request = new SearchByLocationRequest();
	// request.setLayerIds(new String[]{getServerLayerId()});
	//
	// org.geomajas.gwt.client.spatial.Bbox box = new org.geomajas.gwt.client.spatial.Bbox(
	// bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	// request.setLocation(GeometryConverter.toDto(gf.createPolygon(box)));
	//
	// request.setCrs(layer.getMapModel().getCrs());
	// request.setSearchType(SearchByLocationRequest.QUERY_CONTAINS);
	// request.setSearchType(SearchByLocationRequest.SEARCH_FIRST_LAYER);
	// request.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_NONE);
	// request.setFilter(layer.getServerLayerId(), layer.getFilter());
	//
	// GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
	// commandRequest.setCommandRequest(request);
	// GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {
	//
	// public void execute(CommandResponse commandResponse) {
	// if (commandResponse instanceof SearchByLocationResponse) {
	// SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
	// Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
	// List<String> features = new ArrayList<String>();
	// if (featureMap.get(layer.getServerLayerId()) != null) {
	// for (org.geomajas.layer.feature.Feature f : featureMap.get(layer.getServerLayerId())) {
	// features.add(f.getId());
	// }
	// }
	// callBack.execute(features.toArray(new String[features.size()]));
	// }
	// }
	//
	// });
	// }
	//
	//
	//
	// /**
	// * Set the fetchType @see {@link GeomajasConstant} for possible values. Note that attributes are always fetched
	// * despite the value of fetchType.
	// *
	// * @param fetchType
	// * the data to include in a fetched feature.
	// */
	// @FutureApi
	// public void setFetchType(int fetchType) {
	// // Make sure attributes are always fetched.
	// if (fetchType % 2 == 0) {
	// this.fetchType = fetchType + 1;
	// } else {
	// this.fetchType = fetchType;
	// }
	//
	// }
	//
}