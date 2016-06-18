/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.api;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;
import java.util.Locale;

/**
 * Geocoder service which allows location lookup based on a list of strings (eg "BE", "Gent", "Gaston Crommenlaan").
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
// @extract-start GeocoderService, Geocoder service interface definition
public interface GeocoderService {

	/**
	 * Name for the geocoder service. This name can be used to select which geocoder services are considered during the
	 * search.
	 * .
	 * @return name for this geocoder
	 */
	String getName();

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
	 * @param maxAlternatives maximum number of alternatives which can be replied to the user, use as a hint, you can
	 *        return more, but they will be discarded.
	 * @param locale locale use for the location if known (can be null if not known)
	 * @return results objects, when only one this is a definite result, when several the request was ambiguous and the
	 *         result are the alternatives. When no results an empty array or null may be returned
	 */
	GetLocationResult[] getLocation(List<String> location, int maxAlternatives, Locale locale);
}
// @extract-end
