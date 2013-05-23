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

package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Event which is passed when a feature is deselected.
 * 
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class FeatureDeselectedEvent extends BaseLayerEvent<FeatureSelectionHandler> {

	private final Feature feature;
	
	/**
	 * Create an event for the specified layer and feature.
	 * 
	 * @param layer the layer of the feature
	 * @param feature the deselected feature
	 */
	public FeatureDeselectedEvent(Layer layer, Feature feature) {
		super(layer);
		this.feature = feature;
	}

	/**
	 * Get the deselected feature.
	 * 
	 * @return The feature.
	 */
	public Feature getFeature() {
		return feature;
	}

	@Override
	@Override
	public Type<FeatureSelectionHandler> getAssociatedType() {
		return FeatureSelectionHandler.TYPE;
	}

	@Override
	protected void dispatch(FeatureSelectionHandler featureSelectionHandler) {
		featureSelectionHandler.onFeatureDeselected(this);
	}
}