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

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.layer.FeaturesSupported;

/**
 * GIN factory for {@link Feature} objects.
 * 
 * @author Jan De Moerloose
 */
@Api(allMethods = true)
public interface FeatureFactory {

	/**
	 * creates a {@link Feature} from a DTO feature and layer.
	 * 
	 * @param feature the DTO feature
	 * @param layer the layer
	 * @return a new {@link Feature}
	 */
	Feature create(org.geomajas.layer.feature.Feature feature, FeaturesSupported layer);
}