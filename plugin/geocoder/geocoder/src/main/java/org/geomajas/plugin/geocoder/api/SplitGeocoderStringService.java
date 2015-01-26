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

package org.geomajas.plugin.geocoder.api;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import java.util.List;

/**
 * Split the geocoder search string in relevant parts and order from most important to less important.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
// @extract-start SplitGeocoderStringService, Service interface for splitting the search string
public interface SplitGeocoderStringService {

	/**
	 * Split the given string in a list of strings according to the separator convention used. After splitting the
	 * biggest area should come first (assuming the original format had a notion of such ordering).
	 *
	 * @param location location to split
	 * @return list of strings with split location
	 */
	List<String> split(String location);

	/**
	 * Combine the list of strings back into one string accoring to the conventions used for this service. Should be
	 * the reverse of the split functions. As end result split(combine(split(x)) should equal split(x).
	 *
	 * @param matchedStrings strings to combine
	 * @return combined string
	 */
	String combine(List<String> matchedStrings);
}
// @extract-end
