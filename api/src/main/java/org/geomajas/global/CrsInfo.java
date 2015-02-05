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

package org.geomajas.global;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

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
