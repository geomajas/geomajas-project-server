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

package org.geomajas.plugin.geocoder.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

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
