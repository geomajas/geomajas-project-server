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

import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;

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

	private List<String> preferred;
	private double x, y;
	private Bbox bbox;
	private ClientUserDataInfo userData;

	/**
	 * Get list of locations to match. Each string is a regex expression.
	 * When the string starts with a "?" is is considered optional whether this level is matched.
	 * When the last string equals "**" the remaining levels are discarded is any.
	 *
	 * @return strings to match, least specific (biggest area) first.
	 */
	public List<String> getToMatch() {
		return toMatch;
	}

	/**
	 * Set list of locations to match. Each string is a regex expression.
	 * When the string starts with a "?" is is considered optional whether this level is matched.
	 * When the last string equals "**" the remaining levels are discarded is any.
	 *
	 * @param toMatch strings to match, least specific (biggest area) first.
	 */
	public void setToMatch(List<String> toMatch) {
		this.toMatch = toMatch;
	}

	/**
	 * Get preferred list of strings to indicate this location.
	 *
	 * @return preferred list of strings
	 */
	public List<String> getPreferred() {
		return preferred;
	}

	/**
	 * Set preferred list of strings to indicate this location.
	 *
	 * @param preferred preferred list of strings for this location
	 */
	public void setPreferred(List<String> preferred) {
		this.preferred = preferred;
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
