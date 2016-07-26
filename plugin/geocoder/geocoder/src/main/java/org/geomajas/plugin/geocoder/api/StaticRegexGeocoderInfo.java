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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configuration for {@link org.geomajas.plugin.geocoder.service.StaticRegexGeocoderService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class StaticRegexGeocoderInfo {

	@NotNull
	private String crs;

	private List<StaticRegexGeocoderLocationInfo> locations;

	/**
	 * CRS for locations.
	 *
	 * @return CRS code
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set CRS for locations.
	 *
	 * @param crs CRS code
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get locations to match.
	 *
	 * @return locations
	 */
	public List<StaticRegexGeocoderLocationInfo> getLocations() {
		return locations;
	}

	/**
	 * Set locations to match.
	 *
	 * @param locations locations
	 */
	public void setLocations(List<StaticRegexGeocoderLocationInfo> locations) {
		this.locations = locations;
	}
}
