/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.pipeline;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.layer.feature.InternalFeature;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Container for result of getFeatures in {@link org.geomajas.layer.VectorLayerService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class GetFeaturesContainer {

	private List<InternalFeature> features;
	private Envelope bounds;

	/**
	 * Get the features.
	 *
	 * @return list of features or null when not set yet.
	 */
	public List<InternalFeature> getFeatures() {
		return features;
	}

	/**
	 * Set the list of features.
	 *
	 * @param features features
	 */
	public void setFeatures(List<InternalFeature> features) {
		this.features = features;
	}

	/**
	 * Get area covered by these features.
	 *
	 * @return bounds of the features
	 */
	public Envelope getBounds() {
		return bounds;
	}

	/**
	 * Set the area covered by the features.
	 *
	 * @param bounds bounds for the features
	 */
	public void setBounds(Envelope bounds) {
		this.bounds = bounds;
	}
}
