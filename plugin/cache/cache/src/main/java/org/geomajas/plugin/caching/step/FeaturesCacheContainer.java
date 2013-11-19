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

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.feature.InternalFeature;

import java.util.List;

/**
 * Container for the objects which need to be stored in the features cache.
 *
 * @author Joachim Van der Auwera
 */
public class FeaturesCacheContainer extends CacheContainer {

	private static final long serialVersionUID = 100L;

	private final List<InternalFeature> features;
	private final Envelope bounds;

	/**
	 * Create for specific features.
	 *
	 * @param features features
	 * @param bounds bounds for features
	 */
	public FeaturesCacheContainer(List<InternalFeature> features, Envelope bounds) {
		super();
		this.features = features;
		this.bounds = bounds;
	}

	/**
	 * Get the cached features.
	 *
	 * @return features
	 */
	public List<InternalFeature> getFeatures() {
		return features;
	}

	/**
	 * Bounds for the features.
	 *
	 * @return feature bounds
	 */
	public Envelope getBounds() {
		return bounds;
	}
}
