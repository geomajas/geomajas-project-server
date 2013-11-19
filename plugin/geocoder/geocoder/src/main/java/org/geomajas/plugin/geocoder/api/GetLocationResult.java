/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.api;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientUserDataInfo;

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
