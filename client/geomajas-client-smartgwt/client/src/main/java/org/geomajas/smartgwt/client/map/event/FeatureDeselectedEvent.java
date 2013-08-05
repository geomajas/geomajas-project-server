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

package org.geomajas.smartgwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.map.feature.Feature;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event which is passed when a feature is deselected.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FeatureDeselectedEvent extends GwtEvent<FeatureSelectionHandler> {

	private Feature feature;

	/**
	 * Constructor.
	 *
	 * @param feature deselected feature
	 */
	public FeatureDeselectedEvent(Feature feature) {
		this.feature = feature;
	}

	/**
	 * Get deselected feature.
	 *
	 * @return deselected feature
	 */
	public Feature getFeature() {
		return feature;
	}

	@Override
	public Type<FeatureSelectionHandler> getAssociatedType() {
		return FeatureSelectionHandler.TYPE;
	}

	@Override
	protected void dispatch(FeatureSelectionHandler featureSelectionHandler) {
		featureSelectionHandler.onFeatureDeselected(this);
	}
}
