/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.layer.feature;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.geomajas.layer.feature.FeatureModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Feature model registry.
 *
 * @author Pieter De Graef
 */
public final class FeatureModelRegistry {

	private final Logger log = LoggerFactory.getLogger(FeatureModelRegistry.class);

	private static FeatureModelRegistry instance = new FeatureModelRegistry();

	private List<FeatureModel> featureModels = new CopyOnWriteArrayList<FeatureModel>();

	private FeatureModelRegistry() {
		// hide constructor
	}

	public static FeatureModelRegistry getRegistry() {
		return instance;
	}

	public FeatureModel lookup(Object feature) {
		for (FeatureModel fm : featureModels) {
			try {
				if (fm.canHandle(feature)) {
					return fm;
				}
			} catch (Exception ex) { // NOSONAR
				log.warn("Problem in feature model lookup, ignoring", ex);
			}
		}

		return null;
	}

	public void register(FeatureModel featureModel) {
		if (!featureModels.contains(featureModel)) {
			featureModels.add(featureModel);
		}
	}
}