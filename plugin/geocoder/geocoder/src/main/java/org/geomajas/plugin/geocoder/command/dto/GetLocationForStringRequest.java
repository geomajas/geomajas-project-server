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
import org.geomajas.command.CommandRequest;

/**
 * Request object for getting location information for a string.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GetLocationForStringRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	/** Value for maxAlternatives which does not limit the result. */
	public static final int MAX_ALTERNATIVES_UNLIMITED = 0;
	/** Default value for maxAlternatives. */
	public static final int MAX_ALTERNATIVES_DEFAULT = 50;

	/**
	 * Command name for command.
	 * @since 1.2.0
	 */
	public static final String COMMAND = "command.geocoder.GetLocationForString";

	private String location;
	private String crs;
	private String servicePattern = ".*";
	private String locale;
	private int maxAlternatives = MAX_ALTERNATIVES_DEFAULT;

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

	/**
	 * Get the locale which should be used for the search, if known.
	 *
	 * @return locale string if known, null if not known
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Set the locale which should be used for the search if known.
	 *
	 * @param locale locale string if known, null if not known
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Get the maximum number of alternatives which may be returned.
	 * <p/>
	 * The default value is to have 50 alternatives returned.
	 *
	 * @return max number of features to return of {@link #MAX_ALTERNATIVES_UNLIMITED} (0) for unlimited.
	 */
	public int getMaxAlternatives() {
		return maxAlternatives;
	}

	/**
	 * Set the maximum number of alternatives which may be returned.
	 *
	 * @param maxAlternatives max number of alternatives to return, or {@link #MAX_ALTERNATIVES_UNLIMITED} (0) for
	 * unlimited (which is not recommended to use).
	 */
	public void setMaxAlternatives(int maxAlternatives) {
		this.maxAlternatives = maxAlternatives;
	}

}
