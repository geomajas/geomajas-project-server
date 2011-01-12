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
package org.geomajas.internal.layer.feature;

import org.geomajas.layer.feature.FeatureModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature model registry.
 *
 * @author Pieter De Graef
 */
public class FeatureModelRegistry {

	private static FeatureModelRegistry INSTANCE;

	private List<FeatureModel> featureModels = new ArrayList<FeatureModel>();

	public static FeatureModelRegistry getRegistry() {
		if (INSTANCE == null) {
			INSTANCE = new FeatureModelRegistry();
		}
		return INSTANCE;
	}

	public FeatureModel lookup(Object feature) {
		for (FeatureModel fm : featureModels) {
			if (fm.canHandle(feature)) {
				return fm;
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