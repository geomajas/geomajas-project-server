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

import org.geomajas.global.Api;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configuration for {@link org.geomajas.plugin.geocoder.service.StaticRegexMatchService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class StaticRegexMatchInfo {

	@NotNull
	private String crs;

	private List<StaticRegexMatchLocationInfo> locations;

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
	public List<StaticRegexMatchLocationInfo> getLocations() {
		return locations;
	}

	/**
	 * Set locations to match.
	 *
	 * @param locations locations
	 */
	public void setLocations(List<StaticRegexMatchLocationInfo> locations) {
		this.locations = locations;
	}
}
