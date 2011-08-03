/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotations.Api;
import org.geomajas.command.CommandResponse;

/**
 * Response object for the {@link org.geomajas.plugin.rasterizing.command.rasterizing.RasterizeMapCommand} command.
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RasterizeMapResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private String mapKey;

	private String legendKey;

	/**
	 * Get the map key. This is the key used to store the map in the raster cache.
	 *
	 * @return map key
	 */
	public String getMapKey() {
		return mapKey;
	}

	/**
	 * Set the map key. This is the key used to store the map in the raster cache.
	 *
	 * @param mapKey map key
	 */
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}

	/**
	 * Get the legend key. This is the key used to store the legend in the raster cache.
	 *
	 * @return legend key
	 */
	public String getLegendKey() {
		return legendKey;
	}

	/**
	 * Set the legend key. This is the key used to store the legend in the raster cache.
	 *
	 * @param legendKey legend key.
	 */
	public void setLegendKey(String legendKey) {
		this.legendKey = legendKey;
	}

}
