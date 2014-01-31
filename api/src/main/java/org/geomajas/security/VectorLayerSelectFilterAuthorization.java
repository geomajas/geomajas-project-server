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

package org.geomajas.security;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.opengis.filter.Filter;

/**
 * Extra filter which should be applied by the layer model when selecting features.
 * <p/>
 * Note that while it is possible to use this filter to limit the features returned by the layer, it does not affect
 * creation of features and may not be applied when trying to fetch features by id. When such checks are needed, you
 * should also implement {@link org.geomajas.security.FeatureAuthorization} and do the checks there.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface VectorLayerSelectFilterAuthorization extends BaseAuthorization {

	/**
	 * Get a filter which limits the visible data which can be used.
	 * <p/>
	 * This allows for example filtering cities on number of inhabitants, features on their area etc.
	 *
	 * @param layerId layer id for which the filter applies
	 * @return string representation of the filter
	 */
	Filter getFeatureFilter(String layerId);
}
