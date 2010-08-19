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

package org.geomajas.plugin.geocoder.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.global.Api;

/**
 * Request object for getting location information for a string.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GetLocationForStringRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	private String location;
	private String crs;
	private String servicePattern = ".*";

	/**
	 * Get the location string to search for.
	 *
	 * @return location string
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Set the location string to search for.
	 *
	 * @param location location string
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned location.
	 *
	 * @return The map's coordinate reference system.
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned location.
	 *
	 * @param crs The map's coordinate reference system.
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the regular expression for the geocoder names which should be used for the search.
	 *
	 * @return regular expression for selecting the geocoders to use
	 */
	public String getServicePattern() {
		return servicePattern;
	}

	/**
	 * Set the regular expression for the geocoder names which should be used for the search.
	 *
	 * @param servicePattern regular expression for selecting the geocoders to use
	 */
	public void setServicePattern(String servicePattern) {
		this.servicePattern = servicePattern;
	}
	
}
