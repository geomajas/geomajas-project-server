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

package org.geomajas.global;

import javax.validation.constraints.NotNull;

/**
 * Description object for adding CRS definitions to Geomajas.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api(allMethods = true)
public class CrsInfo {

	@NotNull
	private String key;

	@NotNull
	private String crsWkt;

	/**
	 * Key for registration of CRS. Should include the authority, for example "EPSG:900913".
	 *
	 * @return crs registration key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Key for registration of CRS. Should include the authority, for example "EPSG:900913".
	 *
	 * @param key crs registration key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get the WKT string for the coordinate reference system.
	 *
	 * @return WKT string for the CRS
	 */
	public String getCrsWkt() {
		return crsWkt;
	}

	/**
	 * Set the WKT string for the coordinate reference system.
	 *
	 * @param crsWkt WKT string for the CRS
	 */
	public void setCrsWkt(String crsWkt) {
		this.crsWkt = crsWkt;
	}
}
