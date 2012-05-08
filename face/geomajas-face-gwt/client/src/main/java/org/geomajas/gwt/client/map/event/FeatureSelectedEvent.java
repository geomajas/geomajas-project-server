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

package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.feature.Feature;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event which is passed when a feature is selected.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FeatureSelectedEvent extends GwtEvent<FeatureSelectionHandler> {

	private Feature feature;

	/**
	 * Constructor.
	 *
	 * @param feature selected feature
	 */
	public FeatureSelectedEvent(Feature feature) {
		this.feature = feature;
	}

	/**
	 * Get selected feature.
	 *
	 * @return selected feature
	 */
	public Feature getFeature() {
		return feature;
	}

	/** {@inheritDoc} */
	public Type<FeatureSelectionHandler> getAssociatedType() {
		return FeatureSelectionHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(FeatureSelectionHandler featureSelectionHandler) {
		featureSelectionHandler.onFeatureSelected(this);
	}
}
