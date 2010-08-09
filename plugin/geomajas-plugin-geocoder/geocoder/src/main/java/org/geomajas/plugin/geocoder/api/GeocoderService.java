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

package org.geomajas.plugin.geocoder.api;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * Geocoder service which allows location lookup based on a list of strings (eg "BE", "Gent", "Gaston Crommenlaan").
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeocoderService {

	/**
	 * CRS which is used for the results of this geocoder.
	 *
	 * @return CRS
	 */
	CoordinateReferenceSystem getCrs();

	/**
	 * Try to get a location for the strings passed. This can be either a coordinate or an envelope, as passed in the
	 * result object.
	 *
	 * @param location location strings, from general to more specific
	 * @return result object or null when no match
	 */
	GetLocationResult getLocation(List<String> location);
}
