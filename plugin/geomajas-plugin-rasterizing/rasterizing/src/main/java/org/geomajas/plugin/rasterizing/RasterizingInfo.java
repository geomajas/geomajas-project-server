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

package org.geomajas.plugin.rasterizing;

import org.geomajas.global.Api;

/**
 * Configuration of rasterizing behaviour.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RasterizingInfo {

	private RasterizingMoment rasterizingMoment;

	/**
	 * When should the rasterizing be done. See {@link RasterizingMoment} for options.
	 *
	 * @return rasterizing moment
	 */
	public RasterizingMoment getRasterizingMoment() {
		return rasterizingMoment;
	}

	/**
	 * Set when the rasterizing should be done. See {@link RasterizingMoment} for options.
	 *
	 * @param rasterizingMoment rasterizing moment
	 */
	public void setRasterizingMoment(RasterizingMoment rasterizingMoment) {
		this.rasterizingMoment = rasterizingMoment;
	}
}
