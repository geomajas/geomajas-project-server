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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.global.Api;

import java.util.List;

/**
 * Class which encapsulates the {@link org.geomajas.plugin.geocoder.api.GeocoderService#getLocation} result.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
// @extract-start GetLocationResult, Fields which are defined in GetLocationResult
public class GetLocationResult {

	private List<String> canonicalStrings;
	private Coordinate coordinate;
	private Envelope envelope;
	private String geocoderName;
	private ClientUserDataInfo userData;
	// @extract-end

	/**
	 * Get the canonical strings which were matched (may be the same as the original request, or different when
	 * matching is a bit fuzzy). This should return the preferred strings for this location.
	 *
	 * @return matched strings or null when nothing better can be given
	 */
	public List<String> getCanonicalStrings() {
		return canonicalStrings;
	}

	/**
	 * Set the preferred or canonical strings for searching/describing the returned location. Should not be set when
	 * identical to the search strings.
	 *
	 * @param canonicalStrings matched strings
	 */
	public void setCanonicalStrings(List<String> canonicalStrings) {
		this.canonicalStrings = canonicalStrings;
	}

	/**
	 * Result coordinate. This should not be set (and may be ignored) if the envelope is set.
	 *
	 * @return coordinate for the result
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * Result coordinate. This should not be set (and may be ignored) if the envelope is set.
	 *
	 * @param coordinate result coordinate
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * Result envelope. When this is set the coordinate should not be set. If this is not set (or null) then the
	 * coordinate	 has to be set.
	 *
	 * @return result envelope
	 */
	public Envelope getEnvelope() {
		return envelope;
	}

	/**
	 * Result envelope. When this is set the coordinate should not be set. If this is not set (or null) then the
	 * coordinate has to be set.
	 *
	 * @param envelope result envelope
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
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
	 * Get extra user data for the geocoder search result.
	 *
	 * @return user data
	 */
	public ClientUserDataInfo getUserData() {
		return userData;
	}

	/**
	 * Set extra user data for the geocoder search result.
	 *
	 * @param userData user data
	 */
	public void setUserData(ClientUserDataInfo userData) {
		this.userData = userData;
	}
}
