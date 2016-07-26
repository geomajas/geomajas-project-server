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
package org.geomajas.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

/**
 * Scale unit type.
 * 
 * @author Jan De Moerloose
 * @since 1.7.0
 */
@Api(allMethods = true)
public enum ScaleUnit  implements IsInfo {

	/**
	 * scale expressed in pixels per map unit.
	 */
	PIXEL_PER_UNIT,
	/**
	 * scale expressed as a pure number, i.e. 1m on screen/1m on map.
	 */
	NORMAL
}
