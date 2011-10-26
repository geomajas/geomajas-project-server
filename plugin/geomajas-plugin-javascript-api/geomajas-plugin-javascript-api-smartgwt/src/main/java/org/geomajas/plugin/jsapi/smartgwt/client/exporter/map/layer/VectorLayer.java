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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.jsapi.map.feature.Feature;
import org.geomajas.jsapi.map.layer.FeaturesSupported;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.feature.FeatureImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exportable facade for {@link org.geomajas.gwt.client.map.layer.VectorLayer} in javascript.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("VectorLayer")
@ExportPackage("org.geomajas.jsapi.map.layer")
public class VectorLayer extends LayerImpl implements Exportable, FeaturesSupported {

	public VectorLayer() {
	}

	/**
	 * Initialize this {@link FeaturesSupported} layer with a {@link VectorLayer} from the GWT face.
	 * 
	 * @param layer
	 *            The {@link VectorLayer} to wrap.
	 * @since 1.0.0
	 */
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

	public Feature[] getSelectedFeatures() {
		Collection<org.geomajas.gwt.client.map.feature.Feature> selection = getLayer().getSelectedFeatureValues();
		Feature[] features = new Feature[selection.size()];
		int count = 0;
		for (org.geomajas.gwt.client.map.feature.Feature feature : selection) {
			features[count] = new FeatureImpl(feature.toDto(), this);
			count++;
		}
		return features;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private org.geomajas.gwt.client.map.layer.VectorLayer getLayer() {
		return (org.geomajas.gwt.client.map.layer.VectorLayer) layer;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private org.geomajas.gwt.client.map.feature.Feature toGwt(Feature feature) {
		org.geomajas.gwt.client.map.feature.Feature gwt;
		gwt = new org.geomajas.gwt.client.map.feature.Feature(feature.getId(), getLayer());
		gwt.setGeometry(GeometryConverter.toGwt(feature.getGeometry()));

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		for (AttributeInfo info : getLayer().getLayerInfo().getFeatureInfo().getAttributes()) {
			// TODO transform attributes....
		}
		gwt.setAttributes(attributes);
		return gwt;
	}
}