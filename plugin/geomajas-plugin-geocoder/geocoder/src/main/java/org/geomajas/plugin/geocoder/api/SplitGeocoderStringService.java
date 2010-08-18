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
