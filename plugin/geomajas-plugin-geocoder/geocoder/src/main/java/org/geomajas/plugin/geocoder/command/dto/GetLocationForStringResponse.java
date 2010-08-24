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
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;

import java.util.List;

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
	private String canonicalLocation;
	private Coordinate center;
	private Bbox bbox;
	private String geocoderName;
	private ClientUserDataInfo userData;
	private List<GetLocationForStringAlternative> alternatives;

	/**
	 * Indicator whether a result was found.
	 *
	 * @return true when there is a result
	 */
	public boolean isLocationFound() {
		return locationFound;
	}

	/**
	 * Set whether a result was found.
	 *
	 * @param locationFound true when a result was found
	 */
	public void setLocationFound(boolean locationFound) {
		this.locationFound = locationFound;
	}

	/**
	 * Get string preferred description of matched location.
	 *
	 * @return preferred string for searching this location
	 */
	public String getCanonicalLocation() {
		return canonicalLocation;
	}

	/**
	 * Set the preferred string for matching this location.
	 *
	 * @param canonicalLocation location as string
	 */
	public void setCanonicalLocation(String canonicalLocation) {
		this.canonicalLocation = canonicalLocation;
	}

	/**
	 * Get center of the located area.
	 *
	 * @return center of area
	 */
	public Coordinate getCenter() {
		return center;
	}

	/**
	 * Set center of the located area.
	 *
	 * @param center center
	 */
	public void setCenter(Coordinate center) {
		this.center = center;
	}

	/**
	 * Get bounding box for the located area.
	 *
	 * @return located area
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * Set bounding box for the located area.
	 *
	 * @param bbox located area
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * Get extra user data for the geocoder search result.    Only set when there was only one results from the list of
	 * geocoder services.
	 *
	 * @return user data
	 */
	public ClientUserDataInfo getUserData() {
		return userData;
	}

	/**
	 * Name of the geocoder service which produced this result.
	 * <p/>
	 * This is filled in automatically by the command.
	 *
	 * @return name of the geocoder service which produced this result
	 */
	public String getGeocoderName() {
		return geocoderName;
	}

	/**
	 * Name of the geocoder service which produced this result.
	 * <p/>
	 * This is filled in automatically by the command.
	 *
	 * @param geocoderName name of the geocoder service which produced this result
	 */
	public void setGeocoderName(String geocoderName) {
		this.geocoderName = geocoderName;
	}

	/**
	 * Set extra user data for the geocoder search result.
	 *
	 * @param userData user data
	 */
	public void setUserData(ClientUserDataInfo userData) {
		this.userData = userData;
	}

	/**
	 * Get alternative locations when search was ambiguous. This should only be set when there is no result.
	 *
	 * @return alternative locations for search
	 */
	public List<GetLocationForStringAlternative> getAlternatives() {
		return alternatives;
	}

	/**
	 * Set alternatives when the search was ambiguous. Only set when there was no result.
	 *
	 * @param alternatives alternative locations for search
	 */
	public void setAlternatives(List<GetLocationForStringAlternative> alternatives) {
		this.alternatives = alternatives;
	}
}
