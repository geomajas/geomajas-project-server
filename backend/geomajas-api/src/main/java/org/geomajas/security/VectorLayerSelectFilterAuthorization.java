/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.security;

import org.opengis.filter.Filter;

/**
 * Extra filter which should be applied by the layer model when selecting features.
 * <p/>
 * Note that while it is possible to use this filter to limit the features returned by the layer, it does not affect
 * creation of features and may not be applied when trying to fetch features by id. When such checks are needed, you
 * should also implement {@link org.geomajas.security.FeatureAuthorization} and do the checks there.
 *
 * @author Joachim Van der Auwera
 */
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
