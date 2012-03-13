/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Event which is passed when a feature is selected.
 * 
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class FeatureSelectedEvent extends BaseLayerEvent<FeatureSelectionHandler> {

	private Feature feature;

	public FeatureSelectedEvent(Layer<?> layer, Feature feature) {
		super(layer);
		this.feature = feature;
	}

	public Feature getFeature() {
		return feature;
	}

	public Type<FeatureSelectionHandler> getAssociatedType() {
		return FeatureSelectionHandler.TYPE;
	}

	protected void dispatch(FeatureSelectionHandler featureSelectionHandler) {
		featureSelectionHandler.onFeatureSelected(this);
	}
}