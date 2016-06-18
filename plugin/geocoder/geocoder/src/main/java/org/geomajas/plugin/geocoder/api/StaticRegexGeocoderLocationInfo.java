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
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configure one location for {@link org.geomajas.plugin.geocoder.service.StaticRegexGeocoderService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class StaticRegexGeocoderLocationInfo {

	@NotNull
	private List<String> toMatch;

	private List<String> canonical;
	private double x, y;
	private Bbox bbox;
	private ClientUserDataInfo userData;

	/**
	 * Get list of locations to match. Each string is a regex expression. When the string starts with a "?" is is
	 * considered optional whether this level is matched. When the last string equals "**" the remaining levels are
	 * discarded is any.
	 *
	 * @return strings to match, least specific (biggest area) first.
	 */
	public List<String> getToMatch() {
		return toMatch;
	}

	/**
	 * Set list of locations to match. Each string is a regex expression. When the string starts with a "?" is is
	 * considered optional whether this level is matched. When the last string equals "**" the remaining levels are
	 * discarded is any.
	 *
	 * @param toMatch strings to match, least specific (biggest area) first.
	 */
	public void setToMatch(List<String> toMatch) {
		this.toMatch = toMatch;
	}

	/**
	 * Get canonical list of strings to indicate this location.
	 *
	 * @return preferred list of strings
	 */
	public List<String> getCanonical() {
		return canonical;
	}

	/**
	 * Set canonical list of strings to indicate this location.
	 *
	 * @param canonical canonical list of strings for this location
	 */
	public void setCanonical(List<String> canonical) {
		this.canonical = canonical;
	}

	/**
	 * X ordinate for this location (only used when no bbox).
	 *
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * X ordinate for this location (only used when no bbox).
	 *
	 * @param x x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Y ordinate for this location (only used when no bbox).
	 *
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Y ordinate for this location (only used when no bbox).
	 *
	 * @param y y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Bounding box for the are matching this location.
	 *
	 * @return area for location
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * Bounding box for the are matching this location.
	 *
	 * @param bbox area for location
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
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
