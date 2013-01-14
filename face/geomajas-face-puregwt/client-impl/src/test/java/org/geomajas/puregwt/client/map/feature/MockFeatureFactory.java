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

package org.geomajas.puregwt.client.map.feature;

import org.geomajas.puregwt.client.map.layer.FeaturesSupported;

/**
 * Test implementation of the {@link FeatureFactory}.
 * 
 * @author Jan De Moerloose
 */
public class MockFeatureFactory implements FeatureFactory {

	public Feature create(org.geomajas.layer.feature.Feature feature, FeaturesSupported<?> layer) {
		return new FeatureImpl(feature, layer);
	}
}