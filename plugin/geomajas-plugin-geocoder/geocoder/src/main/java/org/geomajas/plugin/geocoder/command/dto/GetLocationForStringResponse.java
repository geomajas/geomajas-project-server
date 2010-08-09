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

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;

/**
 * Response object for getting location information for a string.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GetLocationForStringResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private boolean locationFound;
	private String matchedLocation;
	private Coordinate center;
	private Bbox bbox;

	public boolean isLocationFound() {
		return locationFound;
	}

	public void setLocationFound(boolean locationFound) {
		this.locationFound = locationFound;
	}

	public String getMatchedLocation() {
		return matchedLocation;
	}

	public void setMatchedLocation(String matchedLocation) {
		this.matchedLocation = matchedLocation;
	}

	public Coordinate getCenter() {
		return center;
	}

	public void setCenter(Coordinate center) {
		this.center = center;
	}

	public Bbox getBbox() {
		return bbox;
	}

	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}
}
