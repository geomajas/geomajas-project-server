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

import org.geomajas.command.CommandResponse;
import org.geomajas.global.Api;

/**
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 *
 */
@Api(allMethods = true)
public class RasterizeMapResponse extends CommandResponse {

	private String mapKey;

	private String legendKey;

	public String getMapKey() {
		return mapKey;
	}

	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}

	public String getLegendKey() {
		return legendKey;
	}

	public void setLegendKey(String legendKey) {
		this.legendKey = legendKey;
	}

}
