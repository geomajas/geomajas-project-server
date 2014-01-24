/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Available options to limit (e.g. the map view) bounds when applying the maxBounds limitation.
 *
 * @author An Buyle
 * @since 1.10.0
 */
@Api(allMethods = true)
public enum BoundsLimitOption implements IsInfo {

	/**
	 *  View center must be within maxBounds.
	 */
	CENTER_WITHIN_MAX_BOUNDS,
	/** 
	 * The whole map view must lay completely within the maximum allowed bounds (maxBounds). 
	 */
	COMPLETELY_WITHIN_MAX_BOUNDS;

	private static final long serialVersionUID = 1100L;

}
